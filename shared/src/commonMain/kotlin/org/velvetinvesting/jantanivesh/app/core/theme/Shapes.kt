package org.velvetinvesting.jantanivesh.app.core.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape

data class JantaNiveshShapes(
    val roundedDp12: Shape = RoundedCornerShape(Spacing.dp12),
    val roundedDp16: Shape = RoundedCornerShape(size = Spacing.dp16)
)

val LocalShapes = staticCompositionLocalOf { JantaNiveshShapes() }