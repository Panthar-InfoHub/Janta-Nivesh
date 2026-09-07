package org.velvetinvesting.jantanivesh.app.features.orders.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.building_icon
import jantanivesh.shared.generated.resources.check_circle_outline_icon
import jantanivesh.shared.generated.resources.headphone
import jantanivesh.shared.generated.resources.ic_graph
import jantanivesh.shared.generated.resources.ic_ruppee_filled
import jantanivesh.shared.generated.resources.icon_clock
import jantanivesh.shared.generated.resources.icon_warning
import jantanivesh.shared.generated.resources.receipt_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.GrayScreenBackGround
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LightGrayBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.ProfileTitleColor
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.grayColor
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderDomain
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderPlanType
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderState
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.asMoney
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.asNav
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.asUnits
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.toOrderDate
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.toOrderDateTime
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.typeLabel

/** Shown wherever the payload reports nothing, rather than a zero that would read as a fact. */
private const val ABSENT = "--"

/**
 * One order in full.
 *
 * There is no per-order endpoint, so everything here comes off the route — see
 * [org.velvetinvesting.jantanivesh.app.features.orders.domain.model.toDetailsRoute]. Which of the
 * four layouts the designs call for is decided here rather than by the caller: a redemption is
 * about where the money goes, while a purchase is about what was bought, and within a purchase
 * the completed, pending and failed states differ in what they can honestly show.
 */
@Composable
fun OrderDetailsScreen(
    order: OrderDomain,
    onBack: () -> Unit,
    onRetryOrder: () -> Unit,
    onDownloadReceipt: () -> Unit,
    onNeedHelp: () -> Unit,
    /** The account a redemption pays out to. Blank hides the card — see `Route.OrderDetails`. */
    payoutAccount: String = "",
    modifier: Modifier = Modifier
) {
    val isRedemption = order.planType == OrderPlanType.REDEMPTION

    Column(modifier = modifier.fillMaxSize().background(White)) {

        OrderDetailsHeader(onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.dp16),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            StatusBanner(order)

            if (isRedemption) {
                RedemptionSummaryCard(order)
            } else {
                InvestmentDetailsCard(order)
                TransactionDetailsCard(order)
            }

            OrderTimelineCard(order)

            if (isRedemption && payoutAccount.isNotBlank()) {
                PayoutAccountCard(payoutAccount)
            }

            Spacer(Modifier.height(Spacing.dp8))
        }

//        OrderDetailsFooter(
//            order = order,
//            isRedemption = isRedemption,
//            onRetryOrder = onRetryOrder,
//            onDownloadReceipt = onDownloadReceipt,
//            onNeedHelp = onNeedHelp
//        )
    }
}

@Composable
private fun OrderDetailsHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(start = Spacing.dp24, end = Spacing.dp16, bottom = Spacing.dp8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppBackButton(onClick = onBack)
        Text(
            text = "Order details",
            fontFamily = InterFontFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = ProfileTitleColor
        )
    }
}

/**
 * The one-line verdict at the top. A failed order explains itself with the gateway's own reason
 * when it gave one, because a generic apology tells the user nothing about what to do next.
 */
