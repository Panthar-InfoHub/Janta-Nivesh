package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class KycType(
    val trading: KycStatus? =null,
    val mf: KycStatus?= null
)

@Serializable
data class KycStatus(
    val status: String
)