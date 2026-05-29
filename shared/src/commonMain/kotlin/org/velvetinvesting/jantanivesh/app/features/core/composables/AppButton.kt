package org.velvetinvesting.jantanivesh.app.features.core.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
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
    val height: Dp
)

object AppButtonDefaults {

    @Composable
    fun style(
        shape: Shape = RoundedCornerShape(Spacing.dp12),
        containerColor: Color = Primary,
        contentColor: Color = White,
        height: Dp = Spacing.dp52
    ): AppButtonStyle {
        return AppButtonStyle(
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
            height = height
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
    modifier: Modifier = Modifier
) {

    Button(
        onClick = onClick,
        enabled=enabled && !loading,
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
        Text(text, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W700))

    }
}

@Preview(showBackground = true, locale = "te")
@Composable
fun ButtonPreview() {
    JantaNiveshTheme {
        AppButton(
            text = "ButtonPreview",
            onClick = {}, modifier = Modifier.fillMaxWidth()
        )
    }
}