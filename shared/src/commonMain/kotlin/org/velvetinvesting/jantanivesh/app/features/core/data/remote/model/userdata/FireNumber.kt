package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class FireNumber(
    val emi_exclude: Double,
    val emi_include: Double
)