package org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import org.velvetinvesting.jantanivesh.app.core.theme.ShadowColor

fun Modifier.genericDropShadow(
    shape: Shape= RectangleShape
): Modifier = this.then(
    Modifier.dropShadow(
        shadow = Shadow(
            radius = 16.dp,
            color = ShadowColor
        ),
        shape = shape
    )
)
