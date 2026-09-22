package org.velvetinvesting.jantanivesh.app.features.goals.domain.models

import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType

/**
 * A goal the user holds, as `GET /user-goal/` and `GET /user-goal/{id}` return it.
 *
 * The projection fields are the server's own `v2.0` arithmetic. Nothing here is recalculated on
 * the client: two devices showing different SIP figures for the same goal would be worse than
 * showing a figure that is a few hours stale.
 */
data class GoalDomain(
    val id: String,
    val userId: String,
    val goalTypeId: Int,
    val goalType: GoalType?,
    /** Empty when the server titles the goal from the child instead; see [displayName]. */
    val goalName: String,
    val childName: String?,
    val childAge: Int?,
    val assetSubtype: String?,
    val yearsRemaining: Int,
    val targetDate: String?,
    /** Set for every type but "Build My Savings". */
    val currentCost: Double?,
    /** Set only for "Build My Savings". */
    val targetAmount: Double?,
    val currentSavings: Double,
    /** Fractions, as stored: 0.06 is 6%. */
    val inflationRate: Double?,
    val expectedReturnRate: Double?,
    val futureTargetAmount: Double,
    val fvCurrentSavings: Double,
    val netRequiredCorpus: Double,
    val requiredMonthlySip: Double,
    val requiredLumpsumToday: Double,
    val status: String,
    val calculationVersion: String?,
    val createdAt: String?,
    val updatedAt: String?,
    /** As reported by the server, so the app and the backend agree on a goal's progress. */
    val progressPercent: Int,
    /**
     * Schemes mapped to this goal. `GET /user-goal/{id}` does not return them under `v2.0`, so
     * this is empty until it does.
     */
    val schemes: List<GoalSchemeDomain> = emptyList()
) {
    /** The present-day amount the goal was sized from, whichever field carries it. */
    val baseAmount: Double
        get() = currentCost ?: targetAmount ?: 0.0

    val displayName: String
        get() = goalName.ifBlank {
            childName?.let { child -> "$child's ${goalType?.childGoalNoun ?: "Goal"}" }
                ?: goalType?.displayName
                ?: "Goal"
        }

    /** Taken from `target_date` rather than added to today's year, which can disagree by a day. */
    val targetYear: Int?
        get() = targetDate?.take(4)?.toIntOrNull()

    val isActive: Boolean
        get() = status.equals(ACTIVE, ignoreCase = true)

    companion object {
        const val ACTIVE = "ACTIVE"
    }
}

data class GoalSchemeDomain(
    val actualFolio: String,
    val balUnits: String,
    val currentVal: String,
    val folio: String,
    val nav: String,
    val schemeId: String,
    val schemeName: String
)
