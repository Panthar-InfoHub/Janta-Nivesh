package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class UserHomeData(
    val fire_number: FireNumber,
    val net_worth: Double,
    val total_expenses: TotalExpenses,
    val fire_percentage: FirePercentageDto
)

@Serializable
data class FirePercentageDto(
    val emi_exclude: Double,
    val emi_include: Double
)