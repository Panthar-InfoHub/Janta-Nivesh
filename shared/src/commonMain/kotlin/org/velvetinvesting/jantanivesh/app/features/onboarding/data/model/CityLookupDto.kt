package org.velvetinvesting.jantanivesh.app.features.onboarding.data.model

import kotlinx.serialization.Serializable

/** `GET /frontend/city?pin=` — the city a 6-digit pincode belongs to. */
@Serializable
data class CityLookupResponseDto(
    val success: Boolean,
    val message: String,
    val city: String? = null,
    val `data`: CityLookupData? = null
)

@Serializable
data class CityLookupData(
    val city: String? = null
)
