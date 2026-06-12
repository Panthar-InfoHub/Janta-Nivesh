package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.allbundles

import kotlinx.serialization.Serializable

@Serializable
data class AllBundlesDto(
    val data: AllBundlesDataDto,
    val message: String,
    val success: Boolean
)

@Serializable
data class AllBundlesDataDto(
    val bundles: List<Data>
)