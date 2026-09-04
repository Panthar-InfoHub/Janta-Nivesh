package org.velvetinvesting.jantanivesh.app.features.profile.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.check_circle_outline_icon
import jantanivesh.shared.generated.resources.front_arrow_icon
import jantanivesh.shared.generated.resources.ic_cross_circled
import jantanivesh.shared.generated.resources.icon_clock
import jantanivesh.shared.generated.resources.icon_cross
import jantanivesh.shared.generated.resources.tick_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Border
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.IconBackgroundBlue
import org.velvetinvesting.jantanivesh.app.core.theme.IconSize
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.ProfileGreen
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.bgColor3
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppSearchBar
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
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

            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(bottom = Spacing.dp24),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp20)
            ) {
                state.transactionGroups.forEach { group ->
                    item {
                        Text(
                            text = group.dateHeader,
                            style = MaterialTheme.typography.labelSmall,
                            color = GreyText,
                            modifier = Modifier.padding(horizontal = Spacing.dp16)
                        )
                    }

                    items(group.transactions, key = { it.id }) { transaction ->
                        TransactionItem(
                            item = transaction,
                            onClick = { /* Handle click */ },
                            modifier = Modifier.padding(horizontal = Spacing.dp16)
                        )
                    }
                }

                if (state.transactionGroups.isEmpty() && !state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No more transactions",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GreyText
                            )
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow(LocalShapes.current.roundedDp12)
            .clip(LocalShapes.current.roundedDp12)
            .background(White)
            .clickable(onClick = onClick)
            .padding(Spacing.dp16)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
        ) {
            TransactionIcon(
                type = item.type,
                title = item.title
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.amount,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.End
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = GreyText
                    )
                    Text(
                        text = item.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = GreyText
                    )
                }

                Box(modifier = Modifier.height(Spacing.dp12))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusTag(status = item.status)
                    Icon(
                        painter = painterResource(Res.drawable.front_arrow_icon),
                        contentDescription = null,
                        modifier = Modifier.size(Spacing.dp12),
                        tint = FilterChipUnselected
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionIcon(
    type: TransactionType,
    title: String
) {
    val backgroundColor = if (type == TransactionType.MUTUAL_FUND) IconBackgroundBlue else Color(0xFFEEF2FF)
    val textColor = Primary
    val text = if (type == TransactionType.MUTUAL_FUND) {
        title.take(1).uppercase()
    } else {
        "BF" // As seen in image, maybe Bank Fixed?
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(Spacing.dp8))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = textColor
        )
    }
}

@Composable
private fun StatusTag(status: TransactionStatus) {
    val (color, text, icon) = when (status) {
        TransactionStatus.SUCCESSFUL -> Triple(
            ProfileGreen,
            "SUCCESSFUL",
            Res.drawable.check_circle_outline_icon
        )

        TransactionStatus.PENDING -> Triple(bgColor3, "PENDING", Res.drawable.icon_clock)
        TransactionStatus.FAILED -> Triple(appRed, "FAILED", Res.drawable.ic_cross_circled)
    }

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
                        dateHeader = "TODAY",
                        transactions = listOf(
                            TransactionHistoryItem(
                                id = "1",
                                title = "HDFC Small Cap Fund",
                                subtitle = "SIP • Direct Growth",
                                amount = "₹5,000",
                                date = "12 Oct 2023",
                                status = TransactionStatus.SUCCESSFUL,
                                type = TransactionType.MUTUAL_FUND
                            ),
                            TransactionHistoryItem(
                                id = "2",
                                title = "ICICI Pru Bluechip",
                                subtitle = "Lumpsum • Direct Growth",
                                amount = "₹25,000",
                                date = "14 Oct 2023",
                                status = TransactionStatus.PENDING,
                                type = TransactionType.MUTUAL_FUND
                            )
                        )
                    ),
                    TransactionGroup(
                        dateHeader = "YESTERDAY",
                        transactions = listOf(
                            TransactionHistoryItem(
                                id = "3",
                                title = "Axis Midcap Fund",
                                subtitle = "SIP • Direct Growth",
                                amount = "₹3,000",
                                date = "10 Oct 2023",
                                status = TransactionStatus.FAILED,
                                type = TransactionType.MUTUAL_FUND
                            )
                        )
                    )
                )
            ),
            onEvent = {}
        )
    }
}
