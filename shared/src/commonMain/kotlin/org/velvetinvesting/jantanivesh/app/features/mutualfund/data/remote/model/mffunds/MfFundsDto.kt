package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mffunds

import kotlinx.serialization.Serializable

/** Envelope of `GET /mf/funds`. */
@Serializable
data class MfFundsDto(
    val success: Boolean = false,
    val message: String = "",
    val `data`: MfFundsData? = null
)
