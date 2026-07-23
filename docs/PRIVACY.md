# RetireWise — Privacy

## User controls (product principle 4.6)

Users can: edit assumptions, reject recommendations, create alternative scenarios, delete conversations, delete financial data, export their information, and choose whether their information can be sent to the AI service.

## Data classification

| Classification | Examples | Handling |
|---|---|---|
| Public | Marketing content, government program summaries | No restrictions |
| Internal | Feature flags, non-user config | Internal access only |
| Personal | Name, email, province | Encrypted at rest, access-logged |
| Sensitive personal | Age, marital status, household composition | Encrypted at rest, minimized before any AI call |
| Financial | Income, balances, debts, pensions | Encrypted at rest, never in analytics, minimized/masked before any AI call, explicit consent required before sending to an AI provider |
| Authentication | Tokens, credentials | Secure device storage, short lived, never logged |
| Derived financial | Projections, recommendations | Same handling as Financial; versioned for auditability |

## AI data handling (see AI_ARCHITECTURE.md, SECURITY.md 23.2)

Before sending profile information to an AI provider: obtain explicit consent, explain what will be sent, minimize the data, replace identifiers with internal references, never send names/emails/account numbers, apply provider retention controls, disable provider training on user data where contractually possible, record the consent version used.

## Canadian considerations

Prepare the product for review under applicable Canadian privacy requirements (e.g. PIPEDA and applicable provincial legislation). The final privacy and legal position must be reviewed by qualified Canadian counsel. Do not claim compliance without a completed legal and security review.

## Phase 0 status

No user data is collected yet. This document will be expanded with concrete retention periods, consent copy, and export/deletion mechanics starting Phase 3 (authentication and consent) per `RELEASE_PLAN.md`.
