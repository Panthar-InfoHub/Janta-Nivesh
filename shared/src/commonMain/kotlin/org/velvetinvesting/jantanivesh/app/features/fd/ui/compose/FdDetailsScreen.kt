package org.velvetinvesting.jantanivesh.app.features.fd.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.dropdown_outlined_icon
import jantanivesh.shared.generated.resources.edit_icon
import jantanivesh.shared.generated.resources.ic_feature_compounding
import jantanivesh.shared.generated.resources.share_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.*
import org.velvetinvesting.jantanivesh.app.core.utils.filterDigits
import org.velvetinvesting.jantanivesh.app.core.utils.math.toYearsFormatKmp
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextFieldDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.DropDownSelector
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.*
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FDModalType
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FDTenureUiModel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FdDetailsEvent
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FdDetailsUiState

@Composable
fun FdDetailsScreen(
    state: FdDetailsUiState,
    onEvent: (FdDetailsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val details = state.details
    if (details == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (state.isLoading) {
                Text("Loading")
            } else {
                Text(state.errorMessage ?: "Something went wrong")
            }
        }
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundFill,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(Spacing.dp16)
            ) {
                AppButton(
                    text = "Invest Now",
                    onClick = { onEvent(FdDetailsEvent.OnInvestNowClicked) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Spacing.dp16)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
        ) {
            // Top Bar
            TopBar(
                onBack = { onEvent(FdDetailsEvent.OnBackClicked) },
                onShare = { onEvent(FdDetailsEvent.OnShareClicked) })

            // Header Card
            HeaderCard(details = details)

            // Investment Config Card
            InvestmentConfigCard(data = state, onEvent = onEvent)

            // Tenure Options Section
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                Text(
                    text = "Tenure options",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 16.sp),
                    color = Primary
                )

                TenureOptionsCard(
                    options = state.calculatedTenures,
                    onOptionSelected = { onEvent(FdDetailsEvent.OnTenureSelected(it)) }
                )

                // Disclaimers
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                    Text(
                        text = "* Interest rates are annualized.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            lineHeight = 15.sp
                        ),
                        color = GreyText
                    )
                    Text(
                        text = "** Calculated based on '${state.selectedPayoutMode?.displayName}' payout selection.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            lineHeight = 15.sp
                        ),
                        color = GreyText
                    )
                }
            }

            // Key Features Section
            if (details.keyFeatures.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Key Features",
                        style = MaterialTheme.typography.labelLarge,
                        color = Primary
                    )

                    for (feature in details.keyFeatures) {
                        FeatureCard(feature = feature)
                    }
                }
            }

            // FAQs Section TODO can be removed
            if (details.faqs.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "FAQs",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Primary
                    )
                    for (faq in details.faqs) {
                        FaqCard(faq = faq)
                    }
                }
            }
            Spacer(modifier = Modifier.height(Spacing.dp16))
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit, onShare: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppBackButton(onClick = onBack)

        IconButton(
            onClick = onShare,
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent)
        ) {
            Icon(
                painter = painterResource(Res.drawable.share_icon),
                contentDescription = "Share",
                tint = SelectedBoxBorder,
                modifier = Modifier.size(Spacing.dp20)
            )
        }
    }
}

@Composable
private fun HeaderCard(details: FDDetailsDomain) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp8))
            .background(White)
            .padding(Spacing.dp16)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp20)) {
            // Bank Info Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(Spacing.dp40)
                        .clip(CircleShape)
                        .background(GreyBox),
                    contentAlignment = Alignment.Center
                ) {
                    if (details.bankLogo.isNotEmpty()) {
                        AsyncImage(
                            model = details.bankLogo,
                            contentDescription = "Bank Logo",
                            modifier = Modifier
                                .size(Spacing.dp40)
                                .clip(CircleShape)
                                .background(SelectTenureCardColor)
                        )
                    } else {
                        Text(
                            text = details.bankName.take(1) + details.bankName.substringAfter(" ")
                                .take(1),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Primary
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                    Text(
                        text = details.bankName,
                        style = MaterialTheme.typography.labelLarge,
                        color = Black
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                        items(details.tags) { tag ->
                            TagChip(
                                text = tag,
                                bgColor = TagPopularBg,
                                textColor = Black
                            )
                        }
                    }
                }
            }

            // Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
            ) {
                MetricBox(
                    title = "INTEREST",
                    value = "${details.maxInterestRate}%",
                    suffix = " p.a.",
                    modifier = Modifier.weight(1f),
                    valueColor = SelectedBoxBorder
                )
                val defaultTenure =
                    details.interestRates.firstOrNull { it.isDefault }?.tenureDays?.toYearsFormatKmp()
                        .toString()
                MetricBox(
                    title = "TENURE",
                    value = defaultTenure,
                    modifier = Modifier.weight(1f)
                )
                MetricBox(
                    title = "MIN. AMT",
                    value = "₹${details.minDeposit}",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    suffix: String? = null,
    valueColor: Color = Black
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(Spacing.dp8))
            .background(GreyBox)
            .padding(vertical = Spacing.dp12),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleSmall, color = GreyText)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                color = valueColor
            )
            if (suffix != null) {
                Text(
                    text = suffix,
                    style = MaterialTheme.typography.titleSmall,
                    color = GreyText,
                    modifier = Modifier.padding(bottom = Spacing.dp2)
                )
            }
        }
    }
}

