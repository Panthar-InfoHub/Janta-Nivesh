package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.usercart

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val lump_sum_items: List<LumpSumItem>,
    val sip_items: List<SipItem>
)