package org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    val is_onboarding_completed: Boolean,
    val onboarding_stage: Int
)