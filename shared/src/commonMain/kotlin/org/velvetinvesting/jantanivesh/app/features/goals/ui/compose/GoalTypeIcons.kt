package org.velvetinvesting.jantanivesh.app.features.goals.ui.compose

import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.education_icon
import jantanivesh.shared.generated.resources.home_icon
import jantanivesh.shared.generated.resources.ins_car_insurance
import jantanivesh.shared.generated.resources.piggybank_icon
import jantanivesh.shared.generated.resources.ring_icon
import jantanivesh.shared.generated.resources.ruppee_circle
import org.jetbrains.compose.resources.DrawableResource
import org.velvetinvesting.jantanivesh.app.features.core.domain.GoalType

/**
 * The icon a goal type is drawn with, shared by the home and goals lists so one goal never shows
 * two different faces. An unknown type — an id added server-side after this build — falls back to
 * the generic rupee mark rather than to nothing.
 */
fun goalIconFor(type: GoalType?): DrawableResource = when (type) {
    GoalType.ChildEducation -> Res.drawable.education_icon
    GoalType.ChildMarriage -> Res.drawable.ring_icon
    GoalType.BuyHome -> Res.drawable.home_icon
    GoalType.BuyVehicle -> Res.drawable.ins_car_insurance
    GoalType.BuildSavings -> Res.drawable.piggybank_icon
    GoalType.OtherGoal, null -> Res.drawable.ruppee_circle
}