@Composable
private fun StatusBanner(order: OrderDomain) {
    val color = order.state.color()

    val (title, message) = when (order.state) {
        OrderState.SUCCESSFUL -> "Completed" to
            "Your order has been successfully processed."

        OrderState.PENDING -> "Order Pending" to
            "Your order is being processed. Expected allotment within 2-3 business days."

        OrderState.FAILED -> "Order Failed" to
            order.reason.ifBlank { "Payment was declined. Please try again or use a different payment method." }

        OrderState.CANCELLED -> "Order Cancelled" to
            order.reason.ifBlank { "This order was cancelled and no money was debited." }

        OrderState.UNKNOWN -> order.stateLabel.ifBlank { "Order placed" } to
            "We'll update this as soon as the exchange confirms your order."
    }

    val icon = when (order.state) {
        OrderState.SUCCESSFUL -> Res.drawable.check_circle_outline_icon
        OrderState.PENDING -> Res.drawable.icon_clock
        else -> Res.drawable.icon_warning
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(color.copy(alpha = 0.05f))
            .padding(Spacing.dp16),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(Spacing.dp20)
        )
        Column {
            Text(
                text = title,
                fontFamily = InterFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(Modifier.height(Spacing.dp2))
            Text(
                text = message,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = color.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun InvestmentDetailsCard(order: OrderDomain) {
    SectionCard(title = "Investment Details", icon = Res.drawable.ic_ruppee_filled) {
        FactRow(
            "Fund Name" to order.fundName,
            "Investment Type" to order.investmentTypeLabel()
        )
        Spacer(Modifier.height(Spacing.dp20))
        FactRow(
            "Amount" to order.amountLabel(),
            "NAV" to order.navLabel()
        )
        Spacer(Modifier.height(Spacing.dp20))
        FactRow(
            "Units Allotted" to order.allottedUnitsLabel(),
            "Folio Number" to order.folioNumber.ifBlank { ABSENT }
        )
    }
}

@Composable
private fun TransactionDetailsCard(order: OrderDomain) {
    // Only the rows the payload can actually fill: a pending order has no allotment date and no
    // payment id, and an empty row labelled "--" adds nothing the reader did not already know.
    val rows = listOfNotNull(
        ("Order Date" to order.createdAt.toOrderDate()).withValue(),
        ("Payment Method" to order.paymentMethodLabel()).withValue(),
        ("Allotment Date" to order.allotmentDateLabel()).withValue()
    )
    val ids = listOfNotNull(
        ("Order ID" to order.orderId).withValue(),
        ("Transaction ID" to order.transactionId).withValue()
    )

    if (rows.isEmpty() && ids.isEmpty()) return

    SectionCard(title = "Transaction Details", icon = Res.drawable.receipt_icon) {
        rows.forEachIndexed { index, (label, value) ->
            if (index > 0) Spacer(Modifier.height(Spacing.dp12))
            LabelledRow(label, value)
        }
        if (rows.isNotEmpty() && ids.isNotEmpty()) {
            Spacer(Modifier.height(Spacing.dp16))
            HorizontalDivider(thickness = 1.dp, color = LightGrayBorder)
            Spacer(Modifier.height(Spacing.dp16))
        }
        ids.forEachIndexed { index, (label, value) ->
            if (index > 0) Spacer(Modifier.height(Spacing.dp12))
            LabelledRow(label, value, monospaceish = true)
        }
    }
}

/**
 * A redemption is money leaving, so the card leads with what is being sold rather than with the
 * NAV and allotment figures a purchase is judged on — those do not exist yet at this point.
 */
@Composable
private fun RedemptionSummaryCard(order: OrderDomain) {
    SectionCard(title = "Summary", icon = null) {
        FactRow(
            "Fund Name" to order.fundName,
            "Amount" to order.amountLabel()
        )
        Spacer(Modifier.height(Spacing.dp20))
        FactRow(
            "Units" to (order.units ?: order.allottedUnits)?.asUnits().orEmpty().ifBlank { ABSENT },
            "Folio Number" to order.folioNumber.ifBlank { ABSENT }
        )
    }
}

@Composable
private fun OrderTimelineCard(order: OrderDomain) {
    val steps = order.timelineSteps()
    if (steps.isEmpty()) return

    SectionCard(title = "Order Timeline", icon = Res.drawable.ic_graph) {
        steps.forEachIndexed { index, step ->
            TimelineRow(step = step, isLast = index == steps.lastIndex)
        }
    }
}

@Composable
private fun PayoutAccountCard(account: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(
                shape = RoundedCornerShape(Spacing.dp12),
            )
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White)
            .border(1.dp, LightGrayBorder, RoundedCornerShape(Spacing.dp12))
            .padding(Spacing.dp16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
    ) {
        Icon(
            painter = painterResource(Res.drawable.building_icon),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(Spacing.dp24)
        )
        Column {
            Text(
                text = "Crediting to",
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                color = titleColor
            )
            Text(
                text = account,
                fontFamily = InterFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ProfileTitleColor
            )
        }
    }
}

@Composable
private fun OrderDetailsFooter(
    order: OrderDomain,
    isRedemption: Boolean,
    onRetryOrder: () -> Unit,
    onDownloadReceipt: () -> Unit,
    onNeedHelp: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(GrayScreenBackGround)
            .padding(horizontal = Spacing.dp16, vertical = Spacing.dp12),
        contentAlignment = Alignment.Center
    ) {
        when {
            // Retrying needs the scheme to place the order against; without it the button would
            // lead nowhere, so it is left off rather than shown broken.
            order.state == OrderState.FAILED && order.mfProductId.isNotBlank() -> AppButton(
                text = "Try Again",
                onClick = onRetryOrder,
                modifier = Modifier.fillMaxWidth()
            )

            isRedemption -> Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(White)
                    .border(1.dp, LightGrayBorder, CircleShape)
                    .clickable(onClick = onNeedHelp)
                    .padding(horizontal = Spacing.dp20, vertical = Spacing.dp10),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.headphone),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(Spacing.dp16)
                )
                Text(
                    text = "Need Help?",
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )
            }

            order.state == OrderState.SUCCESSFUL -> AppButton(
                text = "Download Receipt",
                onClick = onDownloadReceipt,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// region building blocks

@Composable
private fun SectionCard(
    title: String,
    icon: DrawableResource?,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(
                shape = RoundedCornerShape(Spacing.dp16),
            )
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .padding(Spacing.dp20)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp10)
        ) {
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(Spacing.dp18)
                )
            }
            Text(
                text = title,
                fontFamily = InterFontFamily,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ProfileTitleColor
            )
        }
        Spacer(Modifier.height(Spacing.dp20))
        content()
    }
}

