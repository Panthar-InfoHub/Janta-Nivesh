package org.velvetinvesting.jantanivesh.app.features.fd.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.dicgc_insured
import jantanivesh.shared.generated.resources.dropdown_outlined_icon
import jantanivesh.shared.generated.resources.fixed_deposit
import jantanivesh.shared.generated.resources.interest_payout_mode
import jantanivesh.shared.generated.resources.interest_rate
import jantanivesh.shared.generated.resources.insured_icon
import jantanivesh.shared.generated.resources.investment_amount_title
import jantanivesh.shared.generated.resources.loading
import jantanivesh.shared.generated.resources.maturity_amount
import jantanivesh.shared.generated.resources.maturity_date
import jantanivesh.shared.generated.resources.projected_returns
import jantanivesh.shared.generated.resources.select
import jantanivesh.shared.generated.resources.select_tenure
import jantanivesh.shared.generated.resources.set_investment_details
import jantanivesh.shared.generated.resources.total_interest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
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
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextFieldDefaults
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.SetInvestmentDetailsEvent
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.SetInvestmentDetailsUiState
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDTenureDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.PayoutType
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.RiskLevel

@Composable
fun SetInvestmentDetailsScreen(
    state: SetInvestmentDetailsUiState,
    onEvent: (SetInvestmentDetailsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val details = state.details
    if (details == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (state.isLoading) {
                Text(stringResource(Res.string.loading))
            } else {
                Text(state.errorMessage ?: "Something went wrong")
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                vertical = Spacing.dp8,
                horizontal = Spacing.dp20
            ) // General screen side padding
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
    ) {
        TopAppBar(onNavigateBack = { onEvent(SetInvestmentDetailsEvent.OnBackClicked) })

        BankNameCard(
            bankName = details.bankName,
            bankLogoUrl = details.bankLogo,
            invType = stringResource(Res.string.fixed_deposit),
            insuredText = stringResource(Res.string.dicgc_insured)
        )

        InvestmentCard(
            amount = state.amount,
            onAmountChange = { onEvent(SetInvestmentDetailsEvent.OnAmountChanged(it)) }
        )

        TenureCard(
            selectedTenure = state.selectedTenure,
            tenures = details.interestRates,
            onTenureChange = { onEvent(SetInvestmentDetailsEvent.OnTenureChanged(it)) }
        )

        InterestPayoutCard(
            selectedPayoutMode = state.selectedPayoutMode?.displayName ?: stringResource(Res.string.select),
            onPayoutModeChange = { /* Handle payout mode click */ TODO() }
        )

        ProjectedReturnsCard(
            maturityAmount = state.maturityAmount,
            totalInterest = state.totalInterest,
            interestRate = state.interestRate,
            maturityDate = state.maturityDate
        )

        Spacer(modifier = Modifier.height(Spacing.dp8))
    }
}

@Composable
private fun TopAppBar(onNavigateBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        AppBackButton(onClick = onNavigateBack)
        Text(
            text = stringResource(Res.string.set_investment_details),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Black
        )
    }
}

@Composable
private fun BankNameCard(
    bankName: String,
    bankLogoUrl: String,
    invType: String,
    insuredText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .padding(Spacing.dp20)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)
            ) {
                if (bankLogoUrl.isNotEmpty()) {
                    AsyncImage(
                        model = bankLogoUrl, contentDescription = "Bank Logo",
                        modifier = Modifier
                            .size(Spacing.dp40)
                            .clip(RoundedCornerShape(Spacing.dp58))
                            .background(SelectTenureCardColor)
                    )
                } else {
                    Text(
                        text = bankName.take(1),
                        style = MaterialTheme.typography.labelLarge,
                        color = Primary
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
                ) {
                    Text(
                        text = bankName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = invType,
                        style = MaterialTheme.typography.labelMedium,
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
                        "High Interest",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 12.sp),
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
                    contentDescription = "Insured",
                    modifier = Modifier.size(Spacing.dp16),
                    tint = SelectedBoxBorder
                )
                Text(insuredText, style = MaterialTheme.typography.labelMedium, color = GreyText)
            }
        }
    }
}

@Composable
fun InvestmentCard(amount: String, onAmountChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .padding(Spacing.dp20)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp16)) {
            Text(
                stringResource(Res.string.investment_amount_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            AppTextField(
                value = amount,
                onValueChange = onAmountChange,
                modifier = Modifier.fillMaxWidth(),
                style = AppTextFieldDefaults.style(
                    textStyle = MaterialTheme.typography.headlineMedium,
                    unfocusedBorderColor = SelectedBoxBorder
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
            ) {
                AmountChip(
                    amount = "10,000",
                    onClick = {
                        onAmountChange(
                            (amount.toLongOrNull() ?: 0L).plus(10000).toString()
                        )
                    })
                AmountChip(
                    amount = "50,000",
                    onClick = {
                        onAmountChange(
                            (amount.toLongOrNull() ?: 0L).plus(50000).toString()
                        )
                    })
                AmountChip(
                    amount = "1,00,00,000",
                    onClick = {
                        onAmountChange(
                            (amount.toLongOrNull() ?: 0L).plus(100000).toString()
                        )
                    })
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
        Text("+$amount", style = MaterialTheme.typography.labelMedium, color = GreyText)
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
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(SelectTenureCardColor)
            .padding(Spacing.dp20)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                Text(
                    text = stringResource(Res.string.select_tenure),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "(Days)",
                    style = MaterialTheme.typography.titleMedium,
                    color = GreyText
                )
            }

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
            ) {
                tenures.forEach { tenure ->
                    TenureChip(
                        months = tenure.tenureLabel,
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
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun InterestPayoutCard(
    selectedPayoutMode: String,
    onPayoutModeChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .padding(Spacing.dp20)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Text(
                text = stringResource(Res.string.interest_payout_mode),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            AppTextField(
                value = selectedPayoutMode,
                onValueChange = { TODO() },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPayoutModeChange(selectedPayoutMode) },
                trailingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.dropdown_outlined_icon),
                        contentDescription = "Dropdown",
                        tint = GreyText
                    )
                },
                style = AppTextFieldDefaults.style(
                    unfocusedBorderColor = SelectedBoxBorder
                )
            )
        }
    }
}

@Composable
fun ProjectedReturnsCard(
    maturityAmount: String,
    totalInterest: String,
    interestRate: String,
    maturityDate: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .padding(Spacing.dp20)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {
            Text(
                text = stringResource(Res.string.projected_returns),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ReturnDetailItem(
                    label = stringResource(Res.string.maturity_amount),
                    value = maturityAmount,
                    valueColor = Primary,
                    modifier = Modifier.weight(1f)
                )
                ReturnDetailItem(
                    label = stringResource(Res.string.total_interest),
                    value = totalInterest,
                    valueColor = SelectedBoxBorder,
                    modifier = Modifier.weight(1f)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.dp1)
                    .background(BoxBorder.copy(alpha = 0.5f))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ReturnDetailItem(
                    label = stringResource(Res.string.interest_rate),
                    value = interestRate,
                    valueColor = Black,
                    modifier = Modifier.weight(1f)
                )
                ReturnDetailItem(
                    label = stringResource(Res.string.maturity_date),
                    value = maturityDate,
                    valueColor = Black,
                    modifier = Modifier.weight(1f)
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = GreyText
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
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
            faqs = emptyList()
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
