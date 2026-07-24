# ADR 0004: AWS CDK (Kotlin), ECS Fargate, and a dev-only environment for the Phase 3 backend

## Context

`docs/ARCHITECTURE.md` section 14.2 specifies the intended backend technology (Kotlin + Ktor, PostgreSQL, Redis, managed secrets, container-based deployment "in a Canadian region where available") but Phase 0 explicitly notes no backend infrastructure exists yet. Phase 3 (Authentication and consent) cannot move past its client-side scaffold — `NotConfiguredAuthRepository` honestly reports every sign-in provider as unavailable — without a real backend account service to authenticate against.

The user has an AWS account and asked to start provisioning real infrastructure there. This requires deciding: which infrastructure-as-code tool, which compute platform for the Ktor service, and how much environment scope (dev only vs. dev+prod) to provision up front. These were resolved directly with the user via explicit choices before any code was written: **AWS**, **AWS CDK in Kotlin**, **ECS Fargate**, **dev environment only**.

## Decision

Use **AWS CDK (`software.amazon.awscdk:aws-cdk-lib` 2.199.0, `software.constructs:constructs` 10.4.2)**, written in Kotlin as a new `infra/` Gradle module, to define AWS infrastructure as versioned code alongside the application. Host the Ktor backend on **ECS Fargate**. Provision **one environment: dev**, in **`ca-central-1` (Montreal)** — the Canadian region `ARCHITECTURE.md` calls for. The first stack (`RetireWiseDevStack`) provisions only what's needed to prove the deploy pipeline end to end: a VPC, an ECR repository, an ECS cluster/service running a health-check-only Ktor skeleton, and an RDS PostgreSQL instance with credentials generated into AWS Secrets Manager. Redis, a load balancer/TLS/custom domain, autoscaling, and a prod environment are explicitly deferred to later slices.

## Alternatives considered

- **Terraform**: mature, cloud-agnostic, plain-HCL diffs are easy to review in PRs. Not chosen because the user preferred infrastructure defined in Kotlin, consistent with the rest of the codebase, and CDK's typed constructs catch some classes of misconfiguration (e.g. wrong resource references) at compile time rather than plan time.
- **AWS App Runner**: simplest possible container hosting (push an image, get an HTTPS URL), but less control over VPC/networking, which the app will need for private RDS access and, later, VPC-scoped services (Redis, internal-only endpoints). Fargate gives that control without materially more setup.
- **EC2**: cheapest at small, always-on scale, but the team would own OS patching and scaling. Rejected in favor of Fargate's serverless container model, matching `ARCHITECTURE.md`'s "container based deployment" recommendation without the operational burden.
- **Firebase Auth-only (no custom backend)**: `ARCHITECTURE.md` notes Firebase "may be used" for auth. Not chosen for this slice because `docs/DATA_MODEL.md` and `docs/API_SPEC.md` already assume a versioned REST API (`/api/v1/...`) backed by RetireWise's own services (profile, consent, audit) beyond just authentication, which Firebase alone doesn't provide.
- **Dev + prod environments now**: rejected for this slice — the user explicitly chose dev-only, to avoid paying for and maintaining a prod environment before the app has anything worth protecting in production.

## Advantages

1. Infrastructure is versioned, code-reviewed, and reproducible — no manual console configuration to lose track of.
2. Kotlin CDK keeps the entire stack (app, backend, infra) in one language and one Gradle build.
3. Fargate + private RDS subnets satisfies `docs/SECURITY.md`'s "least privilege," "encrypt at rest/in transit," and "secrets in a managed secret service" requirements from day one, rather than retrofitting them later.
4. The dev-only, no-NAT, no-ALB, single-task scope keeps monthly cost low (roughly RDS `db.t4g.micro` + one Fargate task + ECR/Secrets Manager/CloudWatch — no NAT Gateway or Application Load Balancer charges) while still proving the real deploy path.

## Risks

1. **No NAT Gateway**: Fargate tasks run in public subnets with `assignPublicIp = true` to avoid the ~$32/month NAT Gateway cost during dev. This is acceptable because the tasks have no public inbound rules (no ALB yet) and RDS stays in isolated subnets — but it means outbound traffic from the service is not routed through a fixed NAT IP, which would matter if a third-party API ever needs to allowlist by IP. Revisit if that becomes necessary.
2. **No load balancer / TLS / custom domain**: the service is not reachable from the internet by a stable address yet. This is fine while nothing calls it, but must be added before the mobile app can use it in any build users install.
3. **Single AZ, single task, no autoscaling**: acceptable for a dev environment with no real users; must not be reused as-is for prod.
4. **RDS deletion protection is off and removal policy is `DESTROY`**: intentional for a disposable dev database, but this must be flipped before any real user data is stored anywhere near this stack.
5. **Cost creep**: even a "cheap" dev stack (RDS + Fargate + ECR + Secrets Manager + CloudWatch Logs) accrues ongoing charges if left running indefinitely. The user should monitor AWS Cost Explorer and consider tearing the stack down (`cdk destroy`) between periods of active backend development.

## Consequences

`infra/` becomes a real Gradle module depending on `aws-cdk-lib`; `backend/authentication` becomes a real Ktor module with only a `/health` endpoint for now. `docs/ARCHITECTURE.md` and `docs/RELEASE_PLAN.md` are updated to reflect this. Actually deploying (`cdk bootstrap` / `cdk deploy`) requires the user to install Node.js, Docker, and configure AWS credentials locally — none of which exist in the environment this ADR was written in — and remains a separate, explicitly confirmed step per deploy, since it creates real, billed AWS resources.

## Review date

Revisit before Phase 3 moves past the health-check skeleton into real sign-in/session endpoints (need to decide on ALB + TLS + domain), and again before any staging/prod environment is provisioned (need to decide on multi-AZ, NAT, autoscaling, and stricter deletion protection).
