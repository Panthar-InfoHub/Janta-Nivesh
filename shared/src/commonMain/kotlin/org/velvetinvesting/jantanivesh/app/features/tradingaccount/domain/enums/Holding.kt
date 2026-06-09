package org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums

import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.joint_holder_icon
import jantanivesh.shared.generated.resources.single_holder_icon
import org.jetbrains.compose.resources.DrawableResource

enum class Holding(
    val heading: String = "",
    val icon: DrawableResource = Res.drawable.single_holder_icon,
    val code: String
) {
    SINGLE(heading = "Single Holding", icon = Res.drawable.single_holder_icon, code = "SI"),
    JOINT(
        heading = "Joint Holding",
        icon = Res.drawable.joint_holder_icon,
        code = "JO"
    )
}