package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.fundredeem.response

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val payment_link: String
)