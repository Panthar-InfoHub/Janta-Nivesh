package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import org.jetbrains.compose.resources.DrawableResource
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
    trailingIcon: DrawableResource? = null,
    modifier: Modifier = Modifier
) {

    OutlinedButton(
        onClick = onClick,
        enabled = enabled && !loading,
        shape = style.shape,
        border = BorderStroke(
            width = Spacing.dp1,
            color = style.contentColor
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = style.containerColor,
            contentColor = style.contentColor,
            disabledContainerColor = style.containerColor,
            disabledContentColor = style.contentColor.copy(alpha = 0.5f)
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
        AppButtonContent(
            text = text,
            loading = loading,
            indicatorColor = style.contentColor,
            indicatorSize = style.indicatorSize,
            trailingIcon = trailingIcon
        )
    }
}