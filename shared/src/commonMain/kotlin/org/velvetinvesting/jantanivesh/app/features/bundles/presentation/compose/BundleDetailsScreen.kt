package org.velvetinvesting.jantanivesh.app.features.bundles.presentation.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.back_arrow
import jantanivesh.shared.generated.resources.cart_icon
import jantanivesh.shared.generated.resources.lock_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardSubtitle
import org.velvetinvesting.jantanivesh.app.core.theme.GrayBackGround
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.IconSize
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.SlateGray
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.formatWithCommas
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.AssetAllocationDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleCategoryDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleMetaDataDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleRisk
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.PortfolioSlotDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.deriveTransactionRules
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.investmentName
import org.velvetinvesting.jantanivesh.app.features.bundles.presentation.viewmodel.BundleDetailsEvent
import org.velvetinvesting.jantanivesh.app.features.bundles.presentation.viewmodel.BundleDetailsUiState
import org.velvetinvesting.jantanivesh.app.features.bundles.presentation.viewmodel.unsupportedFundNames
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PurchaseMode
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ShadowCard
import org.velvetinvesting.jantanivesh.app.features.plans.ui.compose.PurchaseDayPickerSheet

internal val DebtColor = Color(0xFF4CAF50)
internal val HybridColor = Color(0xFF2196F3)

@Composable
fun BundleDetailsScreen(
    state: BundleDetailsUiState,
    onEvent: (BundleDetailsEvent) -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.imePadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val bundle = state.bundle
            when {
                bundle == null && state.error != null -> ErrorScreen(
                    errorMessage = state.error,
                    onRetryClick = { onEvent(BundleDetailsEvent.Retry) }
                )

                bundle == null -> LoaderScreen()

                else -> Column(modifier = Modifier.fillMaxWidth()){
                    BundleDetailsHeader(
                        onBackClick={onEvent(BundleDetailsEvent.OnBackClicked)},
                        onCartClick={onEvent(BundleDetailsEvent.OnCartClicked)},
                        bundle=bundle,
                        cartFundCount = state.cartFundCount,
                        modifier = Modifier.fillMaxWidth().padding(start = Spacing.dp20, end = Spacing.dp20, bottom = Spacing.dp16, top = Spacing.dp16)
                    )
                    BundleDetailsContent(
                        bundle = bundle,
                        state = state,
                        onEvent = onEvent,
                        modifier = Modifier.weight(1f)
                    )
                    NextButtonFooter(
                        value = "Add to Cart",
                        onClick = { onEvent(BundleDetailsEvent.OnAddToCartClicked) },
                        enabled = state.canInvest && !state.isAddingToCart,
                        loading = state.isAddingToCart
                    )
                }
            }
        }
    }

    if (state.showSipDayPicker) {
        // Only the days every selected fund accepts are selectable.
        PurchaseDayPickerSheet(
            selectedDay = state.selectedSipDay,
            allowedDays = state.transactionRules?.sipAllowedDates.orEmpty(),
            onDaySelected = { onEvent(BundleDetailsEvent.OnSipDaySelected(it)) },
            onDismiss = { onEvent(BundleDetailsEvent.OnSipDayPickerDismissed) }
        )
    }
}

