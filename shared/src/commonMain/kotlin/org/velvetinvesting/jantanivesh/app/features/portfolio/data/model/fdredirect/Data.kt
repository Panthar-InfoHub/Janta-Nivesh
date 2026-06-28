package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.fdredirect

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val `data`: DataX,
    val success: Boolean
)