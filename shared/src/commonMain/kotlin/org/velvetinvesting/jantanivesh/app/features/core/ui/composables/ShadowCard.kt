package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow


@Composable
fun ShadowCard(
    modifier: Modifier = Modifier,
    shape: Shape = LocalShapes.current.roundedDp16,
    backgroundColor: Color = Color.White,
    onClick: () -> Unit = {},
    clickable: Boolean = false,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .genericDropShadow(shape)
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (clickable) Modifier.clickable(onClick = onClick)
                else Modifier
            ),
        contentAlignment = contentAlignment,
        content = content
    )
}
