package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.building_icon
import jantanivesh.shared.generated.resources.monument_icon
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.utils.tradingaccount.FatcaOccupationType
import org.sharad.velvetinvestment.utils.tradingaccount.SourceOfWealth
import org.sharad.velvetinvestment.utils.tradingaccount.YesNo
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.YesNoRadioGroup
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.ClientType
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.DefaultDp
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Holding
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.Data
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountEvent
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountUiState

@Preview(showBackground = true, heightDp = 1500)
@Composable
fun TradingAccountClientInfoPreview() {
    JantaNiveshTheme {
        TradingAccountClientInfoScreen(
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
fun TradingAccountClientInfoScreen(
    pv: PaddingValues,
    uiState: TradingAccountUiState,
    handleEvent: (TradingAccountEvent) -> Unit,
    onClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LocalTopAppBarWithBackButtonAndStepCount(
            title = "Trading",
            stepCount = 3,
            totalSteps = 5,
            onBack = onBackClick,
            modifier = Modifier.padding(pv)
        )

        UiStateContainer(
            uiState = uiState.formState,
            onRetry = { handleEvent(TradingAccountEvent.GetUserData) },
            modifier = Modifier.fillMaxSize()
        ) { uiData ->
            val data = uiData.data

            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(horizontal = Spacing.dp16),
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                        ) {
                            DropDownSelector(
                                title = "Client Type/ (क्लाइंट प्रकार)",
                                value = ClientType.getDisplayName(data.client_type),
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnClientTypeChangeUi(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Select Client Type",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                list = ClientType.entries,
                                textConvertor = { it.displayName }
                            )

                            YesNoRadioGroup(
                                title = "PMS Service/ (पीएमएस सेवा)",
                                selectedCode = data.pms,
                                onValueChange = { handleEvent(TradingAccountEvent.OnPmsChange(it)) },
                                modifier = Modifier.fillMaxWidth(),
                                mandatory = true
                            )
                            DropDownSelector(
                                title = "Default Depository Participant (DP)/ ",
                                value = DefaultDp.getDisplayName(data.default_dp),
                                onValueChange = {
                                    handleEvent(
                                        TradingAccountEvent.OnDefaultDpChangeUi(
                                            it
                                        )
                                    )
                                },
                                placeholder = "Select Default DP",
                                mandatory = true,
                                list = DefaultDp.entries,
                                textConvertor = { it.displayName }
                            )
                            Column(
                                modifier = Modifier.genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                    .clip(RoundedCornerShape(Spacing.dp16))
                                    .border(width = Spacing.dp1, color = BoxBorder)
                                    .background(Color.White)
                                    .padding(Spacing.dp24),
                                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.monument_icon), // Replace with actual CDSL icon if available
                                        contentDescription = null,
                                        modifier = Modifier.size(Spacing.dp20),
                                        tint = Primary
                                    )
                                    Text(
                                        "CDSL Details/ (सीडीएसएल विवरण)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Primary
                                    )
                                }

                                TitledAppTextField(
                                    title = "CDSL DP ID/ (सीडीएसएल डीपी आईडी)",
                                    value = data.cdsl_dpid,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnCdslDpidChange(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "Enter DP ID",
                                    keyboardType = KeyboardType.Number
                                )

                                TitledAppTextField(
                                    title = "CDSL Client ID/ (सीडीएसएल क्लाइंट आईडी)",
                                    value = data.cdslcltid,
                                    onValueChange = {
                                        handleEvent(
                                            TradingAccountEvent.OnCdslCltidChange(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = "Enter Client ID",
                                    keyboardType = KeyboardType.Number
                                )
                            }

                            // NSDL Block
                            TitledAppTextField(
                                title = "CMBP ID/ (सीएमबीपी आईडी)",
                                value = data.cmbp_id,
                                onValueChange = { handleEvent(TradingAccountEvent.OnCmbpIdChange(it)) },
                                placeholder = "Enter CMBP ID (Clearing Member Business Partner)",
                                mandatory = true,
                                keyboardType = KeyboardType.Text
                            )

                            Column(
                                modifier = Modifier.genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                    .clip(RoundedCornerShape(Spacing.dp16))
                                    .border(width = Spacing.dp1, color = BoxBorder)
                                    .background(Color.White)
                                    .padding(Spacing.dp24),
                                verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.building_icon), // Replace with actual NSDL icon if available
                                        contentDescription = null,
                                        modifier = Modifier.size(Spacing.dp20),
                                        tint = Primary
                                    )
                                    Text(
                                        "NSDL Details/ (एनएसडीएल विवरण)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Primary
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
                                ) {
                                    TitledAppTextField(
                                        title = "NSDL DP ID/ (एनएसडीएल डीपी आईडी)",
                                        value = data.nsdldpid,
                                        onValueChange = {
                                            handleEvent(
                                                TradingAccountEvent.OnNsdlDpidChange(
                                                    it
                                                )
                                            )
                                        },
                                        placeholder = "Enter NSDL DP ID",
                                        mandatory = true,
                                        modifier = Modifier.weight(1f),
                                        keyboardType = KeyboardType.Text
                                    )

                                    TitledAppTextField(
                                        title = "NSDL Client ID/ (एनएसडीएल क्लाइंट आईडी)",
                                        value = data.nsdlcltid,
                                        onValueChange = {
                                            handleEvent(
                                                TradingAccountEvent.OnNsdlCltidChange(
                                                    it
                                                )
                                            )
                                        },
                                        placeholder = "Enter NSDL Client ID",
                                        mandatory = true,
                                        modifier = Modifier.weight(1f),
                                        keyboardType = KeyboardType.Number
                                    )
                                }
                            }

                        }
                    }
                    item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }
                }
                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Continue →",
                    enabled = uiState.clientScreenButtonEnabled
                )
            }
        }
    }
}