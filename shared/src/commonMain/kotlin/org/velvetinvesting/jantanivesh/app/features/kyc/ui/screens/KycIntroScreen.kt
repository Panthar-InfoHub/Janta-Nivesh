package org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.kyc_image
import jantanivesh.shared.generated.resources.lock_icon
import jantanivesh.shared.generated.resources.receipt_icon
import jantanivesh.shared.generated.resources.rupee_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.GrayBackGround
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCScreenEvent
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCScreenUiState

@Composable
fun KycIntroScreen(
    state: KYCScreenUiState,
    onEvent: (KYCScreenEvent) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        bottomBar = {
            AppButton(
                text = "COMPLETE YOUR KYC  →",
                onClick = { onEvent(KYCScreenEvent.OnStartKycClicked) },
                loading = state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.dp24),
            )
        },
        containerColor = White
    ) { pv ->
        Column(
            modifier = Modifier.fillMaxSize().padding(pv)
        ){
            LocalBackHeader(
                title = "KYC",
                onBack = onBack,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.dp24)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Image(
                        painter = painterResource(Res.drawable.kyc_image),
                        contentDescription = null,
                        modifier = Modifier.size(Spacing.dp235),
                        contentScale = ContentScale.FillWidth
                    )
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp12),
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Complete your KYC",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Just a few steps to unlock your investment journey with Nivesh Sansar.",
                            style = MaterialTheme.typography.labelSmall,
                            color = GreyText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
                    ) {
                        ZeroPriceCard(
                            text = "₹ 0 Account Opening Fees",
                            subText = "Start investing without any initial costs.",
                            icon = Res.drawable.rupee_icon,
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                        ZeroPriceCard(
                            text = "₹ 0 Maintenance Charges",
                            subText = "No hidden fees, keep what you earn.",
                            icon = Res.drawable.receipt_icon,
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                    }
                }
                item {
                    EncryptedDataCard()
                }
            }
        }
    }
}

@Composable
fun ZeroPriceCard(text: String, subText: String, icon: DrawableResource, modifier: Modifier) {
    Row(
        modifier = modifier
            .genericDropShadow(RoundedCornerShape(Spacing.dp12))
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(Color.White)
            .padding(Spacing.dp22),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "Rupee Symbol",
            tint = Primary,
            modifier = Modifier
                .size(Spacing.dp48)
                .background(
                    color = GrayBackGround,
                    shape = CircleShape
                )
                .padding(Spacing.dp16)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = subText,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}
@Composable
fun EncryptedDataCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp8))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(top = Spacing.dp16, start = Spacing.dp16, end = Spacing.dp16, bottom = Spacing.dp24),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        Icon(
            painter = painterResource(Res.drawable.lock_icon),
            contentDescription = "Security Lock",
            tint = GreyText,
            modifier = Modifier
                .size(Spacing.dp24)
                .padding(top = Spacing.dp1_2)
        )

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)){
            Text(
                text = "Your data is encrypted and secure",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = "We use bank-level encryption to ensure your personal information remains completely confidential.",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
private fun LocalBackHeader(onBack: () -> Unit, title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {
        AppBackButton(onClick = onBack)
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(heightDp = 1000)
@Composable
fun KycIntroScreenPreview() {
    JantaNiveshTheme{
        KycIntroScreen(
            state = KYCScreenUiState(),
            onEvent = {},
            onBack = {}
        )
    }
}