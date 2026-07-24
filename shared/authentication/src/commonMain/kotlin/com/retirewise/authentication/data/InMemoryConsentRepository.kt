package com.retirewise.authentication.data

import com.retirewise.authentication.domain.ConsentRecord
import com.retirewise.authentication.domain.ConsentRepository
import com.retirewise.authentication.domain.ConsentType

/**
 * Phase 3 client-side scaffold: holds consent decisions in memory only, so
 * they do not survive process death and are not synced to a backend. Real
 * persistence (local storage and/or backend sync, per docs/SECURITY.md
 * section 23.2) lands once that infrastructure exists.
 */
class InMemoryConsentRepository : ConsentRepository {
    private val records = mutableMapOf<ConsentType, ConsentRecord>()

    override suspend fun recordConsent(record: ConsentRecord) {
        records[record.type] = record
    }

    override suspend fun isGranted(type: ConsentType): Boolean = records[type]?.granted == true
}
