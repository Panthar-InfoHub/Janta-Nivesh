package org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class Stages(
    val kyc: String,
    val nominee: String,
    val penny_drop: String,
    val profile: String,
    val readiness: String
)