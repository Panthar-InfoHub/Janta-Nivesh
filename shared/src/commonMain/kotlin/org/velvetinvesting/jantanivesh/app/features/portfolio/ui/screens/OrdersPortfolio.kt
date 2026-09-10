package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.redColor
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderDomain
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderFilter
import org.velvetinvesting.jantanivesh.app.features.orders.domain.model.title
import org.velvetinvesting.jantanivesh.app.features.orders.ui.compose.OrderCard
import org.velvetinvesting.jantanivesh.app.features.orders.ui.compose.OrderFilterRow
import org.velvetinvesting.jantanivesh.app.features.orders.ui.viewmodel.MyOrdersUiState
import org.velvetinvesting.jantanivesh.app.shared.compose.PaginationEffect
import org.velvetinvesting.jantanivesh.app.shared.compose.PaginationFooter

/**
 * The Orders tab.
 *
 * The same chips and cards the standalone My Orders screen renders, minus its own header and back
 * button — here the Portfolio header stands above the tab row. Paging and filtering are the
 * portfolio view model's, so the tab stays in step with the screen's other reads.
 */
@Composable
fun OrdersPortfolio(
    state: MyOrdersUiState,
    onFilterSelected: (OrderFilter) -> Unit,
    onOrderClick: (OrderDomain) -> Unit,
    onLoadNext: () -> Unit,
    reload: () -> Unit,
    onBrowseClick: () -> Unit
) {
    val lazyListState = rememberLazyListState()

    PaginationEffect(lazyListState = lazyListState) { onLoadNext() }

    Column(modifier = Modifier.fillMaxSize()) {

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
                    onBrowseClick = onBrowseClick,
                    text = when (state.selectedFilter) {
                        OrderFilter.ALL -> "You haven't placed any orders yet"
                        else -> "No ${state.selectedFilter.title().lowercase()} orders"
                    },
                    buttonText = "Browse SIP"
                )

                else -> PullToRefreshBox(
                    isRefreshing = false,
                    onRefresh = reload
                ) {
                    LazyColumn(
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
                        stickyHeader {
                            OrderFilterRow(
                                selected = state.selectedFilter,
                                onFilterSelected = onFilterSelected
                            )
                        }
                        items(state.orders, key = { it.id }) { order ->
                            OrderCard(order = order, onClick = { onOrderClick(order) })
                        }
                        item { PaginationFooter(hasNextPage = state.hasNextPage) }
                    }
                }
            }
        }
    }
}
