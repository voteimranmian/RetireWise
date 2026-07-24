# backend/authentication

Phase 3 (Authentication and consent) backend skeleton, per `docs/RELEASE_PLAN.md`.

A real Kotlin + Ktor JVM module (`./gradlew :backend:authentication:build`), not a placeholder. It currently exposes exactly one route, `GET /health`, returning `{"status":"ok"}`. This exists to prove the deploy pipeline (container build → ECR → ECS Fargate → RDS network path, defined in `infra/`) works end to end — it does **not** implement real sign-in, session issuance, OAuth verification, or consent persistence yet. Those are the next slice, once this is deployed and real Apple/Google OAuth credentials exist. See `docs/ADR/0004-aws-cdk-ecs-fargate-dev-infrastructure.md` for why AWS/CDK/Fargate were chosen.

## Running locally

```
./gradlew :backend:authentication:run
curl http://localhost:8080/health
```

## Building the container image

```
docker build -f backend/authentication/Dockerfile -t retirewise-authentication .
```

Requires Docker locally (not installed in this environment as of Phase 3). The image is pushed to the ECR repository `infra/` creates (`retirewise-authentication-dev`), tagged `latest`; the ECS service defined in `infra/` will not start healthy tasks until an image has been pushed.

## Database

`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` are injected as container environment variables by the ECS task definition in `infra/` (username/password come from AWS Secrets Manager, never hardcoded). This service does not yet connect to the database — that wiring, along with the `users`/`consents` schema and migrations (`backend/migrations`), is part of the next slice.
