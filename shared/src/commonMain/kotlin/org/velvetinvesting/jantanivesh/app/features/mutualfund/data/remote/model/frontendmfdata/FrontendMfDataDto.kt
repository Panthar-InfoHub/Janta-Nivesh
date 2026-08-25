package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.frontendmfdata

import kotlinx.serialization.Serializable

@Serializable
data class FrontendMfDataDto(
    val success: Boolean = false,
    val message: String = "",
    val `data`: Map<String, FundSectionDto> = emptyMap()
)
