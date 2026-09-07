package org.velvetinvesting.jantanivesh.app.features.orders.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.velvetinvesting.jantanivesh.app.core.theme.GrayScreenBackGround
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.ProfileTitleColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.grayColor
import org.velvetinvesting.jantanivesh.app.core.theme.redColor
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.MutualFundIcon
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderDomain
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderFilter
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderPlanType
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderState
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.placedForLabel
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.title
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.toOrderDate
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.typeLabel
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.unitsLabel
import org.velvetinvesting.jantanivesh.app.features.orders.ui.viewmodel.MyOrdersEvent
import org.velvetinvesting.jantanivesh.app.features.orders.ui.viewmodel.MyOrdersUiState
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.EmptyFundScreen
import org.velvetinvesting.jantanivesh.app.shared.compose.PaginationEffect
import org.velvetinvesting.jantanivesh.app.shared.compose.PaginationFooter

@Composable
fun MyOrdersScreen(
    state: MyOrdersUiState,
    onEvent: (MyOrdersEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    PaginationEffect(lazyListState = lazyListState) { onEvent(MyOrdersEvent.LoadNext) }

    Column(modifier = modifier.fillMaxSize().background(White)) {

        MyOrdersHeader(onBack = { onEvent(MyOrdersEvent.OnBackClicked) })

        OrderFilterRow(
            selected = state.selectedFilter,
            onFilterSelected = { onEvent(MyOrdersEvent.OnFilterSelected(it)) }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading && state.orders.isEmpty() ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                state.error != null && state.orders.isEmpty() -> Text(
                    text = state.error,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    color = redColor,
                    modifier = Modifier.align(Alignment.Center).padding(Spacing.dp24)
                )

                state.orders.isEmpty() -> EmptyFundScreen(
                    onBrowseClick = { onEvent(MyOrdersEvent.OnBackClicked) },
                    text = when (state.selectedFilter) {
                        OrderFilter.ALL -> "You haven't placed any orders yet"
                        else -> "No ${state.selectedFilter.title().lowercase()} orders"
                    },
                    buttonText = "Go Back"
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    contentPadding = PaddingValues(
                        start = Spacing.dp16,
                        end = Spacing.dp16,
                        top = Spacing.dp8,
                        bottom = Spacing.dp24
                    ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp12)
                ) {
                    items(state.orders, key = { it.id }) { order ->
                        OrderCard(
                            order = order,
                            onClick = { onEvent(MyOrdersEvent.OnOrderClicked(order)) }
                        )
                    }
                    item { PaginationFooter(hasNextPage = state.hasNextPage) }
                }
            }
        }
    }
}

@Composable
private fun MyOrdersHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Spacing.dp24, end = Spacing.dp16, bottom = Spacing.dp8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppBackButton(onClick = onBack)
        Text(
            text = "My Orders",
            fontFamily = InterFontFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = ProfileTitleColor
        )
    }
}

@Composable
private fun OrderFilterRow(
    selected: OrderFilter,
    onFilterSelected: (OrderFilter) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.dp8),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp10),
        contentPadding = PaddingValues(horizontal = Spacing.dp16)
    ) {
        items(OrderFilter.entries) { filter ->
            OrderFilterChip(
                title = filter.title(),
                isSelected = filter == selected,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

/**
 * The shared [org.velvetinvesting.jantanivesh.app.features.core.ui.composables.FilterChip] puts a
 * dismiss cross on the selected chip, which reads as "remove this filter". These four are one
 * exclusive choice with no empty state to fall back to, so the selection is shown by fill alone.
 */
@Composable
private fun OrderFilterChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = title,
        fontFamily = InterFontFamily,
        fontSize = 13.sp,
        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
        color = if (isSelected) White else grayColor,
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) Primary else GrayScreenBackGround)
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.dp20, vertical = Spacing.dp6)
    )
}

@Composable
private fun OrderCard(
    order: OrderDomain,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(Spacing.dp16), radius = Spacing.dp12)
            .clip(RoundedCornerShape(Spacing.dp16))
            .background(White)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(Spacing.dp16)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OrderFundIcon(order)
            Spacer(Modifier.size(Spacing.dp12))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.fundName,
                    fontFamily = InterFontFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProfileTitleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(Spacing.dp2))
                Text(
                    text = "${order.typeLabel()} • ${order.placedForLabel()}".withInterRupee(),
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.size(Spacing.dp8))
            OrderStatusPill(order.state, order.stateLabel)
        }

        Spacer(Modifier.height(Spacing.dp14))
        HorizontalDivider(thickness = 1.dp, color = GrayScreenBackGround)
        Spacer(Modifier.height(Spacing.dp10))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Order placed: ${order.createdAt.toOrderDate().ifBlank { "--" }}",
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                color = titleColor
            )
            Text(
                text = "Units: ${order.unitsLabel()}",
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                color = titleColor
            )
        }
    }
}

