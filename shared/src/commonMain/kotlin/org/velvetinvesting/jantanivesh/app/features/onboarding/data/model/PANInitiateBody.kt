package org.velvetinvesting.jantanivesh.app.features.onboarding.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PANInitiateBody(
    val date_of_birth: String,
    val name: String,
    val pan: String
)