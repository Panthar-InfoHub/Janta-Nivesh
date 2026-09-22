package org.velvetinvesting.jantanivesh.app.features.goals.data.mapper

import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserGoal
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** The goal endpoints send money and rates as strings; the mapper is what makes them numbers. */
class GoalMapperTest {

    private val vehicleGoal = UserGoal(
        id = "940a7f46-212e-4c53-a5bd-09399cb2bad2",
        user_id = "54757adc-5af6-4799-b80e-dee135dafcc3",
        goal_type_id = 4,
        goal_name = "My New Car",
        asset_subtype = "CAR",
        years_remaining = 5,
        target_date = "2031-09-18T00:00:00.000Z",
        current_cost = "1000000",
        current_savings = "100000",
        inflation_rate = "0.05",
        expected_return_rate = "0.1",
        future_target_amount = "1276281.56",
        fv_current_savings = "161051",
        net_required_corpus = "1115230.56",
        required_monthly_sip = "14401.77",
        required_lumpsum_today = "692470.43",
        status = "ACTIVE",
        calculation_version = "v2.0",
        progress_percent = 8.0
    )

    @Test
    fun parsesStringAmountsAndResolvesTheType() {
        val goal = vehicleGoal.toDomain()

        assertEquals(GoalType.BuyVehicle, goal.goalType)
        assertEquals(1_000_000.0, goal.currentCost)
        assertEquals(1_276_281.56, goal.futureTargetAmount)
        assertEquals(14_401.77, goal.requiredMonthlySip)
        assertEquals(0.05, goal.inflationRate)
        assertEquals(8, goal.progressPercent)
        assertEquals(2031, goal.targetYear)
        assertNull(goal.targetAmount)
    }

    @Test
    fun summaryTargetsTheProjectedAmountNotTodaysCost() {
        val summary = vehicleGoal.toDomain().toSummary()

        assertEquals("My New Car", summary.title)
        assertEquals(100_000L, summary.amount)
        assertEquals(1_276_281L, summary.targetAmount)
        assertEquals(8, summary.progressPercent)
    }

    @Test
    fun aGoalTheServerHasNotSizedYetFallsBackToItsBaseAmount() {
        val summary = vehicleGoal.copy(future_target_amount = null).toDomain().toSummary()

        assertEquals(1_000_000L, summary.targetAmount)
    }

    @Test
    fun childGoalIsTitledFromTheChildWhenTheServerSendsNoName() {
        val goal = vehicleGoal.copy(
            goal_type_id = 1,
            goal_name = null,
            asset_subtype = null,
            child_name = "Aarav",
            child_age = 5
        ).toDomain()

        assertEquals("Aarav's Education", goal.displayName)
    }

    @Test
    fun anUnknownTypeIdStillMapsRatherThanFailing() {
        val goal = vehicleGoal.copy(goal_type_id = 99).toDomain()

        assertNull(goal.goalType)
        assertEquals("My New Car", goal.displayName)
    }
}
