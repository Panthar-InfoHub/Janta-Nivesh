package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mfpurchasemandatestatus

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val enach_status: String,
    val mandate_id: String,
    val status: String,
    val umrn_no: String
)