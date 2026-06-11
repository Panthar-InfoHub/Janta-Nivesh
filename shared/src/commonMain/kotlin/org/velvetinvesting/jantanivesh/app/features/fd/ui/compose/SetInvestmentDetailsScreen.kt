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
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.insured_icon
import org.jetbrains.compose.resources.painterResource
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
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextFieldDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
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
                Text("Loading...")
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
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
    ) {
        TopAppBar(onNavigateBack = { onEvent(SetInvestmentDetailsEvent.OnBackClicked) })

        BankNameCard(
            bankName = details.bankName,
            bankLogoUrl = details.bankLogo,
            invType = "Fixed Deposit",
            insuredText = "DICGC INSURED",
            tags = details.tags
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
            data = state,
            onPayoutModeChange = { SetInvestmentDetailsEvent.OnPayoutModeChanged(it) }
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
    invType: String,
    insuredText: String,
    tags: List<String>
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
                Text(insuredText, style = MaterialTheme.typography.labelSmall, color = GreyText) //TODO fetch text dynamically
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
                "Invest",
                style = MaterialTheme.typography.labelLarge
            )

            AppTextField(
                value = amount,
                onValueChange = onAmountChange,
                modifier = Modifier.fillMaxWidth(),
                style = AppTextFieldDefaults.style(
                    textStyle = MaterialTheme.typography.headlineMedium,
                    unfocusedBorderColor = SelectedBoxBorder
                ),
                leadingIcon = {
                    Text(
                        "₹ ",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
            ) {
                AmountChip(
                    amount = "10K",
                    onClick = {
                        onAmountChange(
                            (amount.toLongOrNull() ?: 0L).plus(10000).toString()
                        )
                    })
                AmountChip(
                    amount = "50K",
                    onClick = {
                        onAmountChange(
                            (amount.toLongOrNull() ?: 0L).plus(50000).toString()
                        )
                    })
                AmountChip(
                    amount = "1L",
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
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(SelectTenureCardColor)
            .padding(Spacing.dp20)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                Text(
                    text = "Tenure options",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "Days",
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
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .padding(Spacing.dp20)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Text(
                text = "Interest payout",
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
fun ProjectedReturnsCard(
    maturityAmount: String,
    totalInterest: String,
    interestRate: String,
    maturityDate: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .padding(Spacing.dp20)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
        ) {
            Text(
                text = "Your return",
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
                    modifier = Modifier.weight(1f)
                )
                ReturnDetailItem(
                    label = "Total Interest",
                    value = totalInterest,
                    valueColor = SelectedBoxBorder,
                    modifier = Modifier.weight(1f)
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
        verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
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