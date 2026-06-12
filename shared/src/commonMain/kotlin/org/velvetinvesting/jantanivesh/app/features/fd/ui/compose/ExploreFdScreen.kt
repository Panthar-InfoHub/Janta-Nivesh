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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.search_icon
import org.jetbrains.compose.resources.painterResource
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.BoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.GreyBox
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.PreviewBackground
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.utils.math.toYearsFormatKmp
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextFieldDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppTextFieldStyle
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FixedDepositDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.RiskLevel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.ExploreFdEvent
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.ExploreFdUiState

@Composable
fun ExploreFdScreen(
    pv: PaddingValues,
    state: ExploreFdUiState,
    onEvent: (ExploreFdEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize().statusBarsPadding().padding(top = Spacing.dp8).padding(pv)
            .padding(horizontal = Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp24)
    ) {
        AppTextField(
            value = state.searchQuery,
            onValueChange = { onEvent(ExploreFdEvent.OnSearchQueryChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Search FDs...",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            leadingIcon = {
                Icon(
                    painterResource(Res.drawable.search_icon),
                    contentDescription = "Search",
                    modifier = Modifier.size(Spacing.dp18),
                    tint = GreyText
                )
            },
            style = AppTextFieldStyle(
                shape = AppTextFieldDefaults.style().shape,
                textStyle = AppTextFieldDefaults.style().textStyle,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = FilterChipUnselected,
                    focusedContainerColor = FilterChipUnselected,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )
        )

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Loading...")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
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
            .genericDropShadow(RoundedCornerShape(Spacing.dp24))
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp24))
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
            Box(
                modifier = Modifier
                    .size(Spacing.dp40)
                    .clip(RoundedCornerShape(Spacing.dp8))
                    .background(GreyBox),
                contentAlignment = Alignment.Center
            ) {
                if (item.bankLogoUrl.isNotEmpty()) {
                    AsyncImage(
                        model = item.bankLogoUrl,
                        contentDescription = "Bank Logo",
                        contentScale = ContentScale.FillBounds
                    )
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
                text = (item.tenures.firstOrNull()?.tenureDays?.toYearsFormatKmp() + " Return"),
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
            pv = PaddingValues(vertical = Spacing.dp16),
            state = ExploreFdUiState(
                fundsList = dummyData
            ),
            onEvent = {},
            modifier = Modifier.background(PreviewBackground)
        )
    }
}
