package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class ItemX(
    val items: List<ItemXX>,
    val key: String,
    val title: String
)