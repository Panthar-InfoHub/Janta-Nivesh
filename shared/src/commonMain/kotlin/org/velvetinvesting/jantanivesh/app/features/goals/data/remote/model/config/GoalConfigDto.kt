package org.velvetinvesting.jantanivesh.app.features.goals.data.remote.model.config

import kotlinx.serialization.Serializable

/** `GET /user-goal/config` — what each goal type asks for and the defaults it is sized with. */
@Serializable
data class GoalConfigResponseDto(
    val success: Boolean = false,
    val message: String = "",
    val `data`: List<GoalConfigDto> = emptyList()
)

@Serializable
data class GoalConfigDto(
    val id: String,
    val goal_type_id: Int,
    val name: String,
    val purpose: String = "",
    val calculation_mode: String = "",
    /** Null for "Build My Savings": a chosen corpus is not inflated. */
    val inflation_rate: Double? = null,
    val expected_return_rate: Double? = null,
    val min_years: Int = 1,
    val max_years: Int = 30,
    val tenure_suggestions: List<Int> = emptyList(),
    val cost_chips: List<Long> = emptyList(),
    /** Non-null only where the type has variants to pick from (the vehicle goal). */
    val asset_subtypes: List<String>? = null,
    val config_version: String = ""
)
