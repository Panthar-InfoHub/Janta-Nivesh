package org.velvetinvesting.jantanivesh.app.features.goals.data.mapper

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.CreateGoalRequest
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalCalculationRequest

/**
 * Request bodies for the goal endpoints, built key by key.
 *
 * They are `JsonObject`s rather than serializable classes because the shared client encodes nulls
 * (`explicitNulls` is on), and these endpoints read an absent field differently from a null one:
 * omitting `inflation_rate` means "use the type's configured rate", and the fields a type does
 * not use — `target_amount` for a cost-based goal, `child_age` for a vehicle — are expected to be
 * left out entirely rather than sent empty.
 */

/** Writes the key only when [value] is present. */
private fun JsonObjectBuilder.putIfPresent(key: String, value: Double?) {
    if (value != null) put(key, value)
}

private fun JsonObjectBuilder.putIfPresent(key: String, value: Int?) {
    if (value != null) put(key, value)
}

private fun JsonObjectBuilder.putIfNotBlank(key: String, value: String?) {
    if (!value.isNullOrBlank()) put(key, value)
}

/** `POST /user-goal/` */
fun CreateGoalRequest.toCreateBody(): JsonObject = buildJsonObject {
    put("goal_type_id", goalType.id)

    if (goalType.needsChildDetails) {
        putIfNotBlank("child_name", childName)
        putIfPresent("child_age", childAge)
    } else {
        putIfNotBlank("goal_name", goalName)
    }
    if (goalType.needsAssetSubtype) {
        putIfNotBlank("asset_subtype", assetSubtype)
    }

    put("years_remaining", yearsRemaining)

    if (goalType.usesTargetAmount) {
        putIfPresent("target_amount", targetAmount)
    } else {
        putIfPresent("current_cost", currentCost)
    }
    put("current_savings", currentSavings)

    // Rates are optional: sent only where the user overrode the type's default.
    putIfPresent("inflation_rate", inflationRate)
    putIfPresent("expected_return_rate", expectedReturnRate)
}

/** `POST /user-goal/calculate` */
fun GoalCalculationRequest.toCalculateBody(): JsonObject = buildJsonObject {
    put("goal_type_id", goalTypeId)
    put("years_remaining", yearsRemaining)
    putIfPresent("current_cost", currentCost)
    putIfPresent("target_amount", targetAmount)
    put("current_savings", currentSavings)
    putIfPresent("inflation_rate", inflationRate)
    putIfPresent("expected_return_rate", expectedReturnRate)
}
