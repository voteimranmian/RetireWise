# Multi-stage build for the Phase 3 authentication service skeleton.
# Build context must be the repository root, e.g.:
#   docker build -f backend/authentication/Dockerfile -t retirewise-authentication .

FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew :backend:authentication:installDist --no-daemon

FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app
COPY --from=build /workspace/backend/authentication/build/install/authentication /app
EXPOSE 8080
ENTRYPOINT ["/app/bin/authentication"]
