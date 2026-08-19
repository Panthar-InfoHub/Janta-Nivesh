package org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.lock_outlined_icon
import jantanivesh.shared.generated.resources.securetick_icon
import jantanivesh.shared.generated.resources.tick_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedTenureChipColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.grayColor
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.VerifyWithDigilockerEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.VerifyWithDigilockerUiState

@Composable
fun DigiLockerSplashScreen(
    state: VerifyWithDigilockerUiState,
    handleEvent: (VerifyWithDigilockerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal=Spacing.dp20)
            .imePadding()

    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp32),
            contentPadding = PaddingValues(top = Spacing.dp24)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Verify with DigiLocker",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Black,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Complete your identity verification securely using your government-issued documents.",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        color = Gray444,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                DigilockerCard()
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
                    TickBox("Instant verification using Aadhaar")
                    TickBox("Secure government-backed authentication")
                    TickBox("No physical documents required")
                    TickBox("Compliant with SEBI regulations")
                }
            }

            item {
                SecureAndSafeCard()
            }
        }

        AppButton(
            text = "Proceed to Digilocker",
            onClick = { handleEvent(VerifyWithDigilockerEvent.OnProceedClick) },
            loading = state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.dp24)
        )
    }
}

@Composable
private fun DigilockerCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(Spacing.dp24))
            .background(White)
            .padding(Spacing.dp24),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp32)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp48)
                    .clip(RoundedCornerShape(Spacing.dp12))
                    .background(SelectedTenureChipColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.lock_outlined_icon),
                    contentDescription = "Lock Icon",
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp24)
                )
            }
            Text(
                text = "DigiLocker",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Black
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
            Text(
                text = "Your digital document wallet",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Black
            )
            Text(
                text = "by Government of India",
                style = MaterialTheme.typography.labelMedium,
                color = Gray444
            )
        }
    }
}

@Composable
private fun TickBox(title: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Spacing.dp24)
                .clip(CircleShape)
                .background(SelectedTenureChipColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.tick_icon),
                contentDescription = "tick Icon",
                tint = Primary,
                modifier = Modifier.size(Spacing.dp12)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Gray444
        )
    }
}

@Composable
private fun SecureAndSafeCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .border(Spacing.dp1, FilterChipUnselected, RoundedCornerShape(Spacing.dp16))
            .background(GoalIconBg)
            .padding(Spacing.dp20),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.securetick_icon),
                contentDescription = "secure tick Icon",
                tint = Primary,
                modifier = Modifier.size(Spacing.dp16)
            )
            Text(
                text = "Secure & Safe",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Primary
            )
        }
        Text(
            text = "We use bank-grade encryption to protect your data. Your documents are never stored on our servers.",
            style = MaterialTheme.typography.labelMedium,
            color = grayColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DigiLockerSplashScreenPreview() {
    JantaNiveshTheme {
        DigiLockerSplashScreen(
            state = VerifyWithDigilockerUiState(),
            handleEvent = {}
        )
    }
}