package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.fdredirect

import kotlinx.serialization.Serializable

@Serializable
data class FDRedirectDto(
    val `data`: Data,
    val message: String,
    val success: Boolean
)