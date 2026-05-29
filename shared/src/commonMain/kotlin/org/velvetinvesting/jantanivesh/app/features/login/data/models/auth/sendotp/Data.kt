package org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.sendotp

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val phone_no: String,
    val user_id: String
)