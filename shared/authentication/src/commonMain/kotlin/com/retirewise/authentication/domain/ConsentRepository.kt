package com.retirewise.authentication.domain

/**
 * Records and reads [ConsentRecord]s. See
 * [com.retirewise.authentication.data.InMemoryConsentRepository] for the
 * only implementation that currently exists.
 */
interface ConsentRepository {
    suspend fun recordConsent(record: ConsentRecord)

    suspend fun isGranted(type: ConsentType): Boolean
}
