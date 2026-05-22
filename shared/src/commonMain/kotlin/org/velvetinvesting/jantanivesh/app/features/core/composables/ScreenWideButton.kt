package org.velvetinvesting.jantanivesh.app.features.core.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.theme.ButtonShadow
import org.velvetinvesting.jantanivesh.app.theme.White

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScreenWideButton(
    buttonText: String,
    onClick: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier.clip(LocalShapes.current.roundedDp12).background(color)
            .clickable(onClick = onClick).dropShadow(
                shape = LocalShapes.current.roundedDp12,
                shadow = Shadow(
                    radius = Spacing.dp16,
                    spread = Spacing.dp12,
                    offset = DpOffset(x = 0.dp, y = Spacing.dp8),
                    color = ButtonShadow
                )
            )
    ) {
        Text(buttonText, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W700), modifier = Modifier.padding(vertical = 18.dp), color = White)
    }
}