package org.velvetinvesting.jantanivesh.app.features.goals.data.mapper

import kotlinx.serialization.json.jsonPrimitive
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.CreateGoalRequest
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.GoalCalculationRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * The create endpoint reads an absent field differently from a null one, so what these bodies
 * leave out matters as much as what they carry.
 */
class GoalRequestBodiesTest {

    @Test
    fun childGoalSendsChildDetailsAndNoGoalName() {
        val body = CreateGoalRequest(
            goalType = GoalType.ChildEducation,
            goalName = "ignored for a child goal",
            childName = "Aarav",
            childAge = 5,
            yearsRemaining = 10,
            currentCost = 1_000_000.0,
            currentSavings = 100_000.0
        ).toCreateBody()

        assertEquals("1", body["goal_type_id"]?.jsonPrimitive?.content)
        assertEquals("Aarav", body["child_name"]?.jsonPrimitive?.content)
        assertEquals("5", body["child_age"]?.jsonPrimitive?.content)
        assertEquals("1000000.0", body["current_cost"]?.jsonPrimitive?.content)
        assertFalse(body.containsKey("goal_name"))
        assertFalse(body.containsKey("target_amount"))
        assertFalse(body.containsKey("asset_subtype"))
    }

    @Test
    fun vehicleGoalSendsSubtypeAndName() {
        val body = CreateGoalRequest(
            goalType = GoalType.BuyVehicle,
            goalName = "My New Car",
            assetSubtype = "CAR",
            yearsRemaining = 5,
            currentCost = 1_000_000.0,
            currentSavings = 100_000.0
        ).toCreateBody()

        assertEquals("My New Car", body["goal_name"]?.jsonPrimitive?.content)
        assertEquals("CAR", body["asset_subtype"]?.jsonPrimitive?.content)
        assertFalse(body.containsKey("child_name"))
        assertFalse(body.containsKey("child_age"))
    }

    @Test
    fun savingsGoalSendsTargetAmountInsteadOfCost() {
        val body = CreateGoalRequest(
            goalType = GoalType.BuildSavings,
            goalName = "Emergency Cushion",
            yearsRemaining = 5,
            targetAmount = 1_000_000.0,
            currentSavings = 200_000.0
        ).toCreateBody()

        assertEquals("1000000.0", body["target_amount"]?.jsonPrimitive?.content)
        assertFalse(body.containsKey("current_cost"))
    }

    @Test
    fun ratesAreOmittedWhenNotOverridden() {
        val request = CreateGoalRequest(
            goalType = GoalType.BuyHome,
            goalName = "My Dream Villa",
            yearsRemaining = 7,
            currentCost = 5_000_000.0,
            currentSavings = 500_000.0
        )

        val default = request.toCreateBody()
        assertFalse(default.containsKey("inflation_rate"))
        assertFalse(default.containsKey("expected_return_rate"))

        val overridden = request.copy(inflationRate = 0.07).toCreateBody()
        assertEquals("0.07", overridden["inflation_rate"]?.jsonPrimitive?.content)
        assertFalse(overridden.containsKey("expected_return_rate"))
    }

    @Test
    fun calculateBodyCarriesOnlyTheAmountItsTypeUses() {
        val costBased = GoalCalculationRequest(
            goalTypeId = 1,
            yearsRemaining = 10,
            currentCost = 1_000_000.0,
            currentSavings = 100_000.0,
            inflationRate = 0.06,
            expectedReturnRate = 0.10
        ).toCalculateBody()

        assertTrue(costBased.containsKey("current_cost"))
        assertFalse(costBased.containsKey("target_amount"))
        assertEquals("0.06", costBased["inflation_rate"]?.jsonPrimitive?.content)

        val corpusBased = GoalCalculationRequest(
            goalTypeId = 5,
            yearsRemaining = 5,
            targetAmount = 1_000_000.0,
            currentSavings = 200_000.0
        ).toCalculateBody()

        assertTrue(corpusBased.containsKey("target_amount"))
        assertFalse(corpusBased.containsKey("current_cost"))
        assertFalse(corpusBased.containsKey("inflation_rate"))
    }
}
