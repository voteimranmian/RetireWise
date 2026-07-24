package com.retirewise.infra

import software.amazon.awscdk.CfnOutput
import software.amazon.awscdk.Duration
import software.amazon.awscdk.RemovalPolicy
import software.amazon.awscdk.Stack
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.services.ec2.InstanceClass
import software.amazon.awscdk.services.ec2.InstanceSize
import software.amazon.awscdk.services.ec2.InstanceType
import software.amazon.awscdk.services.ec2.Port
import software.amazon.awscdk.services.ec2.SecurityGroup
import software.amazon.awscdk.services.ec2.SubnetConfiguration
import software.amazon.awscdk.services.ec2.SubnetSelection
import software.amazon.awscdk.services.ec2.SubnetType
import software.amazon.awscdk.services.ec2.Vpc
import software.amazon.awscdk.services.ecr.LifecycleRule
import software.amazon.awscdk.services.ecr.Repository
import software.amazon.awscdk.services.ecr.TagStatus
import software.amazon.awscdk.services.ecs.AwsLogDriverProps
import software.amazon.awscdk.services.ecs.Cluster
import software.amazon.awscdk.services.ecs.ContainerDefinitionOptions
import software.amazon.awscdk.services.ecs.ContainerImage
import software.amazon.awscdk.services.ecs.FargateService
import software.amazon.awscdk.services.ecs.FargateTaskDefinition
import software.amazon.awscdk.services.ecs.HealthCheck
import software.amazon.awscdk.services.ecs.LogDriver
import software.amazon.awscdk.services.ecs.PortMapping
import software.amazon.awscdk.services.ecs.Protocol
import software.amazon.awscdk.services.logs.LogGroup
import software.amazon.awscdk.services.logs.RetentionDays
import software.amazon.awscdk.services.rds.Credentials
import software.amazon.awscdk.services.rds.DatabaseInstance
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine
import software.amazon.awscdk.services.rds.PostgresEngineVersion
import software.amazon.awscdk.services.rds.PostgresInstanceEngineProps
import software.constructs.Construct
import software.amazon.awscdk.services.ecs.Secret as EcsSecret

/**
 * Dev-only AWS environment for the Phase 3 backend account service scaffold.
 * See docs/ADR/0004-aws-cdk-ecs-fargate-dev-infrastructure.md for why these
 * choices were made (cost-conscious dev slice: no NAT gateway, no load
 * balancer, single-AZ, desiredCount 1).
 *
 * This stack only provisions infrastructure. It does not push a container
 * image — the ECS service will fail to start tasks until an image tagged
 * "latest" is pushed to the ECR repository this stack creates (see
 * backend/authentication/README.md for the manual `docker build`/`docker
 * push` step, which requires Docker locally).
 */
