package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.ic_leaf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.ProfileTitleColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton

/**
 * The empty state behind any list the user has nothing in yet.
 *
 * Every word is the caller's: the same layout stands in for mutual funds, fixed deposits, orders
 * and mandates, and only that screen knows what the user is missing or where the button should
 * send them. One action only — a screen that lists funds offers funds, and a screen that lists
 * deposits offers deposits, so the two never needed to be offered side by side.
 */
@Composable
fun EmptyFundScreen(
    title: String,
    subtitle: String,
    buttonText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: DrawableResource = Res.drawable.ic_leaf
) {
    Box(
        modifier = modifier.fillMaxSize().background(White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.dp24),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmptyStateBadge(icon = icon)

            Spacer(Modifier.height(Spacing.dp32))

            Text(
                text = title,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    lineHeight = 32.sp
                ),
                color = ProfileTitleColor,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(Spacing.dp12))

            Text(
                text = subtitle,
                style = TextStyle(
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                ),
                color = GreyText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Spacing.dp8)
            )

            Spacer(Modifier.height(Spacing.dp40))

            AppButton(
                text = buttonText,
                onClick = onActionClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * The icon on a white disc, lifted off the page by a soft halo rather than a shadow — the disc is
 * the same white as the screen, so the glow is the only thing that separates the two.
 */
@Composable
private fun EmptyStateBadge(icon: DrawableResource) {

        Box(
            modifier = Modifier
                .size(124.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                )
                .clip(CircleShape)
                .background(White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(Spacing.dp48),
                tint = Primary
            )
        }

}

@Preview
@Composable
private fun EmptyFundPreview() {
    JantaNiveshTheme {
        EmptyFundScreen(
            title = "Your investment journey starts here",
            subtitle = "You haven't invested in any funds yet. Start your journey with Janta Nivesh and grow your wealth with ease.",
            buttonText = "Explore Mutual Funds",
            onActionClick = {}
        )
    }
}
