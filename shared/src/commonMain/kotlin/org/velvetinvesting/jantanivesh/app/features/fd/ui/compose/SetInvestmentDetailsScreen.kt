package org.velvetinvesting.jantanivesh.app.features.fd.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.insured_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.HighInterestTextColor
import org.velvetinvesting.jantanivesh.app.core.theme.InterestChip
import org.velvetinvesting.jantanivesh.app.core.theme.InterestChipBorder
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.PreviewBackground
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectTenureCardColor
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedTenureChipColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.utils.filterDigits
import org.velvetinvesting.jantanivesh.app.core.utils.math.toMonthsFormatKmp
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextFieldDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDTenureDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.PayoutType
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.RiskLevel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.SetInvestmentDetailsEvent
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.SetInvestmentDetailsUiState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.MutualFundIcon

@Composable
fun SetInvestmentDetailsScreen(
    state: SetInvestmentDetailsUiState,
    onEvent: (SetInvestmentDetailsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.details == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (state.isLoading) {
                LoaderScreen()
            } else {
                ErrorScreen(
                    errorMessage = state.errorMessage?.ifEmpty { "Something Went Wrong" } ?: "Something Went Wrong",
                    onRetryClick = { onEvent(SetInvestmentDetailsEvent.LoadDetails) }
                )
            }
        }
        return
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .clearFocusOnTap()
            .padding(top = Spacing.dp8),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
    ) {
        TopAppBar(onNavigateBack = { onEvent(SetInvestmentDetailsEvent.OnBackClicked) },
            modifier= Modifier
                .padding(horizontal = Spacing.dp16))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.dp16)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
            contentPadding = PaddingValues(vertical = Spacing.dp12)
        ) {
            item {
                BankNameCard(
                    bankName = state.details.bankName,
                    bankLogoUrl = state.details.bankLogo,
                    insuredText = state.details.keyFeatures.firstOrNull()?.title ?: "",
                    tags = state.details.tags
                )
            }

            item {
                InvestmentCard(
                    amount = state.amount,
                    minAmount = state.minAmount,
                    onAmountChange = { onEvent(SetInvestmentDetailsEvent.OnAmountChanged(it)) }
                )
            }

            item {
                InterestPayoutCard(
                    data = state,
                    onPayoutModeChange = { payout ->
                        onEvent(SetInvestmentDetailsEvent.OnPayoutModeChanged(payout))
                    }
                )
            }

            item {
                AnimatedVisibility(state.selectedPayoutMode!=null){
                    TenureCard(
                        selectedTenure = state.selectedTenure,
                        tenures = state.availableTenures,
                        onTenureChange = { onEvent(SetInvestmentDetailsEvent.OnTenureChanged(it)) }
                    )
                }
            }

            if (state.isButtonEnabled){
                item {
                    ProjectedReturnsCard(
                        maturityAmount = state.maturityAmount,
                        totalInterest = state.totalInterest,
                        interestRate = state.interestRate,
                        maturityDate = state.maturityDate
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(Spacing.dp8))
            }
        }
        NextButtonFooter(
            value = "Invest Now",
            onClick = { onEvent(SetInvestmentDetailsEvent.OnContinueClicked) },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.isButtonEnabled,
            loading = state.isPurchasing
        )
    }
}

@Composable
private fun TopAppBar(onNavigateBack: () -> Unit, modifier: Modifier) {
    Row(
        modifier=modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        AppBackButton(onClick = onNavigateBack)
        Text(
            text = "Set Investment Details",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Black
        )
    }
}

