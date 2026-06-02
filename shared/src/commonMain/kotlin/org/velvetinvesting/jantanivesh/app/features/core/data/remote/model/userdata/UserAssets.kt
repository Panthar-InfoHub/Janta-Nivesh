package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class UserAssets(
    val cash_saving: String,
    val createdAt: String,
    val fd: String,
    val gold: String,
    val id: String,
    val mutual_funds: String,
    val real_estate: String,
    val stocks: String,
    val updatedAt: String,
    val user_id: String
)