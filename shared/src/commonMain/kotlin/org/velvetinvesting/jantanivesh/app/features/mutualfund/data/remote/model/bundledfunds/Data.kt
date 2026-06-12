package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundledfunds

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val bundles: List<Bundle>,
    val pagination: Pagination
)