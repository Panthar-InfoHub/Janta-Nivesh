package org.velvetinvesting.jantanivesh.app.features.profile.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.check_circle_outline_icon
import jantanivesh.shared.generated.resources.ic_cross_circled
import jantanivesh.shared.generated.resources.icon_clock
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.IconBackgroundBlue
import org.velvetinvesting.jantanivesh.app.core.theme.IconSize
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.ProfileGreen
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.bgColor3
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppSearchBar
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.MutualFundIcon
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionGroup
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionHistoryItem
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionStatus
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.TransactionType
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.TransactionFilter
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.TransactionHistoryEvent
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.TransactionHistoryUiState

@Composable
fun TransactionHistoryScreen(
    state: TransactionHistoryUiState,
    onEvent: (TransactionHistoryEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = White,
        topBar = {
            Column {
                BackHeader(
                    title = "Transactions History",
                    onBack = { onEvent(TransactionHistoryEvent.OnBackClicked) },
                    modifier = Modifier.padding(horizontal = Spacing.dp16)
                )
                TransactionTabs(
                    selectedTab = state.selectedTab,
                    onTabSelected = { onEvent(TransactionHistoryEvent.OnTabSelected(it)) },
                    modifier = Modifier.padding(horizontal = Spacing.dp16, vertical = Spacing.dp8)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .clearFocusOnTap()
        ) {
            AppSearchBar(
                value = state.searchQuery,
                onTextChange = { onEvent(TransactionHistoryEvent.OnSearchQueryChanged(it)) },
                placeholder = if (state.selectedTab == TransactionType.MUTUAL_FUND) {
                    "Search funds, dates, or amounts..."
                } else {
                    "Search FDs, dates, or amounts..."
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.dp16, vertical = Spacing.dp16),
                onSearchClick = {}
            )

            FilterRow(
                selectedFilter = state.selectedFilter,
                onFilterSelected = { onEvent(TransactionHistoryEvent.OnFilterSelected(it)) },
                modifier = Modifier.padding(bottom = Spacing.dp16)
            )

            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                when {
                    state.isLoading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )

                    state.error != null -> Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = appRed,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = Spacing.dp24)
                    )

                    state.transactionGroups.isEmpty() -> Text(
                        text = "No transactions to show",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GreyText,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = Spacing.dp24),
                        verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
                    ) {
                        state.transactionGroups.forEach { group ->
                            item(key = "header_${group.header}") {
                                Text(
                                    text = group.header,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GreyText,
                                    modifier = Modifier.padding(horizontal = Spacing.dp16)
                                )
                            }

                            items(group.transactions, key = { it.id }) { transaction ->
                                TransactionItem(
                                    item = transaction,
                                    modifier = Modifier.padding(horizontal = Spacing.dp16)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionTabs(
    selectedTab: TransactionType,
    onTabSelected: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(Spacing.dp8))
            .background(FilterChipUnselected.copy(alpha = 0.5f))
            .padding(Spacing.dp4),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp4)
    ) {
        TabItem(
            title = "Mutual Funds",
            isSelected = selectedTab == TransactionType.MUTUAL_FUND,
            onClick = { onTabSelected(TransactionType.MUTUAL_FUND) },
            modifier = Modifier.weight(1f)
        )
        TabItem(
            title = "Fixed Deposits",
            isSelected = selectedTab == TransactionType.FIXED_DEPOSIT,
            onClick = { onTabSelected(TransactionType.FIXED_DEPOSIT) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(Spacing.dp6))
            .background(if (isSelected) White else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isSelected) Primary else GreyText
        )
    }
}

@Composable
private fun FilterRow(
    selectedFilter: TransactionFilter,
    onFilterSelected: (TransactionFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = Spacing.dp16),
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)
    ) {
        items(TransactionFilter.entries) { filter ->
            TransactionFilterChip(
                text = filter.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                isSelected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

@Composable
private fun TransactionFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) Primary else FilterChipUnselected.copy(alpha = 0.5f))
            .clickable(onClick = onClick)
            .padding(horizontal = Spacing.dp20, vertical = Spacing.dp8),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) White else GreyText
        )
    }
}

