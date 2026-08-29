package org.velvetinvesting.jantanivesh.app.features.profile.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.bell_icon
import jantanivesh.shared.generated.resources.change_pin
import jantanivesh.shared.generated.resources.delete_account
import jantanivesh.shared.generated.resources.forward
import jantanivesh.shared.generated.resources.privacy_policy
import jantanivesh.shared.generated.resources.terms_of_service
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Gray45
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.ProfileTitleColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.profileDividerColor
import org.velvetinvesting.jantanivesh.app.core.theme.redColor
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileSettingEvent
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileSettingUiState

@Composable
fun ProfileSettingScreen(
    state: ProfileSettingUiState,
    modifier: Modifier = Modifier,
    onEvent: (ProfileSettingEvent) -> Unit
) {
    Scaffold(
        containerColor = White
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
            contentPadding = PaddingValues(
                bottom = Spacing.dp16
            )
        ) {
            item {
                BackHeader("Setting", onBack = {onEvent(ProfileSettingEvent.OnProfileBackClicked)}, true)
            }
            item {
                Box(
                    modifier = Modifier.genericDropShadow(RoundedCornerShape(Spacing.dp8)).clip(
                        RoundedCornerShape(
                            Spacing.dp8
                        )
                    ).fillMaxWidth().clip(RoundedCornerShape(Spacing.dp8))
                        .background(color = Color.White)
                ) {
                    Column {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Preferences",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        HorizontalDivider(
                            thickness = 1.dp, color = profileDividerColor.copy(0.2f)
                        )
                        RowItemWithOutIconBackground(
                            icon = Res.drawable.bell_icon,
                            subtitle = null,
                            onCLick = {onEvent(ProfileSettingEvent.OnNotificationClicked)},
                            title = "Notification"
                        )

                    }


                }
            }
            item {
                Box(
                    modifier = Modifier.genericDropShadow(RoundedCornerShape(Spacing.dp8)).clip(
                        RoundedCornerShape(
                            Spacing.dp8
                        )
                    ).fillMaxWidth().clip(RoundedCornerShape(Spacing.dp8))
                        .background(color = Color.White)
                ) {
                    Column {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Security",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        HorizontalDivider(
                            thickness = 1.dp, color = profileDividerColor.copy(0.2f)
                        )
                        RowItemWithOutIconBackground(
                            icon = Res.drawable.change_pin,
                            subtitle = null,
                            onCLick = {onEvent(ProfileSettingEvent.OnChangePinClicked)},
                            title = "Change PIN"
                        )
                        // Biometric login has no settings toggle yet — the prompt is offered on
                        // the lock screen itself.
                    }


                }
            }
            item {
                Box(
                    modifier = Modifier.genericDropShadow(RoundedCornerShape(Spacing.dp8)).clip(
                        RoundedCornerShape(
                            Spacing.dp8
                        )
                    ).fillMaxWidth().clip(RoundedCornerShape(Spacing.dp8))
                        .background(color = Color.White)
                ) {
                    Column {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Data & Privacy",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        HorizontalDivider(
                            thickness = 1.dp, color = profileDividerColor.copy(0.2f)
                        )
                        RowItemWithOutIconBackground(
                            icon = Res.drawable.privacy_policy,
                            subtitle = null,
                            onCLick = {onEvent(ProfileSettingEvent.OnPrivacyPolicyClicked)},
                            title = "Privacy Policy"
                        )
                        HorizontalDivider(
                            thickness = 1.dp, color = profileDividerColor.copy(0.2f)
                        )
                        RowItemWithOutIconBackground(
                            icon = Res.drawable.terms_of_service,
                            subtitle = null,
                            onCLick = {onEvent(ProfileSettingEvent.OnTermsOfServiceClicked)},
                            title = "Terms of Service"
                        )
                        HorizontalDivider(
                            thickness = 1.dp, color = profileDividerColor.copy(0.2f)
                        )
                        RowItemWithOutIconBackground(
                            icon = Res.drawable.delete_account, colorIcon = redColor,
                            subtitle = null,
                            onCLick = {onEvent(ProfileSettingEvent.OnDeleteAccountClicked)},
                            title = "Delete Account", colorTitle = redColor, colorForwardIcon = redColor
                        )
                    }


                }
            }

        }
    }
}



@Composable
fun RowItemWithOutIconBackground(
    icon: DrawableResource,
    title: String,
    subtitle: String? = null,
    onCLick: () -> Unit = {},
    colorIcon: Color=Primary,
    colorTitle: Color= ProfileTitleColor,
    colorForwardIcon: Color= Gray45
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(color = White, shape = RoundedCornerShape(
            Spacing.dp8)).clickable { onCLick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )
        {


                Icon(
                    painter = painterResource(icon),
                    contentDescription = title,
                    tint = colorIcon,
                    modifier = Modifier
                        .size(20.dp)
                )


            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Normal ,
                    color = colorTitle)

            }
            Icon(
                painter = painterResource(Res.drawable.forward),
                contentDescription = "Forward Icon",
                tint = colorForwardIcon
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview3() {
    JantaNiveshTheme {
        ProfileSettingScreen(state = ProfileSettingUiState(), onEvent = {})
    }
}