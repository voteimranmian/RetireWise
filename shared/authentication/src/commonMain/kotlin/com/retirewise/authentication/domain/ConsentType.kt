package com.retirewise.authentication.domain

/**
 * A category of user consent, per docs/SECURITY.md section 23.2. [Privacy]
 * covers the app's own data handling and is required to use the app;
 * [AiData] covers sending profile information to an AI provider and is
 * optional (CLAUDE.md rule: never send personally identifiable financial
 * information to an AI provider without explicit consent).
 */
enum class ConsentType {
    Privacy,
    AiData,
}
