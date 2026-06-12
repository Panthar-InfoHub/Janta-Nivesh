package org.velvetinvesting.jantanivesh.app.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily

@Composable
fun String.withInterRupee(): AnnotatedString {
    val inter = InterFontFamily

    val formattedText = replace("₹-", "-₹")

    return buildAnnotatedString {
        formattedText.forEach { char ->
            if (char == '₹') {
                withStyle(
                    SpanStyle(
                        fontFamily = inter
                    )
                ) {
                    append(char)
                }
            } else {
                append(char)
            }
        }
    }
}