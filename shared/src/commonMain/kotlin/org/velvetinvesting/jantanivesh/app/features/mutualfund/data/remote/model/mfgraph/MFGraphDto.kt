package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfgraph

import kotlinx.serialization.Serializable

@Serializable
data class MFGraphDto(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)