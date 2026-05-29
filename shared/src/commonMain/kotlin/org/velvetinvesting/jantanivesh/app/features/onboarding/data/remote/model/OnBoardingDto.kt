package org.velvetinvesting.jantanivesh.app.features.onboarding.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class OnBoardingDto(
    val full_name: String,
    val email: String?,
    val dob: String,
)
