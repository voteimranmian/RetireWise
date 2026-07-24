package com.retirewise.authentication.domain

/**
 * A record of a user's decision on a [ConsentType], including the version of
 * the consent copy they responded to (docs/SECURITY.md section 23.2.8:
 * "Record the consent version").
 */
data class ConsentRecord(
    val type: ConsentType,
    val version: String,
    val granted: Boolean,
)

/** The consent copy version currently shown to users, per [ConsentType]. */
object ConsentVersions {
    const val PRIVACY = "1.0"
    const val AI_DATA = "1.0"
}
