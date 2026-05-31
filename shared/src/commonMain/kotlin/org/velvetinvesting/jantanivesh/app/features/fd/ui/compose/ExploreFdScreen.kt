package org.velvetinvesting.jantanivesh.app.features.fd.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.dropdown_icon
import jantanivesh.shared.generated.resources.filter_icon
import jantanivesh.shared.generated.resources.load_more_funds
import jantanivesh.shared.generated.resources.loading
import jantanivesh.shared.generated.resources.loading_more
import jantanivesh.shared.generated.resources.nbfc
import jantanivesh.shared.generated.resources.per_annum
import jantanivesh.shared.generated.resources.private_bank
import jantanivesh.shared.generated.resources.public_bank
import jantanivesh.shared.generated.resources.search_funds
import jantanivesh.shared.generated.resources.search_icon
import jantanivesh.shared.generated.resources.top_funds
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.Border
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.PreviewBackground
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextFieldDefaults
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextFieldStyle
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FixedDepositDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.RiskLevel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.ExploreFdEvent
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.ExploreFdUiState
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FixedDepositsEvent

@Composable
fun SearchFundsScreen(
    state: ExploreFdUiState,
    onEvent: (ExploreFdEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
    ) {
        AppTextField(
            value = state.searchQuery,
            onValueChange = { onEvent(ExploreFdEvent.OnSearchQueryChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search FDs...") },
            leadingIcon = {
                Icon(
                    painterResource(Res.drawable.search_icon),
                    contentDescription = "Search",
                    modifier = Modifier.size(Spacing.dp16),
                    tint = GreyText
                )
            },
            trailingIcon = {
                Icon(
                    painterResource(Res.drawable.filter_icon),
                    contentDescription = "Search",
                    modifier = Modifier.clickable(onClick = {  onEvent(ExploreFdEvent.OnFilterMenuClicked)  }),
                    tint = Primary

                )
            },
            style = AppTextFieldStyle(
                shape = AppTextFieldDefaults.style().shape,
                textStyle = AppTextFieldDefaults.style().textStyle,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = FilterChipUnselected,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp12)
        ) {
            FilterChip(
                text = stringResource(Res.string.public_bank),
                isSelected = state.selectedFilter == "Public Bank",
                onClick = { onEvent(ExploreFdEvent.OnFilterChipClicked("Public Bank")) }
            )
            FilterChip(
                text = stringResource(Res.string.nbfc),
                isSelected = state.selectedFilter == "NBFC",
                onClick = { onEvent(ExploreFdEvent.OnFilterChipClicked("NBFC")) }
            )
            FilterChip(
                text = stringResource(Res.string.private_bank),
                isSelected = state.selectedFilter == "Private Bank",
                onClick = { onEvent(ExploreFdEvent.OnFilterChipClicked("Private Bank")) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.dp8)) {
                Text(
                    text = stringResource(Res.string.top_funds),
                    style = MaterialTheme.typography.titleMedium,
                    color = Primary
                )
                Text(
                    text = "(${state.totalFundsCount})",
                    style = MaterialTheme.typography.titleSmall,
                    color = GreyText
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp4),
                modifier = Modifier.clickable { onEvent(ExploreFdEvent.OnSortDropdownClicked) }
            ) {
                Text(
                    text = state.sortOption,
                    style = MaterialTheme.typography.labelMedium,
                    color = SelectedBoxBorder
                )
                Icon(
                    painter = painterResource(Res.drawable.dropdown_icon),
                    contentDescription = "Sort Options",
                    tint = SelectedBoxBorder,
                    modifier = Modifier.size(Spacing.dp8)
                )
            }
        }

        if (state.isLoading) {
             Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                 Text(stringResource(Res.string.loading))
             }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            items(state.fundsList) { fundItem ->
                FundListItem(
                    item = fundItem,
                    onClick = { onEvent(ExploreFdEvent.OnFundItemClicked(fundItem)) }
                )
            }
            if (state.isLoadingNext) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(stringResource(Res.string.loading_more))
                    }
                }
            }
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
                            .padding(horizontal = Spacing.dp32, vertical = Spacing.dp12)
                    ) {
                        Text(
                            text = stringResource(Res.string.load_more_funds),
                            style = MaterialTheme.typography.labelMedium,
                            color = Primary
                        )
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
            .padding(horizontal = Spacing.dp20, vertical = Spacing.dp12),
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp24))
            .background(White)
            .clickable { onClick() }
            .padding(horizontal = Spacing.dp16, vertical = Spacing.dp20)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp16),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(Spacing.dp40)
                        .clip(RoundedCornerShape(Spacing.dp8))
                        .background(GreyBox),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.bankLogoUrl.isNotEmpty()) {
                        AsyncImage(model = item.bankLogoUrl, contentDescription = "Bank Logo")
                    } else {
                        Text(
                            text = item.bankName.take(1),
                            style = MaterialTheme.typography.labelLarge,
                            color = Primary
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp4)) {
                    Text(
                        text = item.bankName,
                        style = MaterialTheme.typography.labelLarge,
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
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(Spacing.dp4)
            ) {
                Text(
                    text = "${item.baseInterest}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = Primary
                )
                Text(
                    text = stringResource(Res.string.per_annum),
                    style = MaterialTheme.typography.titleSmall,
                    color = GreyText
                )
            }
        }
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun SearchFundsScreenPreview() {
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
        SearchFundsScreen(
            state = ExploreFdUiState(
                fundsList = dummyData
            ),
            onEvent = {},
            modifier = Modifier.background(PreviewBackground)
        )
    }
}