class RetireWiseDevStack(
    scope: Construct,
    id: String,
    props: StackProps,
) : Stack(scope, id, props) {
    init {
        val vpc =
            Vpc.Builder
                .create(this, "Vpc")
                .maxAzs(2)
                .natGateways(0)
                .subnetConfiguration(
                    listOf(
                        SubnetConfiguration
                            .builder()
                            .name("public")
                            .subnetType(SubnetType.PUBLIC)
                            .cidrMask(24)
                            .build(),
                        SubnetConfiguration
                            .builder()
                            .name("isolated")
                            .subnetType(SubnetType.PRIVATE_ISOLATED)
                            .cidrMask(24)
                            .build(),
                    ),
                ).build()

        val repository =
            Repository.Builder
                .create(this, "AuthenticationRepository")
                .repositoryName("retirewise-authentication-dev")
                .imageScanOnPush(true)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build()
        repository.addLifecycleRule(
            LifecycleRule
                .builder()
                .tagStatus(TagStatus.UNTAGGED)
                .maxImageAge(Duration.days(14))
                .build(),
        )

        val cluster =
            Cluster.Builder
                .create(this, "Cluster")
                .vpc(vpc)
                .clusterName("retirewise-dev")
                .build()

        val logGroup =
            LogGroup.Builder
                .create(this, "AuthenticationLogGroup")
                .logGroupName("/retirewise/dev/authentication")
                .retention(RetentionDays.TWO_WEEKS)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build()

        val databaseSecurityGroup =
            SecurityGroup.Builder
                .create(this, "DatabaseSecurityGroup")
                .vpc(vpc)
                .description("Postgres access for the authentication Fargate service only")
                .allowAllOutbound(false)
                .build()

        val database =
            DatabaseInstance.Builder
                .create(this, "AuthenticationDatabase")
                .engine(
                    DatabaseInstanceEngine.postgres(
                        PostgresInstanceEngineProps
                            .builder()
                            .version(PostgresEngineVersion.VER_16_9)
                            .build(),
                    ),
                ).vpc(vpc)
                .vpcSubnets(SubnetSelection.builder().subnetType(SubnetType.PRIVATE_ISOLATED).build())
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE4_GRAVITON, InstanceSize.MICRO))
                .credentials(Credentials.fromGeneratedSecret("retirewise_admin"))
                .databaseName("retirewise_dev")
                .allocatedStorage(20)
                .securityGroups(listOf(databaseSecurityGroup))
                .publiclyAccessible(false)
                .backupRetention(Duration.days(1))
                .deletionProtection(false)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build()
        val databaseSecret = requireNotNull(database.secret) { "RDS generated secret was not created" }

        val taskDefinition =
            FargateTaskDefinition.Builder
                .create(this, "AuthenticationTaskDefinition")
                .cpu(256)
                .memoryLimitMiB(512)
                .build()

        val container =
            taskDefinition.addContainer(
                "authentication",
                ContainerDefinitionOptions
                    .builder()
                    .image(ContainerImage.fromEcrRepository(repository, "latest"))
                    .logging(
                        LogDriver.awsLogs(
                            AwsLogDriverProps
                                .builder()
                                .streamPrefix("authentication")
                                .logGroup(logGroup)
                                .build(),
                        ),
                    ).environment(
                        mapOf(
                            "DB_HOST" to database.instanceEndpoint.hostname,
                            "DB_PORT" to database.instanceEndpoint.port.toString(),
                            "DB_NAME" to "retirewise_dev",
                        ),
                    ).secrets(
                        mapOf(
                            "DB_USERNAME" to EcsSecret.fromSecretsManager(databaseSecret, "username"),
                            "DB_PASSWORD" to EcsSecret.fromSecretsManager(databaseSecret, "password"),
                        ),
                    ).healthCheck(
                        HealthCheck
                            .builder()
                            .command(listOf("CMD-SHELL", "wget -q --spider http://localhost:8080/health || exit 1"))
                            .interval(Duration.seconds(30))
                            .timeout(Duration.seconds(5))
                            .retries(3)
                            .build(),
                    ).build(),
            )
        container.addPortMappings(
            PortMapping.builder().containerPort(8080).protocol(Protocol.TCP).build(),
        )

        val serviceSecurityGroup =
            SecurityGroup.Builder
                .create(this, "AuthenticationServiceSecurityGroup")
                .vpc(vpc)
                .description("Authentication Fargate service. No inbound rules: no load balancer in this slice.")
                .allowAllOutbound(true)
                .build()
        databaseSecurityGroup.addIngressRule(
            serviceSecurityGroup,
            Port.tcp(5432),
            "Allow Postgres access from the authentication Fargate service",
        )

        val service =
            FargateService.Builder
                .create(this, "AuthenticationService")
                .cluster(cluster)
                .taskDefinition(taskDefinition)
                .desiredCount(1)
                .assignPublicIp(true)
                .vpcSubnets(SubnetSelection.builder().subnetType(SubnetType.PUBLIC).build())
                .securityGroups(listOf(serviceSecurityGroup))
                .build()

        CfnOutput.Builder.create(this, "RepositoryUri").value(repository.repositoryUri).build()
        CfnOutput.Builder.create(this, "ClusterName").value(cluster.clusterName).build()
        CfnOutput.Builder.create(this, "ServiceName").value(service.serviceName).build()
        CfnOutput.Builder.create(this, "DatabaseEndpoint").value(database.instanceEndpoint.hostname).build()
    }
}