@Composable
private fun BankNameCard(
    bankName: String,
    bankLogoUrl: String,
    invType: String = "Fixed Deposit",
    insuredText: String,
    tags: List<String>
) {
    Box(
        modifier = Modifier
            .genericDropShadow(RoundedCornerShape(Spacing.dp12))
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .padding(Spacing.dp20)
    ) {
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                SubcomposeAsyncImage(
                    model = bankLogoUrl,
                    contentDescription = "Bank Logo",
                    modifier = Modifier.size(Spacing.dp48),

                    loading = {
                        MutualFundIcon(
                            schemeName = bankName,
                            size = Spacing.dp40,
                            cornerRadius = Spacing.dp40,
                            backgroundColor = GreyBox,
                            textColor = Primary
                        )
                    },

                    error = {
                        MutualFundIcon(
                            schemeName = bankName,
                            size = Spacing.dp40,
                            cornerRadius = Spacing.dp40,
                            backgroundColor = GreyBox,
                            textColor = Primary
                        )
                    },

                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
                ) {
                    Text(
                        text = bankName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = invType,
                        style = MaterialTheme.typography.labelSmall,
                        color = GreyText
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Spacing.dp16))
                        .background(color = InterestChip)
                        .border(
                            width = Spacing.dp1,
                            color = InterestChipBorder,
                            shape = RoundedCornerShape(Spacing.dp16)
                        )
                        .padding(horizontal = Spacing.dp10, vertical = Spacing.dp4)
                ) {
                    Text(
                        tags.firstOrNull() ?: "High Interest",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = HighInterestTextColor
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
            ) {
                Spacer(modifier = Modifier.size(Spacing.dp40))
                Icon(
                    painter = painterResource(Res.drawable.insured_icon),
                    contentDescription = "Insured Icon",
                    modifier = Modifier.size(Spacing.dp16),
                    tint = SelectedBoxBorder
                )
                Text(insuredText, style = MaterialTheme.typography.labelSmall, color = GreyText)
            }
        }
    }
}

