package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class UserPortFolioDto(
    val code: Int,
    val `data`: Data,
    val message: String
)