package org.velvetinvesting.jantanivesh.app.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = TextOnPrimary,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    outline = Border,
    outlineVariant = BorderFocused,
)

@Composable
fun JantaNiveshTheme(
    content: @Composable () -> Unit,
) {
    val typography = rememberLessPayTypography()

    CompositionLocalProvider(
        LocalShapes provides JantaNiveshShapes(),
    )
    {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = typography,
            content = content,
        )
    }
}