@Composable
private fun InvestmentConfigCard(
    data: FdDetailsUiState,
    onEvent: (FdDetailsEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
    ) {
        Column {
            // Invest Amount
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.dp16),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp8),
            ) {
                Text(
                    text = "Invest",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    ),
                    color = GreyText
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val focusRequester = remember { FocusRequester() }
                    val keyboardController = LocalSoftwareKeyboardController.current
                    AppTextField(
                        value = data.investInput,
                        onValueChange = { onEvent(FdDetailsEvent.OnUpdateInvest(it.filterDigits())) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        prefix = { Text("₹ ", style = MaterialTheme.typography.headlineSmall) },
                        trailingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(Spacing.dp32)
                                    .clip(CircleShape)
                                    .background(HighlightRowBg)
                                    .clickable {
                                        focusRequester.requestFocus()
                                        keyboardController?.show()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.edit_icon),
                                    contentDescription = "Edit Amount",
                                    tint = Primary,
                                    modifier = Modifier.size(Spacing.dp14)
                                )
                            }
                        },
                        modifier = Modifier.weight(1f).focusRequester(focusRequester),
                        style = AppTextFieldDefaults.style(textStyle = MaterialTheme.typography.headlineSmall)
                    )

                }
            }

            HorizontalDivider(color = GreyBoxDivider, modifier = Modifier.padding(horizontal = Spacing.dp16))

            // Interest Payout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEvent(FdDetailsEvent.OnPayoutTypeClicked) }
                    .padding(Spacing.dp16),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                    Text(
                        text = "Interest payout",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        ),
                        color = GreyText
                    )
                    DropDownSelector(
                        value = data.details?.selectedPayout?.displayName ?: "",
                        onValueChange = {
                            onEvent(FdDetailsEvent.OnUpdatePayout(it))
                        },
                        placeholder = data.details?.selectedPayout?.displayName ?: "UNAVAILABLE",
                        mandatory = false,
                        modifier = Modifier.fillMaxWidth(),
                        list = data.frequencies,
                        textConvertor = {
                            it.displayName
                        }
                    )
                }
                Icon(
                    painter = painterResource(Res.drawable.dropdown_outlined_icon),
                    contentDescription = "Select",
                    tint = GreyText,
                    modifier = Modifier.size(Spacing.dp12)
                )
            }

            HorizontalDivider(color = GreyBoxDivider, modifier = Modifier.padding(horizontal = Spacing.dp16))

            // Applicable For
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEvent(FdDetailsEvent.OnApplicantCategoryClicked) }
                    .padding(Spacing.dp16),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                    Text(
                        text = "Applicable for",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        ),
                        color = GreyText
                    )
                    DropDownSelector(
                        value = data.details?.applicable ?: "UNAVAILABLE",
                        onValueChange = {
                            onEvent(FdDetailsEvent.OnUpdateApplicable(it))
                        },
                        placeholder = data.details?.applicable ?: "UNAVAILABLE",
                        mandatory = false,
                        modifier = Modifier.fillMaxWidth(),
                        list = data.details?.applicableFor ?: emptyList(),
                        textConvertor = {
                            it
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TenureOptionsCard(
    options: List<FDTenureUiModel>,
    onOptionSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x80F4FFef))
                    .padding(horizontal = Spacing.dp16, vertical = Spacing.dp16),
            ) {
                Text(
                    text = "Tenure",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    ),
                    color = GreyText,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Interest*",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    ),
                    color = GreyText,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "You receive**",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    ),
                    color = GreyText,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }

            HorizontalDivider(color = GreyBoxDivider)

            options.forEachIndexed { index, option ->
                val bgColor = if (option.isDefault) HighlightRowBg else White

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor)
                        .clickable { onOptionSelected(option.id) }
                        .padding(horizontal = Spacing.dp16, vertical = Spacing.dp16),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
                ) {
                    if (option.isDefault)
                        VerticalDivider(
                            thickness = Spacing.dp4,
                            color = Primary,
                            modifier = Modifier.height(Spacing.dp52).clip(
                                RoundedCornerShape(
                                    topStart = Spacing.dp16,
                                    bottomStart = Spacing.dp16
                                )
                            )
                        )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
                    ) {
                        Text(
                            text = option.tenureLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = Primary
                        )
                        if (option.isDefault) {
                            TagChip(
                                text = "MAX RETURN",
                                bgColor = TagMaxReturnBg,
                                textColor = TagMaxReturnText
                            )
                        }
                    }
                    Text(
                        text = "${option.interestRate}%",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (option.isDefault) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = Primary,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "₹ ${option.maturityAmount}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (option.isDefault) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = Primary,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }

                if (index != options.lastIndex) {
                    HorizontalDivider(color = GreyBoxDivider)
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(feature: KeyFeatureDomain) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp8))
            .background(White)
            .padding(Spacing.dp16)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp40)
                    .clip(CircleShape)
                    .background(BackgroundFill),
                contentAlignment = Alignment.Center
            ) {
                if (!feature.iconUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = feature.iconUrl,
                        contentDescription = "Bank Logo",
                        modifier = Modifier
                            .size(Spacing.dp40),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.ic_feature_compounding),
                        contentDescription = feature.title,
                        tint = Primary,
                        modifier = Modifier.size(Spacing.dp16)
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                Text(
                    text = feature.title,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Primary
                )
                Text(
                    text = feature.description,
                    style = MaterialTheme.typography.titleSmall,
                    color = GreyText,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun FaqCard(faq: FDFaqDomain) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .padding(Spacing.dp16)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = faq.question,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                text = faq.answer,
                style = MaterialTheme.typography.titleSmall,
                color = GreyText,
                lineHeight = MaterialTheme.typography.bodySmall.lineHeight
            )
        }
    }
}

