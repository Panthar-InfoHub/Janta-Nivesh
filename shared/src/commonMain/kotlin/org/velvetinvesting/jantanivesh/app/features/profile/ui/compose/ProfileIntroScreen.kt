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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.forward
import jantanivesh.shared.generated.resources.insurance_shield_icon
import jantanivesh.shared.generated.resources.profile_contact_us
import jantanivesh.shared.generated.resources.profile_help
import jantanivesh.shared.generated.resources.profile_kyc_status
import jantanivesh.shared.generated.resources.profile_language
import jantanivesh.shared.generated.resources.profile_setting
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.GoalIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.Gray45
import org.velvetinvesting.jantanivesh.app.core.theme.InsuranceIconBg
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.ProfileGreen
import org.velvetinvesting.jantanivesh.app.core.theme.ProfileSecondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.redColor
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.HomeScreenUiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileEvent

@Composable
fun ProfileIntroScreen(
    state: HomeScreenUiState,
    modifier: Modifier = Modifier,
    onEvent: (ProfileEvent) -> Unit
) {
    Column(
        modifier.fillMaxSize()
            .background(White).padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = Spacing.dp16)
        ) {
            item {
                ProfileTopBar()
            }
            item {
                Box(
                    Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Primary.copy(0.05f), ProfileSecondary.copy(0.05f)
                                )
                            )
                        ),
                ) {
                    Row(Modifier.padding(20.dp)) {

                        Row(
                            Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier.size(64.dp).clip(CircleShape)
                                    .background(color = ProfileSecondary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.userName.firstOrNull()?.toString() ?: "",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = White
                                )
                            }

                            Column {
                                Text(
                                    state.userName,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Black
                                )
                                Text(
                                    state.email,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Normal
                                    ),
                                    color = Gray45
                                )
                            }
                        }


