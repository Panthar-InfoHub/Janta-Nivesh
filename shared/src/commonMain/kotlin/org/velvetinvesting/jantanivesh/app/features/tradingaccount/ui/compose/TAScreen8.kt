package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.checkbadge_icon
import jantanivesh.shared.generated.resources.guardian_icon
import jantanivesh.shared.generated.resources.guardian_pan_number
import jantanivesh.shared.generated.resources.securetick_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Holding
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.Data
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountEvent
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountUiState

@Preview(showBackground = true, locale = "hi")
@Composable
fun TradingAccountGuardianPanPreview() {
    JantaNiveshTheme {
        TradingAccountGuardianPanScreen(
            pv = PaddingValues(16.dp),
            uiState = TradingAccountUiState(
                formState = UiState.Success(TradingAccountFormDomain(data = Data())),
                holderNature = Holding.SINGLE
            ),
            handleEvent = {},
            onClick = {},
            onBackClick = {}
        )
    }
}

@Composable
fun TradingAccountGuardianPanScreen(
    pv: PaddingValues,
    uiState: TradingAccountUiState,
    handleEvent: (TradingAccountEvent) -> Unit,
    onClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LocalTopAppBarWithBackButtonAndStepCount(
            title = "Trading",
            stepCount = 2,
            totalSteps = uiState.totalSteps,
            onBack = onBackClick,
            modifier = Modifier.padding(pv)
        )

        UiStateContainer(
            uiState = uiState.formState,
            onRetry = { handleEvent(TradingAccountEvent.GetUserData) }
        ) { baseResponse ->
            val data = baseResponse.data

            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.dp16),
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
                ) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
                        ) {
                            Text(
                                "KYC & PAN Details",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "To comply with regulatory requirements, we need a few more details to set up your account securely.",
                                style = MaterialTheme.typography.labelSmall,
                                color = GreyText
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .genericDropShadow(RoundedCornerShape(Spacing.dp12))
                                .clip(RoundedCornerShape(Spacing.dp12))
                                .border(
                                    width = Spacing.dp1,
                                    color = Primary,
                                    shape = RoundedCornerShape(Spacing.dp12)
                                )
                                .background(Color(0xffE5F7FD))
                                .padding(
                                    end = Spacing.dp16,
                                    top = Spacing.dp16,
                                    bottom = Spacing.dp16
                                ),
                        ) {
                            VerticalDivider(
                                modifier = Modifier.fillMaxHeight(),
                                thickness = Spacing.dp16,
                                color = Primary
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(Spacing.dp24),
                                modifier = Modifier.padding(
                                    vertical = Spacing.dp16,
                                    horizontal = Spacing.dp8
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.guardian_icon),
                                        contentDescription = null,
                                        tint = Primary
                                    )
                                    Text(
                                        "Guardian PAN Required",
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Primary
                                    )
                                }
                                Text(
                                    "Since you are opening a minor account, the PAN details of the registered guardian must be verified to proceed.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GreyText
                                )
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
                                ) {
                                    TitledAppTextField(
                                        title = "Guardian PAN Number/ " + "(" + stringResource(Res.string.guardian_pan_number) + ")",
                                        value = data.primary_holder_pan,
                                        placeholder = "e.g. ABCD1234F",
                                        mandatory = true,
                                        modifier = Modifier.padding(top = Spacing.dp16).weight(1f),
                                        onValueChange = {
                                            handleEvent(
                                                TradingAccountEvent.OnPanChange(
                                                    it.uppercase()
                                                )
                                            )
                                        },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    )
                                    if (!uiState.panVerified || uiState.verifiedPanNumber != data.primary_holder_pan) {

                                        Button(
                                            onClick = {
                                                handleEvent(
                                                    TradingAccountEvent.VerifyPan(
                                                        data.guardian_pan
                                                    )
                                                )
                                            },
                                            shape = RoundedCornerShape(Spacing.dp8),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Primary,
                                                contentColor = White
                                            ),
                                            modifier = Modifier.height(54.dp)
                                                .align(Alignment.Bottom)
                                                .padding(bottom = Spacing.dp2)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
                                            ) {
                                                Icon(
                                                    painter = painterResource(Res.drawable.checkbadge_icon),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(Spacing.dp15),
                                                    tint = White
                                                )
                                                Text(
                                                    "Verify PAN",
                                                    style = MaterialTheme.typography.titleSmall.copy(
                                                        fontWeight = FontWeight.Normal
                                                    )
                                                )
                                            }
                                        }
                                    } else {
                                        Text(
                                            text = "Verified",
                                            color = appGreen,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(Spacing.dp12))
                                .background(Color(0xffD3E4FE))
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.securetick_icon),
                                    contentDescription = "notice icon",
                                    tint = Primary
                                )
                                Text(
                                    "Your PAN details are securely fetched from the central registry (CERSAI) for KYC compliance as per IRDAI guidelines. We ensure your data is encrypted and protected.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GreyText
                                )

                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }
                }

                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Next",
                    enabled = uiState.panVerified && uiState.verifiedPanNumber == data.guardian_pan
                )
            }
        }
    }
}