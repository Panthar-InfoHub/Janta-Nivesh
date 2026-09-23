package org.velvetinvesting.jantanivesh.app.features.goals.domain.models

import androidx.compose.ui.graphics.Color
import org.velvetinvesting.jantanivesh.app.core.theme.MutualFundIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.SecondaryPrimary
import org.velvetinvesting.jantanivesh.app.core.theme.Orange
import org.velvetinvesting.jantanivesh.app.core.theme.bgColor4
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType

/**
 * A goal type as the create-goal screen offers it: the config row from `GET /user-goal/config`
 * plus the colour the app paints that type in.
 *
 * Everything but [title], [color] and [goalTypeId] is a default the server supplies, so an option
 * built by [goalOptionFor] (no config loaded — a card rendered from a goal the user already has)
 * still names and colours the type correctly, it just cannot drive the form's suggestions.
 */
data class GoalOption(
    val title: String,
    val color: Color,
    val goalTypeId: Int,
    /** Null when the server sends an id this build does not know about. */
    val type: GoalType? = GoalType.fromId(goalTypeId),
    val purpose: String = "",
    val calculationMode: String = "",
    /** Fractions, as the server sends them: 0.06 is 6%. Inflation is null for a fixed corpus. */
    val inflationRate: Double? = null,
    val expectedReturnRate: Double? = null,
    val minYears: Int = 1,
    val maxYears: Int = 30,
    val tenureSuggestions: List<Int> = emptyList(),
    val costChips: List<Long> = emptyList(),
    val assetSubtypes: List<String> = emptyList(),
    /** The config row's own id, and the version it was published under. */
    val configId: String? = null,
    val configVersion: String? = null
)

/** The colour each type is drawn in, stable whether or not config has loaded. */
fun goalTypeColor(goalTypeId: Int): Color = when (goalTypeId) {
    GoalType.ChildEducation.id -> MutualFundIconBg
    GoalType.ChildMarriage.id -> Orange
    GoalType.BuyHome.id -> bgColor4
    GoalType.BuyVehicle.id -> Secondary
    GoalType.BuildSavings.id -> SecondaryPrimary
    else -> Primary
}

/**
 * A minimal option for a goal the user already holds, where all that is needed is a name and a
 * colour. [title] falls back to the type's own name, or to "Goal" for an id this build predates.
 */
fun goalOptionFor(goalTypeId: Int, title: String? = null): GoalOption {
    val type = GoalType.fromId(goalTypeId)
    return GoalOption(
        title = title?.takeIf { it.isNotBlank() } ?: type?.displayName ?: "Goal",
        color = goalTypeColor(goalTypeId),
        goalTypeId = goalTypeId,
        type = type
    )
}

/**
 * Offered until `GET /user-goal/config` answers. The rates and chips are deliberately left empty:
 * the real ones come from config, and guessing them here would show the user a projection the
 * server would disagree with.
 */
val goalOptions: List<GoalOption> = GoalType.entries.map { goalOptionFor(it.id) }