@Composable
private fun TagChip(
    text: String,
    bgColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(bgColor)
            .padding(horizontal = Spacing.dp8, vertical = Spacing.dp4)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            ),
            color = textColor
        )
    }
}

// --- PREVIEW ---
@Preview(showBackground = true, heightDp = 1500, widthDp = 360)
@Composable
fun FdDetailsScreenPreview() {
    JantaNiveshTheme {
        val dummyDetails = FDDetailsDomain(
            id = "1",
            bankName = "State Bank of India",
            bankLogo = "",
            rating = "AAA",
            maxInterestRate = 7.35,
            riskLabel = RiskLevel.LOW,
            minDeposit = 10000,
            invest = 500000,
            selectedPayout = PayoutType.Cumulative,
            applicable = "Senior citizen",
            payoutOptions = listOf(PayoutType.Cumulative),
            applicableFor = listOf("Senior citizen"),
            interestRates = listOf(
                FDTenureDomain(
                    id = "1",
                    tenureLabel = "1Y",
                    tenureDays = 365,
                    interestRate = 6.80,
                    annualYield = 534000.0,
                    isDefault = false,
                    payoutFrequency = PayoutType.Cumulative
                ),
                FDTenureDomain(
                    id = "3",
                    tenureLabel = "3Y",
                    tenureDays = 1095,
                    interestRate = 7.35,
                    annualYield = 610250.0,
                    isDefault = true,
                    payoutFrequency = PayoutType.Cumulative
                )
            ),
            lockInDays = 30,
            prematurePenalty = 0.5,
            insuranceAmount = "₹5L",
            about = "SBI FD is secure.",
            keyFeatures = listOf(
                KeyFeatureDomain(
                    "Quarterly Compounding",
                    "Interest is calculated every 3 months and added back to your savings for higher total growth."
                )
            ),
            faqs = emptyList()
        )

        FdDetailsScreen(
            state = FdDetailsUiState(
                details = dummyDetails
            ),
            onEvent = {}
        )
    }
}