//                            Box(
//                                modifier = Modifier.clip(RoundedCornerShape(50.dp))
//                                    .background(
//                                        color = if (state.kycVerified) ProfileGreen.copy(
//                                            0.1f
//                                        ) else redColor.copy(0.1f)
//                                    ).padding(8.dp)
//                            ) {
//                                Text(
//                                    if (state.kycVerified) "KYC Completed " else "KYC Pending",
//                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
//                                    color = if (state.kycVerified) ProfileGreen else redColor
//                                )
//                            }

                    }

                }
            }

            item {
                Text(
                    "ACCOUNT", modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Gray45,
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.dp12)
                        .genericDropShadow(RoundedCornerShape(Spacing.dp8))
                        .clip(RoundedCornerShape(Spacing.dp8)).background(color = White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth().background(
                                White, shape = RoundedCornerShape(
                                    Spacing.dp8
                                )
                            )
                    ) {
                        RowItem(
                            icon = Res.drawable.profile_language,
                            title = "Secondary Language",
                            onCLick = { onEvent(ProfileEvent.OnSecondaryLanguageClicked) }
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = InsuranceIconBg.copy(0.2f),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        RowItem(
                            icon = Res.drawable.profile_setting,
                            title = "Settings",
                            onCLick = {
                                onEvent(
                                    ProfileEvent.OnSettingsClicked
                                )
                            })

                    }
                }

            }

            item {
                Text(
                    "INVESTMENT", modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Gray45,
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.dp12)
                        .genericDropShadow(RoundedCornerShape(Spacing.dp8))
                        .clip(RoundedCornerShape(Spacing.dp8)).background(color = White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth().background(
                                White, shape = RoundedCornerShape(
                                    Spacing.dp8
                                )
                            )
                    ) {
//                            RowItem(icon = Res.drawable.profile_bank, title = "Bank Accounts", onCLick = {onEvent(
//                                ProfileEvent.OnBankAccountsClicked)})
//                            HorizontalDivider(
//                                thickness = 1.dp,
//                                color = InsuranceIconBg.copy(0.2f),
//                                modifier = Modifier.padding(horizontal = 24.dp)
//                            )
//                            RowItem(icon = Res.drawable.profile_clock, title = "Transction History", onCLick = {onEvent(
//                                ProfileEvent.OnTransactionHistoryClicked)})
//                            HorizontalDivider(
//                                thickness = 1.dp,
//                                color = InsuranceIconBg.copy(0.2f),
//                                modifier = Modifier.padding(horizontal = 24.dp)
//                            )
                        RowItemText(
                            Res.drawable.profile_kyc_status,
                            title = "KYC Status",
                            text = if (state.kycVerified) "Completed" else "Pending",
                            color = if (state.kycVerified) ProfileGreen else redColor,
                            onCLick = {
                                if (state.kycVerified) return@RowItemText
                                onEvent(
                                    ProfileEvent.OnKycStatusClicked
                                )
                            })
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = InsuranceIconBg.copy(0.2f),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        RowItemText(
                            Res.drawable.insurance_shield_icon,
                            title = "Trading Account Status",
                            text = if (state.tradingAccountVerified) "Completed" else "Pending",
                            color = if (state.tradingAccountVerified) ProfileGreen else redColor,
                            onCLick = {
                                if (state.tradingAccountVerified) return@RowItemText
                                onEvent(
                                    ProfileEvent.OnTradingAccountStatusClicked
                                )
                            }
                        )

                    }
                }

            }

            item {
                Text(
                    "SUPPORT", modifier = Modifier,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Gray45,
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.dp12)
                        .genericDropShadow(RoundedCornerShape(Spacing.dp8))
                        .clip(RoundedCornerShape(Spacing.dp8)).background(color = White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth().background(
                                White, shape = RoundedCornerShape(
                                    Spacing.dp8
                                )
                            )
                    ) {
                        RowItem(
                            icon = Res.drawable.profile_help,
                            title = "Help & FAQs",
                            onCLick = {
                                onEvent(
                                    ProfileEvent.OnHelpFaqClicked
                                )
                            })
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = InsuranceIconBg.copy(0.2f),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        RowItem(
                            icon = Res.drawable.profile_contact_us,
                            title = "Contact Us",
                            onCLick = {
                                onEvent(
                                    ProfileEvent.OnContactUsClicked
                                )
                            })
                    }
                }

            }
            item {
                AppButton(
                    "Log Out",
                    onClick = { onEvent(ProfileEvent.OnLogoutClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    JantaNiveshTheme {
        ProfileIntroScreen(
            state = HomeScreenUiState(
                userName = "Sharad Pratap Singh",
                email = "sharadsengar2003@gmail.com"
            ), onEvent = {})
    }
}

@Composable
fun ProfileTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {


        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF273E71)
        )
    }
}

@Composable
fun RowItem(
    icon: DrawableResource,
    title: String,
    subtitle: String? = null,
    onCLick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(
            color = White, shape = RoundedCornerShape(
                Spacing.dp8
            )
        ).clickable { onCLick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        {

            Box(
                modifier = Modifier.size(40.dp)
                    .clip(CircleShape)
                    .background(
                        color = GoalIconBg,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = title,
                    tint = Primary,
                    modifier = Modifier
                        .size(14.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

            }
            Icon(
                painter = painterResource(Res.drawable.forward),
                contentDescription = "Forward Icon",
                tint = Gray45
            )
        }
    }
}


@Composable
fun RowItemText(
    icon: DrawableResource,
    title: String,
    text: String,
    subtitle: String? = null,
    onCLick: () -> Unit = {},
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(
            color = White, shape = RoundedCornerShape(
                Spacing.dp8
            )
        ).clickable { onCLick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        {

            Box(
                modifier = Modifier.size(40.dp)
                    .clip(CircleShape)
                    .background(
                        color = GoalIconBg,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = title,
                    tint = Primary,
                    modifier = Modifier
                        .size(14.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

            }
            Text(text, color = color)
        }
    }
}