@Composable
private fun BundleDetailsHeader(
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    bundle: BundleDetailsDomain,
    cartFundCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier.size(IconSize.dp20).clickable(onClick = onBackClick)
            )
            Text(
                text = "JANTA RECOMMENDED BUNDLES",
                style = tinyLabel.copy(fontWeight = FontWeight.Bold),
                color = Secondary,
                modifier= Modifier.weight(1f)
            )
            CartIconWithBadge(
                count = cartFundCount,
                onClick = onCartClick,
                modifier = Modifier.padding(end = Spacing.dp8)
            )
        }

        Column(
            modifier= Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp2)
        ) {
            Text(
                text = bundle.name,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                text = bundle.description,
                style = MaterialTheme.typography.bodySmall,
                color = BundleCardSubtitle
            )
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp12),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {
            BundleTag(
                text = bundle.metaData.investmentGrowth
            )
            BundleTag(
                text= bundle.metaData.investmentTime
            )
            BundleRiskPill(
                risk = bundle.metaData.riskLevel,
                style = bundle.metaData.risk.style.toCardStyle()
            )
        }
    }
}
/** The header cart icon, with the number of funds in the cart on its corner once there are any. */
@Composable
private fun CartIconWithBadge(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clickable(onClick = onClick),
        contentAlignment = Alignment.TopEnd
    ) {
        Icon(
            painter = painterResource(Res.drawable.cart_icon),
            contentDescription = "Cart",
            modifier = Modifier.size(IconSize.dp20),
            tint = Secondary
        )
        if (count > 0) {
            Box(
                modifier = Modifier
                    .offset(x = 6.dp, y = (-6).dp)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (count > 9) "9+" else count.toString(),
                    color = Color.White,
                    fontSize = 7.sp,
                    lineHeight = 8.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun BundleDetailsContent(
    bundle: BundleDetailsDomain,
    state: BundleDetailsUiState,
    onEvent: (BundleDetailsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            InvestmentSummaryCard(
                bundleName = bundle.name,
                investmentAmount = state.investmentAmount,
                purchaseMode = state.purchaseMode,
                unsupportedFundNames = state.unsupportedSlots.mapNotNull { it.selectedFund?.name },
                selectedSipDay = state.selectedSipDay,
                onPurchaseModeChange = { onEvent(BundleDetailsEvent.OnPurchaseModeSelected(it)) },
                onAmountChange = { onEvent(BundleDetailsEvent.OnAmountChanged(it)) },
                onSipDayClick = { onEvent(BundleDetailsEvent.OnSipDayClicked) },
                minAmount = state.minAmount,
                assetAllocation = bundle.assetAllocation
            )
        }

        if (!state.allSlotsFilled) {
            item {
                InfoBox("Select a fund for every slot to proceed with this portfolio.")
            }
        }

        item {
            BundleOverviewCard(bundle, investmentAmount = state.investmentAmount)
        }

        item {
            Text(
                text = "FUND DISTRIBUTION BREAKDOWN",
                style = tinyLabel,
                color = titleColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(bundle.categories, key = { it.id }) { category ->
            CategoryDistributionItem(
                category = category,
                investmentAmount = state.investmentAmount,
                purchaseMode = state.purchaseMode,
                unsupportedFundNames = state.unsupportedFundNames(category),
                onChangeFundClick = { onEvent(BundleDetailsEvent.OnChangeFundClicked(category.id)) }
            )
        }
    }
}

@Composable
private fun BundleOverviewCard(bundle: BundleDetailsDomain, investmentAmount: Long) {
    ShadowCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(Spacing.dp20),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.dp8)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "TARGET ASSET ALLOCATION",
                    style = tinyLabel.copy(fontWeight = FontWeight.Bold),
                    color = Primary
                )
                Text(
                    text = "Auto-rebalanced",
                    fontSize = 10.sp,
                    color = BundleCardSubtitle
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = GrayBackGround
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
            ){
                AllocationDonut(bundle.assetAllocation)
                AssetLegend(
                    allocation = bundle.assetAllocation,
                    investmentAmount = investmentAmount,
                    modifier = Modifier.weight(1f)
                )
            }

            AllocationLockedCard()
        }
    }
}

@Composable
private fun AllocationDonut(allocation: AssetAllocationDomain) {
    val segments = remember(allocation) {
        listOf(
            allocation.equity to Primary,
            allocation.commodity to Secondary,
            allocation.debt to DebtColor,
            allocation.hybrid to HybridColor
        ).filter { (percentage, _) -> percentage > 0 }
    }
    val highest = remember(allocation) {
        listOf(
            "Equity" to allocation.equity,
            "Commodity" to allocation.commodity,
            "Debt" to allocation.debt,
            "Hybrid" to allocation.hybrid
        ).maxBy { it.second }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 12.dp)) {
        DonutChart(segments = segments, size = 80.dp, strokeWidth = 14.dp)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${highest.second.toInt()}%",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                text = highest.first,
                fontSize = 8.sp,
                lineHeight = 10.sp,
                color = titleColor
            )
        }
    }
}

@Composable
private fun DonutChart(segments: List<Pair<Double, Color>>, size: Dp, strokeWidth: Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val total = segments.sumOf { it.first }
        if (total <= 0.0) return@Canvas

        val stroke = strokeWidth.toPx()
        val arcSize = Size(this.size.width - stroke, this.size.height - stroke)
        val topLeft = Offset(stroke / 2, stroke / 2)
        var startAngle = -90f

        segments.forEach { (value, color) ->
            val sweep = (value / total * 360.0).toFloat()
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke)
            )
            startAngle += sweep
        }
    }
}

@Composable
private fun BundleTag(text: String) {
    Surface(
        shape = LocalShapes.current.circle,
        color = GreyBox,
        border = BorderStroke(1.dp, BoxBorder)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = tinyLabel.copy(fontWeight = FontWeight.Normal),
            color = Color.Black
        )
    }
}

@Composable
private fun AssetLegend(
    allocation: AssetAllocationDomain,
    investmentAmount: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier=modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (allocation.equity > 0) LegendItem("Equity", allocation.equity, investmentAmount, Primary)
        if (allocation.commodity > 0) LegendItem("Commodities", allocation.commodity, investmentAmount, Secondary)
        if (allocation.debt > 0) LegendItem("Debt", allocation.debt, investmentAmount, DebtColor)
        if (allocation.hybrid > 0) LegendItem("Hybrid", allocation.hybrid, investmentAmount, HybridColor)
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = GrayBackGround
        )
        Row(
            modifier= Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total Bundle Allocation:",
                style = MaterialTheme.typography.bodySmall,
                color = BundleCardSubtitle
            )
            Text(
                text = "100% Balanced",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = appGreen
            )
        }
    }
}