@Composable
fun InvestmentCard(amount: String, onAmountChange: (String) -> Unit, minAmount: Long) {
    Box(
        modifier = Modifier
            .genericDropShadow(RoundedCornerShape(Spacing.dp12))
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .padding(Spacing.dp20)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
            Text(
                "Investment Amount",
                style = MaterialTheme.typography.labelLarge
            )

            AppTextField(
                value = amount,
                onValueChange = { onAmountChange(it.filterDigits()) },
                modifier = Modifier.fillMaxWidth(),
                style = AppTextFieldDefaults.style(
                    textStyle = MaterialTheme.typography.headlineMedium,
                    unfocusedBorderColor = SelectedBoxBorder
                ),
                prefix = {
                    Text(
                        "₹ ",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                isError = (amount.toLongOrNull()?:0L) < minAmount
            )

            if ((amount.toLongOrNull()?:0L) < minAmount){
                Text(
                    text = "Selected amount should be greater the $minAmount",
                    style = MaterialTheme.typography.labelSmall,
                    color = appRed
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                AmountChip(
                    amount = "10,000",
                    onClick = {
                        onAmountChange(
                            10000.toString()
                        )
                    })
                AmountChip(
                    amount = "50,000",
                    onClick = {
                        onAmountChange(50000.toString())
                    })
                AmountChip(
                    amount = "1,00,000",
                    onClick = {
                        onAmountChange(
                            100000.toString()
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun AmountChip(amount: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Spacing.dp4))
            .border(
                width = Spacing.dp1,
                color = GreyText.copy(alpha = 0.2f),
                shape = RoundedCornerShape(Spacing.dp4)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.dp12, vertical = Spacing.dp8),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "+$amount",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = GreyText
        )
    }
}

@Composable
fun TenureCard(
    selectedTenure: FDTenureDomain?,
    tenures: List<FDTenureDomain>,
    onTenureChange: (FDTenureDomain) -> Unit
) {
    Box(
        modifier = Modifier
            .genericDropShadow(RoundedCornerShape(Spacing.dp12))
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .border(1.dp, SelectedBoxBorder, RoundedCornerShape(Spacing.dp12))
            .background(SelectTenureCardColor)
            .padding(Spacing.dp20)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Tenure",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "(Months)",
                    style = MaterialTheme.typography.labelSmall,
                    color = GreyText
                )
            }

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
            ) {
                tenures.forEach { tenure ->
                    TenureChip(
                        months = tenure.tenureDays.toMonthsFormatKmp(),
                        interestRate = "${tenure.interestRate}%",
                        isSelected = selectedTenure?.id == tenure.id
                    ) { onTenureChange(tenure) }
                }
            }
        }
    }
}

@Composable
fun TenureChip(
    months: String,
    interestRate: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) SelectedTenureChipColor else White
    val textColor = if (isSelected) Primary else Black

    Box(
        modifier = Modifier
            .size(Spacing.dp64)
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
        ) {
            Text(
                text = months,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = textColor
            )
            if (isSelected && interestRate != null) {
                Text(
                    text = interestRate,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun InterestPayoutCard(
    data: SetInvestmentDetailsUiState,
    onPayoutModeChange: (PayoutType) -> Unit
) {
    Box(
        modifier = Modifier
            .clearFocusOnTap()
            .genericDropShadow(RoundedCornerShape(Spacing.dp12))
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .padding(Spacing.dp20)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Text(
                text = "Interest Payout Mode",
                style = MaterialTheme.typography.labelLarge
            )
            DropDownSelector(
                value = data.selectedPayoutMode?.displayName ?: "",
                onValueChange = onPayoutModeChange,
                placeholder = "Select Payout Mode",
                mandatory = false,
                modifier = Modifier.fillMaxWidth(),
                list = data.frequencies,
                textConvertor = {
                    it.displayName
                }
            )
        }
    }
}

@Composable
private fun ProjectedReturnsCard(
    maturityAmount: String,
    totalInterest: String,
    interestRate: String,
    maturityDate: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .border(Spacing.dp1, BoxBorder, RoundedCornerShape(Spacing.dp12))
            .background(White)
            .padding(Spacing.dp20)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Text(
                text = "Projected Returns",
                style = MaterialTheme.typography.labelLarge,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ReturnDetailItem(
                    label = "Maturity Amount",
                    value = maturityAmount,
                    valueColor = Primary,
                )
                ReturnDetailItem(
                    label = "Total Interest",
                    value = totalInterest,
                    valueColor = SelectedBoxBorder,
                )
            }

            HorizontalDivider(thickness = Spacing.dp1, color = BoxBorder.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ReturnDetailItem(
                    label = "Interest Rate",
                    value = interestRate,
                    valueColor = Black,
                    valueStyle = MaterialTheme.typography.labelLarge,
                )
                ReturnDetailItem(
                    label = "Maturity Date",
                    value = maturityDate,
                    valueColor = Black,
                    valueStyle = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun ReturnDetailItem(
    label: String,
    value: String,
    valueColor: Color,
    valueStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = GreyText
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = valueColor
        )
    }
}

@Preview(showBackground = true, heightDp = 1500)
@Composable
fun SetInvestmentDetailsScreenPreview() {
    JantaNiveshTheme {
        val dummyDetails = FDDetailsDomain(
            id = "1",
            bankName = "HDFC Bank",
            bankLogo = "https://example.com/hdfc_logo.png",
            rating = "AAA",
            maxInterestRate = 7.60,
            riskLabel = RiskLevel.LOW,
            minDeposit = 10000,
            invest = 100000,
            selectedPayout = PayoutType.Cumulative,
            applicable = "Regular",
            payoutOptions = listOf(PayoutType.Cumulative),
            applicableFor = listOf("Regular"),
            interestRates = listOf(
                FDTenureDomain(
                    id = "2",
                    tenureLabel = "24",
                    tenureDays = 1095,
                    interestRate = 7.60,
                    annualYield = 7.80,
                    isDefault = false,
                    payoutFrequency = PayoutType.Cumulative
                ),
                FDTenureDomain(
                    id = "1",
                    tenureLabel = "36",
                    tenureDays = 1095,
                    interestRate = 7.60,
                    annualYield = 7.80,
                    isDefault = true,
                    payoutFrequency = PayoutType.Cumulative
                )
            ),
            lockInDays = 30,
            prematurePenalty = 0.5,
            insuranceAmount = "₹5L",
            about = "HDFC FD.",
            keyFeatures = emptyList(),
            faqs = emptyList(),
            tags = listOf("High Interest", "DICGC Insured")
        )

        SetInvestmentDetailsScreen(
            state = SetInvestmentDetailsUiState(
                details = dummyDetails,
                amount = "100000",
                selectedTenure = dummyDetails.interestRates.first(),
                selectedPayoutMode = PayoutType.Cumulative,
                maturityAmount = "₹1,25,310",
                totalInterest = "₹25,310",
                interestRate = "7.60% p.a.",
                maturityDate = "15 Oct 2027"
            ),
            onEvent = {},
            modifier = Modifier.background(color = PreviewBackground)
        )
    }
}