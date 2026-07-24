package com.retirewise.infra

import software.amazon.awscdk.App
import software.amazon.awscdk.Environment
import software.amazon.awscdk.StackProps
import software.amazon.awscdk.Tags

/**
 * Entry point invoked by the CDK CLI (see infra/cdk.json). Synthesizes the
 * dev environment stack for the Phase 3 backend account service scaffold
 * (docs/RELEASE_PLAN.md, docs/ADR/0004-aws-cdk-ecs-fargate-dev-infrastructure.md).
 *
 * Reads the target AWS account/region from the CDK_DEFAULT_ACCOUNT /
 * CDK_DEFAULT_REGION environment variables that the CDK CLI sets from your
 * locally configured AWS credentials, rather than hardcoding either value.
 */
fun main() {
    val app = App()

    val account = System.getenv("CDK_DEFAULT_ACCOUNT")
    val region = System.getenv("CDK_DEFAULT_REGION") ?: "ca-central-1"

    val stack =
        RetireWiseDevStack(
            app,
            "RetireWiseDevStack",
            StackProps
                .builder()
                .env(Environment.builder().account(account).region(region).build())
                .description("RetireWise Phase 3 dev environment: VPC, ECR, ECS Fargate, RDS Postgres.")
                .build(),
        )

    Tags.of(stack).add("Project", "RetireWise")
    Tags.of(stack).add("Environment", "dev")
    Tags.of(stack).add("ManagedBy", "cdk")

    app.synth()
}
