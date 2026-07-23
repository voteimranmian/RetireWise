# RetireWise — Security

This application handles highly sensitive personal and financial information. Use privacy by design.

## 23.1 Security requirements

1. Encrypt data in transit
2. Encrypt data at rest
3. Use secure device storage for tokens
4. Use short lived access tokens
5. Rotate refresh tokens
6. Support biometric application lock
7. Apply least privilege access
8. Separate production and non production environments
9. Never use production financial data in testing
10. Mask sensitive values in logs
11. Disable sensitive screen previews where appropriate
12. Protect backend endpoints with rate limits
13. Maintain audit logs
14. Validate every API input
15. Apply database row level authorization
16. Store secrets in a managed secret service
17. Run dependency and vulnerability scanning
18. Generate a software bill of materials
19. Support remote session revocation
20. Support complete account deletion

## 23.2 AI privacy

Before sending profile information to an AI provider:

1. Obtain explicit consent
2. Explain which data will be sent
3. Minimize the data
4. Replace identifiers with internal references
5. Avoid sending names, email addresses, account numbers, or institution account identifiers
6. Apply provider retention controls where available
7. Do not permit provider training on user data where contractual controls allow
8. Record the consent version

## Data classification

Classify fields as: Public, Internal, Personal, Sensitive personal, Financial, Authentication, Derived financial. Create a data handling policy for each classification (see `PRIVACY.md`).

## Phase 0 status

No authentication, secrets, or network calls exist yet. Applicable now: no secrets committed to the repo, `local.properties` / `.env` are gitignored, and CI runs dependency checks on every module as they're added. Full security controls (encryption, token handling, rate limiting, audit logs) land starting Phase 3 (authentication) per `RELEASE_PLAN.md`.
