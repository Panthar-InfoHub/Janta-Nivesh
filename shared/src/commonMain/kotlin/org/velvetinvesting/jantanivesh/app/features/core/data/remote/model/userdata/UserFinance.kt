package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class UserFinance(
    val annual_income: String,
    val createdAt: String,
    val expense_food: String,
    val expense_house: String,
    val expense_others: String,
    val expense_transportation: String,
    val id: String,
    val updatedAt: String,
    val user_id: String
)