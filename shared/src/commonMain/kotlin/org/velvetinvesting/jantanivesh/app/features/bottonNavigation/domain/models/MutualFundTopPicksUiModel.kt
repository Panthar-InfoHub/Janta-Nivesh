package org.velvetinvesting.jantanivesh.app.features.bottonNavigation.domain.models

data class MutualFundTopPicksUiModel(
    val icon: String,
    val name: String,
    val metadata: String,
    val returnYears: Int,
    val percentage: Double?,
    val id: String
)
