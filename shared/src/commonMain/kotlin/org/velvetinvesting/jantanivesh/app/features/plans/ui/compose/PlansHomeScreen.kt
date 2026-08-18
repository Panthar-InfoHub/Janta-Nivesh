package org.velvetinvesting.jantanivesh.app.features.plans.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.InvertedAppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow

/**
 * Temporary entry point for the plans flow — a plain fork between the two journeys while the
 * real home screen is being built.
 */
@Composable
fun PlansHomeScreen(
    onPurchaseClick: () -> Unit,
    onRedeemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = Spacing.dp24),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What would you like to do?",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Black,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Start a new SIP, or redeem from an existing holding.",
            style = MaterialTheme.typography.bodyLarge,
            color = GreyText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.dp16)
        )

        AppButton(
            text = "Purchase",
            onClick = onPurchaseClick,
            modifier = Modifier
                .fillMaxWidth()
                .genericDropShadow()
        )
        InvertedAppButton(
            text = "Redeem",
            onClick = onRedeemClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlansHomeScreenPreview() {
    JantaNiveshTheme {
        PlansHomeScreen(
            onPurchaseClick = {},
            onRedeemClick = {}
        )
    }
}