/** Two facts side by side, the way the designs pair them. */
@Composable
private fun FactRow(left: Pair<String, String>, right: Pair<String, String>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Fact(left.first, left.second, Modifier.weight(1f))
        Spacer(Modifier.width(Spacing.dp12))
        Fact(right.first, right.second, Modifier.weight(1f))
    }
}

@Composable
private fun Fact(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            fontFamily = InterFontFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.6.sp,
            color = titleColor
        )
        Spacer(Modifier.height(Spacing.dp4))
        Text(
            text = value.ifBlank { ABSENT }.withInterRupee(),
            fontFamily = InterFontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = ProfileTitleColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** Label on the left, value on the right — the transaction-details shape. */
@Composable
private fun LabelledRow(label: String, value: String, monospaceish: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            color = titleColor
        )
        Spacer(Modifier.width(Spacing.dp16))
        Text(
            text = value,
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = ProfileTitleColor,
            textAlign = TextAlign.End,
            maxLines = if (monospaceish) 2 else 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

private enum class StepState { DONE, CURRENT, UPCOMING }

private data class TimelineStep(
    val title: String,
    val subtitle: String,
    val state: StepState
)

@Composable
private fun TimelineRow(step: TimelineStep, isLast: Boolean) {
    val color = when (step.state) {
        StepState.DONE -> CompletedGreen
        StepState.CURRENT -> Secondary
        StepState.UPCOMING -> LightGrayBorder
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(Spacing.dp20)
                    .clip(CircleShape)
                    .background(color.copy(alpha = if (step.state == StepState.UPCOMING) 1f else 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                if (step.state != StepState.UPCOMING) {
                    Box(
                        modifier = Modifier
                            .size(Spacing.dp8)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(Spacing.dp32)
                        .background(LightGrayBorder)
                )
            }
        }

        Spacer(Modifier.width(Spacing.dp12))

        Column(modifier = Modifier.padding(bottom = if (isLast) Spacing.dp0 else Spacing.dp8)) {
            Text(
                text = step.title,
                fontFamily = InterFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (step.state == StepState.UPCOMING) grayColor else ProfileTitleColor
            )
            if (step.subtitle.isNotBlank()) {
                Spacer(Modifier.height(Spacing.dp2))
                Text(
                    text = step.subtitle,
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    color = titleColor
                )
            }
        }
    }
}

// endregion

// region what each state can honestly say

private fun OrderDomain.investmentTypeLabel(): String {
    val type = typeLabel()
    return if (systematic && frequency.isNotBlank()) {
        "$type • ${frequency.lowercase().replaceFirstChar { it.uppercase() }}"
    } else {
        type
    }
}

/** The amount actually put in once the AMC confirms it, and the amount asked for until then. */
private fun OrderDomain.amountLabel(): String =
    (purchasedAmount ?: amount)?.let { "₹${it.asMoney()}" } ?: ABSENT

/**
 * The allotment NAV, which only exists after allotment. Before that the scheme's current NAV is
 * not the one this order will get, so nothing is shown rather than a figure that will change.
 */
private fun OrderDomain.navLabel(): String = purchasedPrice?.asNav()
    ?: latestNav?.takeIf { state == OrderState.SUCCESSFUL }?.asNav()
    ?: ABSENT

private fun OrderDomain.allottedUnitsLabel(): String = when {
    allottedUnits != null -> allottedUnits.asUnits()
    state == OrderState.FAILED -> "Failed"
    else -> ABSENT
}

private fun OrderDomain.paymentMethodLabel(): String =
    paymentMethod.replace('_', ' ').lowercase()
        .split(' ')
        .filter { it.isNotBlank() }
        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

private fun OrderDomain.allotmentDateLabel(): String =
    allottedNavDate.toOrderDate().ifBlank { succeededAt.toOrderDate() }

/**
 * The steps the order has actually been through, plus the ones still ahead of it. A failed order
 * stops where it failed — there is no allotment to be waiting on.
 */
private fun OrderDomain.timelineSteps(): List<TimelineStep> {
    val placed = TimelineStep(
        title = if (planType == OrderPlanType.REDEMPTION) "Request Placed" else "Order Placed",
        subtitle = createdAt.toOrderDateTime(),
        state = StepState.DONE
    )

    if (state == OrderState.FAILED || state == OrderState.CANCELLED) {
        val closedAt = failedAt.toOrderDateTime()
        return listOf(
            placed,
            TimelineStep(
                title = if (state == OrderState.FAILED) "Order Failed" else "Order Cancelled",
                subtitle = closedAt,
                state = StepState.DONE
            )
        )
    }

    val middleTitle =
        if (planType == OrderPlanType.REDEMPTION) "Processed by AMC" else "Payment Confirmed"
    val finalTitle =
        if (planType == OrderPlanType.REDEMPTION) "Payout to Bank" else "Units Allotted"

    val middleDone = submittedAt.isNotBlank()
    val finalDone = succeededAt.isNotBlank() || allottedNavDate.isNotBlank()

    return listOf(
        placed,
        TimelineStep(
            title = middleTitle,
            subtitle = submittedAt.toOrderDateTime().ifBlank { "In progress" },
            state = if (middleDone) StepState.DONE else StepState.CURRENT
        ),
        TimelineStep(
            title = finalTitle,
            subtitle = when {
                succeededAt.isNotBlank() -> succeededAt.toOrderDateTime()
                allottedNavDate.isNotBlank() -> allottedNavDate.toOrderDate()
                else -> "Awaited"
            },
            state = when {
                finalDone -> StepState.DONE
                middleDone -> StepState.CURRENT
                else -> StepState.UPCOMING
            }
        )
    )
}

/** Keeps a label/value pair only when there is a value to show; drops the row otherwise. */
private fun Pair<String, String>.withValue(): Pair<String, String>? =
    takeIf { it.second.isNotBlank() }

// endregion

@Preview
@Composable
fun OrderDetailsCompletedPreview() {
    JantaNiveshTheme {
        OrderDetailsScreen(
            order = previewOrder(state = OrderState.SUCCESSFUL, amount = 5000.0).copy(
                purchasedAmount = 5000.0,
                purchasedPrice = 29.189,
                allottedUnits = 171.29,
                allottedNavDate = "2026-09-03T00:00:00.000Z",
                paymentMethod = "NET_BANKING"
            ),
            onBack = {},
            onRetryOrder = {},
            onDownloadReceipt = {},
            onNeedHelp = {}
        )
    }
}

@Preview
@Composable
fun OrderDetailsPendingPreview() {
    JantaNiveshTheme {
        OrderDetailsScreen(
            order = previewOrder(state = OrderState.PENDING, amount = 5000.0),
            onBack = {},
            onRetryOrder = {},
            onDownloadReceipt = {},
            onNeedHelp = {}
        )
    }
}

@Preview
@Composable
fun OrderDetailsFailedPreview() {
    JantaNiveshTheme {
        OrderDetailsScreen(
            order = previewOrder(state = OrderState.FAILED, amount = 5000.0),
            onBack = {},
            onRetryOrder = {},
            onDownloadReceipt = {},
            onNeedHelp = {}
        )
    }
}

@Preview
@Composable
fun OrderDetailsRedemptionPreview() {
    JantaNiveshTheme {
        OrderDetailsScreen(
            order = previewOrder(
                state = OrderState.PENDING,
                planType = OrderPlanType.REDEMPTION,
                units = 172.41
            ),
            onBack = {},
            onRetryOrder = {},
            onDownloadReceipt = {},
            onNeedHelp = {},
            payoutAccount = "HDFC Bank - XXXX1234"
        )
    }
}