@Composable
private fun TransactionItem(
    item: TransactionHistoryItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow(LocalShapes.current.roundedDp12)
            .clip(LocalShapes.current.roundedDp12)
            .background(White)
            .padding(Spacing.dp16)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
        ) {
            TransactionIcon(iconUrl = item.iconUrl, title = item.title)

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.dp8),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.amount.withInterRupee(),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.End
                    )
                }

                // A holding carries neither a subtitle-worthy date nor a state, so this row is
                // laid out around whatever is actually present.
                if (item.subtitle.isNotBlank() || item.date.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp2),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = GreyText,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (item.date.isNotBlank()) {
                            Text(
                                text = item.date,
                                style = MaterialTheme.typography.bodySmall,
                                color = GreyText
                            )
                        }
                    }
                }

                if (item.status != null) {
                    Box(modifier = Modifier.height(Spacing.dp12))
                    StatusTag(status = item.status, label = item.statusLabel)
                }
            }
        }
    }
}

/** The scheme or issuer logo when the payload has one, and its initials when it does not. */
@Composable
private fun TransactionIcon(iconUrl: String, title: String) {
    val fallback: @Composable () -> Unit = {
        MutualFundIcon(
            schemeName = title,
            size = 40.dp,
            cornerRadius = Spacing.dp8,
            backgroundColor = IconBackgroundBlue,
            textColor = Primary
        )
    }

    if (iconUrl.isBlank()) {
        fallback()
        return
    }

    SubcomposeAsyncImage(
        model = iconUrl,
        contentDescription = null,
        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(Spacing.dp8)),
        loading = { fallback() },
        error = { fallback() },
        success = { SubcomposeAsyncImageContent() }
    )
}

@Composable
private fun StatusTag(status: TransactionStatus, label: String) {
    val (color, icon) = when (status) {
        TransactionStatus.SUCCESSFUL -> ProfileGreen to Res.drawable.check_circle_outline_icon
        TransactionStatus.PENDING -> bgColor3 to Res.drawable.icon_clock
        TransactionStatus.FAILED -> appRed to Res.drawable.ic_cross_circled
    }
    // The source's own wording ("Payment Pending", "FD Created") says more than the three
    // buckets it is coloured by, so it is what the tag reads.
    val text = label.ifBlank { status.name }.uppercase()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.dp4),
        modifier = Modifier.clip(LocalShapes.current.circle)
            .background(color.copy(alpha = 0.1f))
            .padding(vertical = Spacing.dp4, horizontal = Spacing.dp6)
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(IconSize.dp12),
            tint = color
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            ),
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionHistoryScreenPreview() {
    JantaNiveshTheme {
        TransactionHistoryScreen(
            state = TransactionHistoryUiState(
                transactionGroups = listOf(
                    TransactionGroup(
                        header = "PENDING ORDERS",
                        transactions = listOf(
                            TransactionHistoryItem(
                                id = "order_1",
                                title = "SBI BLUECHIP FUND - DIRECT PLAN - GROWTH",
                                subtitle = "Sip • SBI Mutual Fund",
                                amount = "₹5,000",
                                date = "25 Oct 2023",
                                status = TransactionStatus.PENDING,
                                statusLabel = "Payment Pending",
                                type = TransactionType.MUTUAL_FUND,
                                iconUrl = ""
                            )
                        )
                    ),
                    TransactionGroup(
                        header = "HOLDINGS",
                        transactions = listOf(
                            TransactionHistoryItem(
                                id = "mf_1",
                                title = "HDFC Small Cap Fund",
                                subtitle = "Equity • Folio 12345678",
                                amount = "₹25,000",
                                date = "",
                                status = null,
                                statusLabel = "",
                                type = TransactionType.MUTUAL_FUND,
                                iconUrl = ""
                            )
                        )
                    )
                )
            ),
            onEvent = {}
        )
    }
}
