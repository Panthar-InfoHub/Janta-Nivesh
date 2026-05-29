package org.velvetinvesting.jantanivesh.app.features.login.data.models.auth.verifyotp

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val metadata: Metadata,
    val phone_no: String,
    val user_id: String
)