/** An asset class's share: its percentage in bold, then what it comes to of the amount entered. */
@Composable
private fun LegendItem(label: String, percentage: Double, investmentAmount: Long, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier.size(12.dp)
                    .clip(RoundedCornerShape(20))
                    .background(color)
            )
            Text(label, style = MaterialTheme.typography.bodySmall, color=Color(0xff334155))
        }
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${percentage.toInt()}%")
                }
                withStyle(SpanStyle(color = BundleCardSubtitle)) {
                    append(" (${shareAmountText(investmentAmount, percentage)})")
                }
            },
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun AllocationLockedCard() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF7E6)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(Res.drawable.lock_icon),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = titleColor
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Allocation is locked as per selected portfolio. You can change individual funds, not the weights.",
                style = titlesStyle.copy(fontSize = 12.sp),
                color = titleColor
            )
        }
    }
}

/**
 * A category's share of the bundle and the funds picked for it. A fund that can't be bought as
 * [purchaseMode] outlines the card in red and is named, with the fix, below it.
 */
@Composable
private fun CategoryDistributionItem(
    category: BundleCategoryDomain,
    investmentAmount: Long,
    purchaseMode: PurchaseMode,
    unsupportedFundNames: List<String>,
    onChangeFundClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val hasError = unsupportedFundNames.isNotEmpty()
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
    ShadowCard(
        modifier = Modifier.fillMaxWidth()
            .then(if (hasError) Modifier.border(1.dp, appRed, shape) else Modifier),
        shape = shape
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FlowRow(
                    itemVerticalAlignment = Alignment.CenterVertically,
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Surface(
                        shape = LocalShapes.current.roundedDp4,
                        color = appGreen.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "${category.slots.size}" + if (category.slots.size > 1) " Funds" else " Fund",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = titlesStyle.copy(fontSize = 10.sp),
                            color = appGreen
                        )
                    }
                    Text(
                        text = "• ${category.allocationPercentage.toInt()}% Alloc",
                        style = titlesStyle.copy(fontSize = 10.sp),
                        color = SlateGray
                    )
                }
                Text(
                    text = category.slots.joinToString(" + ") { it.selectedFund?.name ?: "--" },
                    style = tinyLabel,
                    color = SlateGray,
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                // The category's share of the amount entered.
                Text(
                    text = shareAmountText(investmentAmount, category.allocationPercentage),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Surface(
                    shape = LocalShapes.current.roundedDp8,
                    color = appGreen.copy(alpha = 0.1f),
                    modifier = Modifier
                        .clip(LocalShapes.current.roundedDp8)
                        .clickable(onClick = onChangeFundClick)
                ) {
                    Text(
                        text = "Change",
                        color = appGreen,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }

    unsupportedFundNames.forEach { name ->
        Text(
            text = "$name isn't available for ${purchaseMode.investmentName}. Tap Change to pick another fund.",
            style = titlesStyle.copy(fontSize = 12.sp),
            color = appRed,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
    }
}

/**
 * [percentage] of [total] in rupees: "₹4,750" when it comes to whole rupees, otherwise rounded to
 * one decimal, as in "₹1,187.5".
 */
internal fun shareAmountText(total: Long, percentage: Double): String {
    // The share in tenths of a rupee: total × percentage ÷ 100, times 10.
    val tenths = kotlin.math.round(total * percentage / 10.0).toLong()
    val rupees = tenths / 10
    val fraction = tenths % 10
    return "₹" + formatWithCommas(rupees) + if (fraction != 0L) ".$fraction" else ""
}

@Preview(heightDp = 1000)
@Composable
private fun BundleDetailsScreenPreview() {
    val fund = previewFund
    val bundle = BundleDetailsDomain(
        name = "Aggressive",
        description = "Velvet Long Term Vision",
        assetAllocation = AssetAllocationDomain(equity = 95.0, debt = 0.0, hybrid = 0.0, commodity = 5.0),
        metaData = BundleMetaDataDomain(
            riskLevel = "AGGRESSIVE",
            investmentTime = "7+ YEARS",
            investmentGrowth = "LONG-TERM WEALTH",
            risk = BundleRisk.HIGH
        ),
        categories = listOf(
            BundleCategoryDomain(
                id = "cat1",
                categoryName = "flexi_cap",
                displayName = "Flexi Cap",
                allocationPercentage = 20.0,
                funds = listOf(fund),
                slots = listOf(PortfolioSlotDomain("slot1", 20.0, 1, fund, fund))
            )
        )
    )

    JantaNiveshTheme {
        BundleDetailsScreen(
            state = BundleDetailsUiState(
                bundle = bundle,
                transactionRules = bundle.deriveTransactionRules(),
                investmentAmount = 5000L,
                cartFundCount = 3,
            ),
            onEvent = {}
        )
    }
}
