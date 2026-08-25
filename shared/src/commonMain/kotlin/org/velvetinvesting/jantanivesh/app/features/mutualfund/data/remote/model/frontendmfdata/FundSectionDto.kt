package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.frontendmfdata

import kotlinx.serialization.Serializable

@Serializable
data class FundSectionDto(
    val tag: String,
    val title: String,
    val funds: List<SectionFundDto> = emptyList()
)
