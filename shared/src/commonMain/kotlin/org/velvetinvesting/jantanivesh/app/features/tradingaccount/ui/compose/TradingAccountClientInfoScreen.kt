package org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import org.sharad.velvetinvestment.utils.tradingaccount.FatcaOccupationType
import org.sharad.velvetinvestment.utils.tradingaccount.SourceOfWealth
import org.sharad.velvetinvestment.utils.tradingaccount.YesNo
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TitledAppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.ClientType
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.DefaultDp
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.enums.Holding
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.Data
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.domain.models.TradingAccountFormDomain
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountEvent
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountUiState

@Preview
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
            title = "Trading Account",
            stepCount = 7,
            totalSteps = 7,
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
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp2)
                        ) {
                            Text(
                                "Client Information Form",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                "Fields are conditionally mandatory based on your selections",
                                fontSize = 14.sp,
                                color = Color(0xff4A5565)
                            )
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .genericDropShadow(RoundedCornerShape(Spacing.dp16))
                                .clip(RoundedCornerShape(Spacing.dp24))
                                .background(Color.White)
                                .padding(Spacing.dp16),
                            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
                        ) {
                            DropDownSelector(
                                title = "Client Type",
                                value = ClientType.getDisplayName(data.client_type),
                                onValueChange = { handleEvent(TradingAccountEvent.OnClientTypeChangeUi(it)) },
                                placeholder = "Client Type",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                list = ClientType.entries,
                                textConvertor = { it.displayName }
                            )

                            if (data.client_type == ClientType.DEMAT.code) {
                                DropDownSelector(
                                    title = "PMS",
                                    value = YesNo.displayNameFromCode(data.pms),
                                    onValueChange = { handleEvent(TradingAccountEvent.OnPmsChange(it.code)) },
                                    placeholder = "Y/N",
                                    mandatory = true,
                                    list = YesNo.entries,
                                    textConvertor = { it.displayName }
                                )

                                DropDownSelector(
                                    title = "Default DP",
                                    value = DefaultDp.getDisplayName(data.default_dp),
                                    onValueChange = { handleEvent(TradingAccountEvent.OnDefaultDpChangeUi(it)) },
                                    placeholder = "Select Default DP",
                                    mandatory = true,
                                    list = DefaultDp.entries,
                                    textConvertor = { it.displayName }
                                )
                            }

                            if (data.default_dp == DefaultDp.CDSL.code) {
                                TitledAppTextField(
                                    title = "CDSL DP ID",
                                    value = data.cdsl_dpid,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnCdslDpidChange(it)) },
                                    placeholder = "Enter CDSL DP ID",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Number
                                )

                                TitledAppTextField(
                                    title = "CDSL Client ID",
                                    value = data.cdslcltid,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnCdslCltidChange(it)) },
                                    placeholder = "Enter CDSL Client ID",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Number
                                )
                            }

                            if (data.default_dp == DefaultDp.NSDL.code) {
                                TitledAppTextField(
                                    title = "CMBP ID",
                                    value = data.cmbp_id,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnCmbpIdChange(it)) },
                                    placeholder = "Enter CMBP ID",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Text
                                )

                                TitledAppTextField(
                                    title = "NSDL DP ID",
                                    value = data.nsdldpid,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnNsdlDpidChange(it)) },
                                    placeholder = "Enter NSDL DP ID",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Text
                                )

                                TitledAppTextField(
                                    title = "NSDL Client ID",
                                    value = data.nsdlcltid,
                                    onValueChange = { handleEvent(TradingAccountEvent.OnNsdlCltidChange(it)) },
                                    placeholder = "Enter NSDL Client ID",
                                    mandatory = true,
                                    keyboardType = KeyboardType.Number
                                )
                            }

                            DropDownSelector(
                                title = "Source Of Wealth",
                                value = SourceOfWealth.getDisplayName(data.srce_wealt),
                                onValueChange = { handleEvent(TradingAccountEvent.OnSourceWealthChange(it.code)) },
                                placeholder = "Source Of Wealth",
                                mandatory = true,
                                list = SourceOfWealth.entries,
                                textConvertor = { it.displayName }
                            )

                            DropDownSelector(
                                title = "Occupation Category",
                                value = FatcaOccupationType.getDisplayName(data.occ_type),
                                onValueChange = { handleEvent(TradingAccountEvent.OnOccTypeChange(it.code)) },
                                placeholder = "Occupation Category",
                                mandatory = true,
                                list = FatcaOccupationType.entries,
                                textConvertor = { it.displayName }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(pv.calculateBottomPadding())) }
                }

                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Next",
                    enabled = uiState.clientScreenButtonEnabled
                )
            }
        }
    }
}