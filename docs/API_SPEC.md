# RetireWise — API Spec

Not implemented in Phase 0. No backend exists yet.

Use versioned REST APIs. Example endpoints:

```
POST /api/v1/auth/session
GET /api/v1/profile
PUT /api/v1/profile
POST /api/v1/plans
GET /api/v1/plans/{planId}
POST /api/v1/projections
POST /api/v1/scenarios
POST /api/v1/scenarios/compare
POST /api/v1/advisor/messages
GET /api/v1/government-programs
GET /api/v1/government-programs/{programId}
GET /api/v1/action-plan
POST /api/v1/reports
GET /api/v1/consents
PUT /api/v1/consents
DELETE /api/v1/account
```

Generate an OpenAPI specification. Generate Kotlin client models from the approved API specification where practical. All mutation endpoints must support idempotency.
