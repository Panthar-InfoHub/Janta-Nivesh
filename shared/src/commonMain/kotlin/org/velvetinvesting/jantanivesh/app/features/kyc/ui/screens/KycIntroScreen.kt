package org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.plain_credit_card_rafiki_1
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCScreenEvent
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCScreenUiState

@Preview
@Composable
fun KycIntroScreenPreview() {
    KycIntroScreen(
        state = KYCScreenUiState(),
        onEvent = {},
        onBack = {}
    )
}

@Composable
fun KycIntroScreen(
    state: KYCScreenUiState,
    onEvent: (KYCScreenEvent) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarWithBackButtonAndStepCount(
                stepCount = 1,
                totalSteps = 5,
                onBack = onBack,
                modifier = Modifier.padding(horizontal = Spacing.dp24)
            )
        },
        bottomBar = {
            AppButton(
                text = "Complete your KYC",
                onClick = { onEvent(KYCScreenEvent.OnStartKycClicked) },
                loading = state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.dp24)
            )
        }
    ) { pv ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
                .padding(horizontal = Spacing.dp24),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            item {
                Image(
                    painter = painterResource(Res.drawable.plain_credit_card_rafiki_1),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp12),
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
                ) {
                    Text(
                        text = "Complete your KYC in Under few minutes to get started",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Experience more for no cost",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                ) {
                    ZeroPriceCard(
                        text = "Account Maintenance Charges",
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    ZeroPriceCard(
                        text = "Account Opening Fees",
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
            }
        }
    }
}

@Composable
fun ZeroPriceCard(text: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(Spacing.dp16)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp64)
                    .clip(RoundedCornerShape(Spacing.dp15))
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "₹0",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Primary
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
