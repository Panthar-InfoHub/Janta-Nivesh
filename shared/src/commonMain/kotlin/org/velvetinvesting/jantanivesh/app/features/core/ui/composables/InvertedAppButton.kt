package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import org.velvetinvesting.jantanivesh.app.core.theme.ButtonShadow
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InvertedAppButton(
    text: String,
    onClick: () -> Unit,
    style: AppButtonStyle = AppButtonDefaults.style(
        containerColor = White,
        contentColor = Primary
    ),
    enabled: Boolean = true,
    loading: Boolean = false,
    modifier: Modifier = Modifier
) {

    OutlinedButton(
        onClick = onClick,
        enabled = enabled && !loading,
        shape = style.shape,
        border = BorderStroke(
            width = Spacing.dp1,
            color = Primary
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = White,
            contentColor = Primary,
            disabledContainerColor = White,
            disabledContentColor = Primary.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .height(style.height)
            .dropShadow(
                shape = LocalShapes.current.roundedDp12,
                shadow = Shadow(
                    radius = Spacing.dp16,
                    spread = Spacing.dp12,
                    offset = DpOffset(
                        x = Spacing.dp0,
                        y = Spacing.dp8
                    ),
                    color = ButtonShadow
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.W700
                )
            )

            AnimatedVisibility(loading) {
                CircularProgressIndicator(
                    color = Primary,
                    strokeWidth = Spacing.dp1,
                    modifier = Modifier.size(style.indicatorSize)
                )
            }
        }
    }
}