package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.ButtonShadow
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.White

data class AppButtonStyle(
    val shape: Shape,
    val containerColor: Color,
    val contentColor: Color,
    val height: Dp,
    val indicatorSize: Dp
)

object AppButtonDefaults {

    @Composable
    fun style(
        shape: Shape = RoundedCornerShape(Spacing.dp12),
        containerColor: Color = Primary,
        contentColor: Color = White,
        height: Dp = Spacing.dp52,
        indicatorSize: Dp = 16.dp
    ): AppButtonStyle {
        return AppButtonStyle(
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
            height = height,
            indicatorSize = indicatorSize
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    style: AppButtonStyle = AppButtonDefaults.style(),
    enabled: Boolean=true,
    loading: Boolean=false,
    trailingIcon: DrawableResource? = null,
    modifier: Modifier = Modifier
) {

    val keyboardManager = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Button(
        onClick = {
            onClick()
            keyboardManager?.hide()
            focusManager.clearFocus()
        },
        enabled = enabled && !loading,
        shapes = ButtonDefaults.shapes(shape = style.shape),
        colors = ButtonDefaults.buttonColors(
            containerColor = style.containerColor,
            contentColor = style.contentColor,
            disabledContainerColor = style.containerColor.copy(alpha = 0.5f),
            disabledContentColor = style.contentColor
        ),
        modifier = modifier
            .height(style.height)
            .dropShadow(
                shape = LocalShapes.current.roundedDp12,
                shadow = Shadow(
                    radius = Spacing.dp16,
                    spread = Spacing.dp12,
                    offset = DpOffset(x = Spacing.dp0, y = Spacing.dp8),
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

/**
 * Shared label for [AppButton] and [InvertedAppButton]. When a [trailingIcon] is supplied the
 * label stays centered within the button and the icon is pinned to the end, as per the designs.
 */
@Composable
internal fun AppButtonContent(
    text: String,
    loading: Boolean,
    indicatorColor: Color,
    indicatorSize: Dp,
    trailingIcon: DrawableResource?
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            Text(
                text,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W700)
            )
            AnimatedVisibility(loading) {
                CircularProgressIndicator(
                    color = indicatorColor,
                    strokeWidth = Spacing.dp1,
                    modifier = Modifier.size(indicatorSize)
                )
            }
        }

        if (trailingIcon != null) {
            Icon(
                painter = painterResource(trailingIcon),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(Spacing.dp16)
            )
        }
    }
}

@Preview(showBackground = true, locale = "te")
@Composable
fun ButtonPreview() {
    JantaNiveshTheme {
        AppButton(
            text = "ButtonPreview",
            onClick = {}, modifier = Modifier.fillMaxWidth(),
            loading = true
        )
    }
}