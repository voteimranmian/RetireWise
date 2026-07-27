package com.retirewise.retirementengine.domain

/** A complete annual projection plus the formula-versioning metadata that produced it. */
data class VersionedProjection(
    val entries: List<AnnualProjectionEntry>,
    val metadata: CalculationMetadata,
)
