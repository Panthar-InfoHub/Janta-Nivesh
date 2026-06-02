package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class UserInsurance(
    val createdAt: String,
    val health_insurance: String,
    val id: String,
    val life_insurance: String,
    val updatedAt: String,
    val user_id: String
)