@Composable
internal fun OrderFundIcon(order: OrderDomain, size: androidx.compose.ui.unit.Dp = Spacing.dp40) {
    val fallback: @Composable () -> Unit = {
        MutualFundIcon(schemeName = order.fundName, size = size, cornerRadius = Spacing.dp12)
    }

    SubcomposeAsyncImage(
        model = order.fundIconUrl,
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(Spacing.dp12))
            .background(White),
        loading = { fallback() },
        error = { fallback() },
        success = { SubcomposeAsyncImageContent() }
    )
}

@Composable
internal fun OrderStatusPill(state: OrderState, stateLabel: String) {
    val color = state.color()
    Text(
        text = state.label(stateLabel),
        fontFamily = InterFontFamily,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = Spacing.dp10, vertical = Spacing.dp4)
    )
}

/** Pending sits between the two outcomes, so it takes the warm middle rather than a second red. */
internal val PendingAmber = Color(0xffF97316)
internal val CompletedGreen = Color(0xff16A34A)

internal fun OrderState.color(): Color = when (this) {
    OrderState.SUCCESSFUL -> CompletedGreen
    OrderState.PENDING -> PendingAmber
    OrderState.FAILED -> redColor
    OrderState.CANCELLED -> grayColor
    OrderState.UNKNOWN -> grayColor
}

/** An unrecognised state keeps the server's own wording rather than being called "unknown". */
internal fun OrderState.label(stateLabel: String): String = when (this) {
    OrderState.SUCCESSFUL -> "COMPLETE"
    OrderState.PENDING -> "PENDING"
    OrderState.FAILED -> "FAILED"
    OrderState.CANCELLED -> "CANCELLED"
    OrderState.UNKNOWN -> stateLabel.uppercase().ifBlank { "UNKNOWN" }
}

@Preview
@Composable
fun MyOrdersScreenPreview() {
    JantaNiveshTheme {
        MyOrdersScreen(
            state = MyOrdersUiState(
                orders = listOf(
                    previewOrder(
                        id = "1",
                        state = OrderState.PENDING,
                        planType = OrderPlanType.PURCHASE,
                        amount = 5000.0
                    ),
                    previewOrder(
                        id = "2",
                        state = OrderState.SUCCESSFUL,
                        planType = OrderPlanType.PURCHASE,
                        amount = 2000.0,
                        systematic = true,
                        allottedUnits = 45.23
                    ),
                    previewOrder(
                        id = "3",
                        state = OrderState.FAILED,
                        planType = OrderPlanType.REDEMPTION,
                        units = 2.311
                    )
                )
            ),
            onEvent = {}
        )
    }
}

internal fun previewOrder(
    id: String = "1",
    state: OrderState = OrderState.PENDING,
    planType: OrderPlanType = OrderPlanType.PURCHASE,
    amount: Double? = null,
    units: Double? = null,
    systematic: Boolean = false,
    allottedUnits: Double? = null
) = OrderDomain(
    id = id,
    planType = planType,
    state = state,
    stateLabel = state.name,
    fundName = "Aditya Birla Sun Life Gold Fund-Growth",
    fundIconUrl = "",
    fundCategory = "Debt • Growth",
    mfProductId = "cmt839yv8004e83ri5ttw9efb",
    isin = "INF209K01PF4",
    folioNumber = "1051586674",
    orderId = "mfp_4be241a208234c329c32a2887c7961c8",
    transactionId = "8",
    amount = amount,
    units = units,
    systematic = systematic,
    frequency = if (systematic) "MONTHLY" else "",
    installmentDay = null,
    numberOfInstallments = null,
    remainingInstallments = null,
    allottedUnits = allottedUnits,
    allottedNavDate = "",
    purchasedAmount = null,
    purchasedPrice = null,
    latestNav = 44.4605,
    paymentMethod = "NET_BANKING",
    paymentSource = "",
    switchToScheme = "",
    reason = if (state == OrderState.FAILED) "Payment was declined by your bank" else "",
    scheduledOn = "2026-09-07T00:00:00.000Z",
    tradedOn = "",
    createdAt = "2026-09-01T16:02:00.885Z",
    submittedAt = "2026-09-01T16:02:27.000Z",
    succeededAt = if (state == OrderState.SUCCESSFUL) "2026-09-03T09:00:00.000Z" else "",
    failedAt = if (state == OrderState.FAILED) "2026-09-02T09:00:00.000Z" else ""
)
