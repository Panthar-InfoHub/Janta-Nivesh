package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models

data class FDTimelineDomain(
    val startDate: String,
    val maturityDate: String,
    val daysRemaining: Int
)
