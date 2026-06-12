package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class BundleFunds(
    val items: List<Item>,
    val key: String,
    val title: String,
)