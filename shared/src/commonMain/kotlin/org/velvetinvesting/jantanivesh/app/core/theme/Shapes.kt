package org.velvetinvesting.jantanivesh.app.core.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class JantaNiveshShapes(
    val roundedDp12: Shape = RoundedCornerShape(Spacing.dp12),
    val roundedDp16: Shape = RoundedCornerShape(size = Spacing.dp16),
    val circle: Shape = CircleShape,
    val roundedDp24: Shape = RoundedCornerShape(size = Spacing.dp24),
    val menuContainer: Shape = RoundedCornerShape(8.dp)
)

val LocalShapes = staticCompositionLocalOf { JantaNiveshShapes() }