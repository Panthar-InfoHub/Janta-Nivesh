package org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class Onboarding(
    val current_stage: String,
    val is_completed: Boolean,
    val stages: Stages
)