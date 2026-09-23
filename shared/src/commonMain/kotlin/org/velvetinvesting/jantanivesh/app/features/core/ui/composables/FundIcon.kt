package org.velvetinvesting.jantanivesh.app.features.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LogoBackgroundColor
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow

/**
 * The logo of a fund, bundle, bank or FD issuer — the one place any of them is drawn.
 *
 * Every list and detail screen showed the same three-branch `SubcomposeAsyncImage`, each with its
 * own copy of the initials placeholder; changing how a scheme's icon looks meant finding all of
 * them. Now it is this file. A blank [iconUrl] skips the request and draws [FundIconPlaceholder]
 * straight away, which is also what a failed or still-loading image falls back to.
 */
@Composable
fun FundIcon(
    iconUrl: String?,
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = Spacing.dp44,
    cornerRadius: Dp = Spacing.dp12,
    backgroundColor: Color = LogoBackgroundColor,
    textColor: Color = Primary,
    /** How many of [name]'s leading characters the placeholder shows. */
    letterNum: Int = 1,
    /** Overrides the letters taken from [name] — for the "first letter of each word" style. */
    initials: String? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit,
    /** Stands in for the initials where a screen shows something else, such as a feature icon. */
    placeholder: @Composable (() -> Unit)? = null
) {
    val fallback: @Composable () -> Unit = {
        if (placeholder != null) {
            Box(
                modifier = Modifier
                    .size(size)
                    .genericDropShadow(
                        shape = RoundedCornerShape(cornerRadius),
                        radius = 12.dp
                    )
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                placeholder()
            }
        } else {
            FundIconPlaceholder(
                name = name,
                size = size,
                cornerRadius = cornerRadius,
                backgroundColor = backgroundColor,
                textColor = textColor,
                letterNum = letterNum,
                initials = initials
            )
        }
    }

    if (iconUrl.isNullOrBlank()) {
        Box(modifier = modifier) { fallback() }
        return
    }

    SubcomposeAsyncImage(
        model = iconUrl,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .size(size)
            .genericDropShadow(
                shape = RoundedCornerShape(cornerRadius),
                radius = 12.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(White),
        loading = { fallback() },
        error = { fallback() },
        success = { SubcomposeAsyncImageContent() }
    )
}

/** The initials tile [FundIcon] shows while there is no logo to show. */
@Composable
fun FundIconPlaceholder(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = Spacing.dp44,
    cornerRadius: Dp = Spacing.dp12,
    backgroundColor: Color = LogoBackgroundColor,
    textColor: Color = Primary,
    letterNum: Int = 1,
    initials: String? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .genericDropShadow(
                shape = RoundedCornerShape(cornerRadius),
                radius = 12.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = (initials ?: name.take(letterNum)).capitalize(Locale.current),
            style = MaterialTheme.typography.headlineSmall,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FundIconPreview() {
    JantaNiveshTheme {
        Column(
            modifier = Modifier.padding(Spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
        ) {
            FundIcon(
                iconUrl = null,
                name = "HDFC Mutual Fund"
            )
            FundIcon(
                iconUrl = null,
                name = "Axis Bluechip Fund",
                letterNum = 2
            )
            FundIcon(
                iconUrl = "https://example.com/logo.png",
                name = "SBI Small Cap Fund"
            )
        }
    }
}
