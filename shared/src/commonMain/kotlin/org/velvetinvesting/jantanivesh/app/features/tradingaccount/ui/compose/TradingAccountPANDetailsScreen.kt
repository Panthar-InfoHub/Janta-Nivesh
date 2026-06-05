package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.info_icon
import jantanivesh.shared.generated.resources.lock_icon
import jantanivesh.shared.generated.resources.lock_outlined_icon
import jantanivesh.shared.generated.resources.receipt_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
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
fun TradingAccountPANDetailsPreview() {
    JantaNiveshTheme {
        TradingAccountPANDetailsScreen(
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TradingAccountPANDetailsScreen(
    pv: PaddingValues,
    uiState: TradingAccountUiState,
    handleEvent: (TradingAccountEvent) -> Unit,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LocalTopAppBarWithBackButtonAndStepCount(
            title = "Trading",
            stepCount = 1,
            totalSteps = 5,
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
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                ) {
                    item {
                        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                            Text(
                                "KYC & PAN Details",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Verify your identity documents to continue.",
                                style = MaterialTheme.typography.labelSmall,
                                color = GreyText
                            )
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                .clip(RoundedCornerShape(Spacing.dp12))
                                .background(Color.White)
                                .padding(Spacing.dp16),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
                                ) {
                                    TitledAppTextField(
                                        title = "Pan Number/ ",
                                        value = data.primary_holder_pan,
                                        placeholder = "ABCD1234F",
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
                                        AppButton(
                                            text = "Verify",
                                            onClick = {
                                                handleEvent(
                                                    TradingAccountEvent.VerifyPan(
                                                        data.primary_holder_pan
                                                    )
                                                )
                                            },
                                            modifier = Modifier.align(Alignment.Bottom)
                                        )
                                    } else {
                                        Text(
                                            text = "Verified",
                                            color = appGreen,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp4)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.lock_outlined_icon),
                                        contentDescription = null,
                                        tint = GreyText,
                                        modifier = Modifier.size(Spacing.dp12)
                                    )
                                    Text(
                                        "Your details are secure with us.",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontSize = 12.sp,
                                        color = GreyText
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
                            modifier = Modifier
                                .genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                .clip(RoundedCornerShape(Spacing.dp8))
                                .background(Color(0xffEFEDF3))
                                .padding(Spacing.dp20),
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.info_icon),
                                contentDescription = null,
                                tint = Color(0xff00658D),
                                modifier = Modifier.size(Spacing.dp20)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
                                Text(
                                    "Regulatory Disclosure",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Primary
                                )
                                Text(
                                    "Please ensure your PAN matches your KYC records. If the PAN and KYC identifiers are inconsistent, the Unique Client Code (UCC) registration will be rejected by the exchange.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GreyText,
                                    modifier = Modifier.padding(bottom = Spacing.dp16)
                                )
                                Text(
                                    "कृपया सुनिश्चित करें कि आपका PAN आपके KYC रिकॉर्ड से मेल खाता हो। यदि PAN और KYC पहचानकर्ता मेल नहीं खाते हैं, तो एक्सचेंज द्वारा Unique Client Code (UCC) पंजीकरण अस्वीकृत कर दिया जाएगा।",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GreyText,
                                    modifier = Modifier.padding(bottom = Spacing.dp16)
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
                    enabled = uiState.panVerified && uiState.verifiedPanNumber == data.primary_holder_pan
                )
            }
        }
    }
}