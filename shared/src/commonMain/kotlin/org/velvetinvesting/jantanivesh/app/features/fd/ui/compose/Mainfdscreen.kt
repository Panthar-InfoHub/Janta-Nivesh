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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.active_count
import jantanivesh.shared.generated.resources.best_rate
import jantanivesh.shared.generated.resources.explore_fds
import jantanivesh.shared.generated.resources.fd_completion_icon
import jantanivesh.shared.generated.resources.fixed_deposits
import jantanivesh.shared.generated.resources.interest_rate
import jantanivesh.shared.generated.resources.invested
import jantanivesh.shared.generated.resources.loading
import jantanivesh.shared.generated.resources.per_annum
import jantanivesh.shared.generated.resources.tenure
import jantanivesh.shared.generated.resources.your_fds
import jantanivesh.shared.generated.resources.arrow_front_icon
import jantanivesh.shared.generated.resources.filter_icon
import jantanivesh.shared.generated.resources.search_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.FilterChipUnselected
import org.velvetinvesting.jantanivesh.app.core.theme.GreyText
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.PreviewBackground
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.SelectTenureCardColor
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxBorder
import org.velvetinvesting.jantanivesh.app.core.theme.SelectedBoxColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppBackButton
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextField
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextFieldDefaults
import org.velvetinvesting.jantanivesh.app.features.core.composables.AppTextFieldStyle
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FixedDepositDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.RiskLevel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FixedDepositsEvent
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FixedDepositsUiState

@Composable
fun FixedDepositsScreen(
    state: FixedDepositsUiState,
    onEvent: (FixedDepositsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize().padding(horizontal = Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(Spacing.dp16)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AppBackButton(onClick = { onEvent(FixedDepositsEvent.OnBackClicked) })

            Text(
                text = stringResource(Res.string.fixed_deposits),
                style = MaterialTheme.typography.headlineMedium,
                color = Primary
            )

            IconButton(
                onClick = { TODO() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.fd_completion_icon),
                    contentDescription = "Go Back",
                    tint = SelectedBoxBorder,
                    modifier = Modifier.size(Spacing.dp20)
                )
            }
        }
        AppTextField(
            value = state.searchQuery,
            onValueChange = { onEvent(FixedDepositsEvent.OnSearchQueryChanged(it)) },
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
                    modifier = Modifier.clickable(onClick = { onEvent(FixedDepositsEvent.OnFilterMenuClicked) }),
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
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.dp16)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.dp2),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(Spacing.dp24))
                    .background(White)
                    .padding(Spacing.dp16)
            ) {
                Text(
                    text = stringResource(Res.string.best_rate),
                    style = MaterialTheme.typography.titleSmall,
                    color = GreyText
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = state.bestRate,
                        style = MaterialTheme.typography.headlineMedium,
                        color = SelectedBoxBorder
                    )
                    Text(
                        text = " " + stringResource(Res.string.per_annum),
                        style = MaterialTheme.typography.titleSmall,
                        color = GreyText,
                    )
                }
                Text(
                    text = state.bestRateBank,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Primary
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.dp2),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(Spacing.dp24))
                    .background(White)
                    .padding(Spacing.dp14)
            ) {
                Text(
                    text = stringResource(Res.string.your_fds),
                    style = MaterialTheme.typography.titleSmall,
                    color = GreyText
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = state.activeFdsCount.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Primary
                    )
                    Text(
                        text = " " + stringResource(Res.string.active_count),
                        style = MaterialTheme.typography.titleSmall,
                        color = GreyText,
                    )
                }
                Text(
                    text = "${state.totalInvested} " + stringResource(Res.string.invested),
                    style = MaterialTheme.typography.titleMedium,
                    color = GreyText
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.explore_fds),
                style = MaterialTheme.typography.headlineSmall,
            )
            Icon(
                painter = painterResource(Res.drawable.arrow_front_icon),
                contentDescription = "Explore More",
                tint = SelectedBoxBorder,
                modifier = Modifier
                    .size(Spacing.dp22)
                    .clickable { onEvent(FixedDepositsEvent.OnExploreFdsArrowClicked) }
            )
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
            items(state.fdList) { fdItem ->
                FdListItem(
                    item = fdItem,
                    onClick = { onEvent(FixedDepositsEvent.OnFdItemClicked(fdItem)) }
                )
            }
        }
    }
}


@Composable
private fun FdListItem(
    item: FixedDepositDomain,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.dp24))
            .background(White)
            .clickable { onClick() }
            .padding(Spacing.dp16)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.dp12),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(Spacing.dp40)
                        .clip(CircleShape)
                        .background(SelectedBoxColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.bankLogoUrl.isNotEmpty()) {
                        AsyncImage(
                            model = item.bankLogoUrl, contentDescription = "Bank Logo",
                            modifier = Modifier
                                .size(Spacing.dp40)
                                .clip(RoundedCornerShape(Spacing.dp58))
                                .background(SelectTenureCardColor)
                        )
                    } else {
                        Text(
                            text = item.bankName.take(1) + item.bankName.substringAfter(" ")
                                .take(1),
                            style = MaterialTheme.typography.labelLarge,
                            color = Primary
                        )
                    }
                }
                Column() {
                    Text(
                        text = item.bankName,
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        text = "Min ₹${item.minDeposit}",
                        style = MaterialTheme.typography.titleSmall,
                        color = GreyText
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.interest_rate),
                        style = MaterialTheme.typography.titleSmall,
                        color = GreyText
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${item.baseInterest}%",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SelectedBoxBorder
                        )
                        Text(
                            text = " " + stringResource(Res.string.per_annum),
                            style = MaterialTheme.typography.labelSmall,
                            color = GreyText,
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(Res.string.tenure),
                        style = MaterialTheme.typography.labelSmall,
                        color = GreyText
                    )
                    Text(
                        text = item.tenures.firstOrNull()?.tenureDays?.toString() ?: "N/A",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FixedDepositsScreenPreview() {
    JantaNiveshTheme {
        val dummyData = listOf(
            FixedDepositDomain(
                id = "1",
                bankName = "Bajaj Finance",
                bankLogoUrl = "",
                riskLevel = RiskLevel.MODERATE,
                baseInterest = 7.40,
                minDeposit = 25000,
                tenures = emptyList(),
                bankTag = "BF",
                tags = listOf("Best Rate")
            )
        )
        FixedDepositsScreen(
            state = FixedDepositsUiState(
                fdList = dummyData
            ),
            onEvent = {},
            modifier = Modifier.background(color = PreviewBackground)
        )
    }
}
