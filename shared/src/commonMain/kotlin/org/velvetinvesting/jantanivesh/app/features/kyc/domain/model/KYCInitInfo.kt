package org.velvetinvesting.jantanivesh.app.features.kyc.domain.model

data class KYCInitInfo(
    val digilockerUrl: String,
    val kycAccessToken: String,
    val kycTypeId: String,
    val sessionId: String,
    val userId: String
)
