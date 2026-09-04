package org.velvetinvesting.jantanivesh.app.features.auth.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.biometric
import jantanivesh.shared.generated.resources.biometric_disable_note
import jantanivesh.shared.generated.resources.ic_face_id
import jantanivesh.shared.generated.resources.info_icon
import jantanivesh.shared.generated.resources.tick_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Gray444
import org.velvetinvesting.jantanivesh.app.core.theme.Gray65
import org.velvetinvesting.jantanivesh.app.core.theme.IconSize
import org.velvetinvesting.jantanivesh.app.core.theme.InsuranceBoxBackground
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LightBlueBorder
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.PrimaryContainer
import org.velvetinvesting.jantanivesh.app.core.theme.ProfileLightBlue
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.profileDividerColor
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.BiometricSettingsEvent
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.BiometricSettingsUiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ShadowCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ToggleSwitch
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.dashedBorder

@Composable
fun BiometricSettingsScreen(
    state: BiometricSettingsUiState,
    onEvent: (BiometricSettingsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.dp16),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
        contentPadding = PaddingValues(bottom = Spacing.dp24)
    ) {

        item {
            BackHeader(
                title = "Biometric Login",
                onBack = { onEvent(BiometricSettingsEvent.OnBackClicked) }
            )
        }

        item {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(LocalShapes.current.circle)
                    .background(PrimaryContainer),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    painter = painterResource(Res.drawable.biometric),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(48.dp)
                )

                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(Spacing.dp8)
                        .dashedBorder(
                            Primary.copy(0.2f),
                            cornerRadius = 55.dp
                        )
                )
            }
        }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
            ) {
                Text(
                    text = "Biometric Login",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Primary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Faster, more secure access to your account using your device's " +
                            "biometric sensors.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray65,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = Spacing.dp28)
                )
            }
        }

        item {
            ShadowCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(Spacing.dp16),
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(40.dp)
                                .clip(LocalShapes.current.roundedDp12)
                                .background(InsuranceBoxBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(IconSize.dp20),
                                painter = painterResource(Res.drawable.ic_face_id),
                                contentDescription = null
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp2)
                        ) {
                            Text(
                                text = "Enable Biometric Login",
                                style = MaterialTheme.typography.titleMedium,
                                color = Gray444
                            )
                            Text(
                                text = "Use Face ID or Touch ID",
                                style = MaterialTheme.typography.titleSmall,
                                color = Gray65
                            )
                        }
                        ToggleSwitch(
                            checked = state.enabled,
                            onCheckedChange = {
                                onEvent(BiometricSettingsEvent.OnEnabledChanged(it))
                            }
                        )
                    }

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = profileDividerColor.copy(alpha = 0.2f)
                    )

                    BenefitRow(
                        label = "Enhanced Security",
                        detail = "Your biometric data is encrypted and never leaves your device."
                    )
                    BenefitRow(
                        label = "Instant Access",
                        detail = "Skip the PIN typing and log in instantly."
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalShapes.current.roundedDp12)
                    .border(
                        width = 1.dp,
                        color = LightBlueBorder,
                        shape = LocalShapes.current.roundedDp12
                    )
                    .background(ProfileLightBlue)
                    .padding(Spacing.dp16),
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.info_icon),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp20)
                )
                Text(
                    text = "If you disable biometric login, you will need to enter your PIN " +
                            "manually each time you open the app.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray65
                )
            }
        }
    }

}

@Composable
private fun BenefitRow(
    label: String,
    detail: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
    ) {
        Box(
            modifier = Modifier
                .padding(top = Spacing.dp4)
                .size(IconSize.dp12)
                .clip(LocalShapes.current.circle)
                .background(Secondary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    Res.drawable.tick_icon,
                ),
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(IconSize.dp6)
            )
        }
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("$label: ")
                }
                append(detail)
            },
            style = MaterialTheme.typography.labelSmall,
            color = Gray444
        )
    }
}

@Preview(locale = "hi", showBackground = true)
@Composable
fun BiometricSettingsScreenPreview() {
    JantaNiveshTheme {
        BiometricSettingsScreen(
            state = BiometricSettingsUiState(enabled = true),
            onEvent = {}
        )
    }
}
