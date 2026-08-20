    package org.velvetinvesting.jantanivesh.app.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.inter_bold
import jantanivesh.shared.generated.resources.inter_medium
import jantanivesh.shared.generated.resources.inter_normal
import jantanivesh.shared.generated.resources.inter_semibold
import org.jetbrains.compose.resources.Font

val InterFontFamily
    @Composable get() = FontFamily(
        Font(Res.font.inter_normal, FontWeight.Normal),
        Font(Res.font.inter_medium, FontWeight.Medium),
        Font(Res.font.inter_semibold, FontWeight.SemiBold),
        Font(Res.font.inter_bold, FontWeight.Bold),
    )

@Composable
fun rememberLessPayTypography(): Typography {
    val fontFamily = InterFontFamily
    return remember(fontFamily) {
        Typography(
            displayLarge = TextStyle(
                fontFamily = fontFamily,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp
            ),

            displayMedium = TextStyle(
                fontFamily = fontFamily,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 44.sp
            ),
            //
            headlineLarge = TextStyle(
                fontFamily = fontFamily,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 38.sp
            ),
            headlineMedium = TextStyle(
                fontFamily = fontFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp
            ),
            headlineSmall = TextStyle(
                fontFamily = fontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp
            ),
            titleLarge = TextStyle(
                fontFamily = fontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 24.sp
            ),
            titleMedium = TextStyle(
                fontFamily = fontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            ),
            titleSmall = TextStyle(
                fontFamily = fontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 14.sp
            ),
            bodyLarge = TextStyle(
                fontFamily = fontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 22.sp
            ),
            bodyMedium = TextStyle(
                fontFamily = fontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp
            ),
            bodySmall = TextStyle(
                fontFamily = fontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 16.sp
            ),
            //
            labelLarge = TextStyle(
                fontFamily = fontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp
            ),
            labelMedium = TextStyle(
                fontFamily = fontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 18.sp
            ),
            //
            labelSmall = TextStyle(
                fontFamily = fontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 16.sp
            ),
        )
    }
}

val largeTextStyle @Composable get() = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 40.sp
)
val titlesStyle @Composable get() = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp
)
val subHeadingMedium @Composable get() = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp
)
val subHeading @Composable get() = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp
)
val tinyLabel @Composable get() = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    lineHeight = 14.sp,
    color = Color(0xff94A3B8)
)

val buttonTextStyle @Composable get() = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
)