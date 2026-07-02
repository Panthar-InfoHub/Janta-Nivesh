package org.velvetinvesting.jantanivesh.app.features.fd.ui.compose

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppSearchBar
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FixedDepositDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.RiskLevel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.ExploreFdEvent
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.ExploreFdUiState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.MutualFundIcon

@Composable
fun ExploreFdScreen(
    state: ExploreFdUiState,
    onEvent: (ExploreFdEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = Spacing.dp16),
    ) {
        BackHeader(
            title = "Fixed Deposits",
            onBack = { onEvent(ExploreFdEvent.OnBackClicked) },
            modifier = Modifier
        )
        AppSearchBar(
            value = state.searchQuery,
            onTextChange = {
                onEvent(
                    ExploreFdEvent.OnSearchQueryChanged(it)
                )
            },
            placeholder = "Search FDs...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.dp16),
            onSearchClick = {
                if (state.searchQuery.isNotBlank()) {
                    onEvent(ExploreFdEvent.OnSearchClick)
                }
            }
        )

        if (state.isLoading) {
            LoaderScreen()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.dp16),
                contentPadding = PaddingValues(vertical = Spacing.dp12)
            ) {
                items(state.fundsList) { fundItem ->
                    FundListItem(
                        item = fundItem,
                        onClick = { onEvent(ExploreFdEvent.OnFundItemClicked(fundItem)) }
                    )
                }
                if (state.isLoadingNext) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Loading more...")
                        }
                    }
                }
                if (state.hasNextPage) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Spacing.dp24),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(Spacing.dp24))
                                    .background(color = White)
                                    .border(1.dp, BoxBorder, RoundedCornerShape(Spacing.dp24))
                                    .clickable { onEvent(ExploreFdEvent.OnLoadMoreClicked) }
                                    .padding(horizontal = Spacing.dp24, vertical = Spacing.dp10)
                            ) {
                                Text(
                                    text = "Load More Funds",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- SUB-COMPONENTS ---
@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Spacing.dp24))
            .background(if (isSelected) Primary else FilterChipUnselected)
            .clickable { onClick() }
            .padding(horizontal = Spacing.dp14, vertical = Spacing.dp8),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) White else Black
        )
    }
}

@Composable
private fun FundListItem(
    item: FixedDepositDomain,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .genericDropShadow(LocalShapes.current.roundedDp12)
            .fillMaxWidth()
            .clip(LocalShapes.current.roundedDp12)
            .background(White)
            .clickable { onClick() }
            .padding(horizontal = Spacing.dp16, vertical = Spacing.dp20),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            SubcomposeAsyncImage(
                model = item.bankLogoUrl,
                contentDescription = "Bank Logo",
                modifier = Modifier.size(Spacing.dp40),

                loading = {
                    MutualFundIcon(
                        schemeName = item.bankName,
                        size = Spacing.dp40,
                        cornerRadius = Spacing.dp8
                    )
                },

                error = {
                    MutualFundIcon(
                        schemeName = item.bankName,
                        size = Spacing.dp40,
                        cornerRadius = Spacing.dp8
                    )
                },

                success = {
                    SubcomposeAsyncImageContent()
                }
            )
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                Text(
                    text = item.bankName,
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Spacing.dp4))
                        .background(GreyBox)
                        .padding(horizontal = Spacing.dp8, vertical = 2.dp)
                ) {
                    Text(
                        text = item.riskLevel.label,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }

        // Right Side: Returns
        Column(
            modifier = Modifier.weight(0.3f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
        ) {
            Text(
                text = "${item.baseInterest}%",
                style = MaterialTheme.typography.labelLarge,
                color = Primary
            )
            Text(
                text =  "3Y Return",
                style = MaterialTheme.typography.titleSmall,
                color = GreyText
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 350)
@Composable
fun ExploreFdScreenPreview() {
    JantaNiveshTheme {
        val dummyData = listOf(
            FixedDepositDomain(
                id = "1",
                bankName = "SBI Bank",
                bankLogoUrl = "https://picsum.photos/200",
                riskLevel = RiskLevel.LOW,
                baseInterest = 6.39,
                minDeposit = 10000,
                tenures = emptyList(),
                bankTag = "S",
                tags = listOf("Public Bank")
            )
        )
        ExploreFdScreen(
            state = ExploreFdUiState(
                fundsList = dummyData
            ),
            onEvent = {},
        )
    }
}
