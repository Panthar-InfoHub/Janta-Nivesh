package org.velvetinvesting.jantanivesh.app.features.bundles.presentation.compose

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.back_arrow
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.velvetinvesting.jantanivesh.app.core.theme.BundleCardSubtitle
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.core.utils.LoadingState
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.unavailableNote
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppSearchBar
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.clearFocusOnTap
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.PurchaseMode
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.MfFilterIds
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.ReturnYearsRateDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.MutualFundSearchResultViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.utils.toTitleCase
import org.velvetinvesting.jantanivesh.app.shared.compose.PaginationEffect
import org.velvetinvesting.jantanivesh.app.shared.compose.PaginationFooter

/** The `GET /mf/funds` tags a bundle category can be listed by; others fall back to a name search. */
private val categoryTags = setOf(
    MfFilterIds.TAG_LARGE_CAP,
    MfFilterIds.TAG_MID_CAP,
    MfFilterIds.TAG_SMALL_CAP,
    MfFilterIds.TAG_FLEXI_CAP,
    MfFilterIds.TAG_MULTI_CAP,
    MfFilterIds.TAG_DEBT,
    MfFilterIds.TAG_OTHERS
)

/**
 * The full fund list for one bundle category, to pick a fund the bundle doesn't offer.
 *
 * The list is [MutualFundSearchResultViewModel], whose search text is fixed when it is created, so
 * each submitted query gets its own instance, keyed by the query. A category the fund list has a
 * tag for is filtered by that tag; one it hasn't — gold, large & mid cap — is searched for by name
 * until the user types a query of their own.
 *
 * A daily SIP applies the fund list's daily ₹10 filter; the other modes load without an amount
 * filter.
 */
@Composable
fun ExploreCategoryFundScreenRoot(
    categoryName: String,
    categoryTitle: String,
    purchaseMode: PurchaseMode,
    /** Funds held by the category's other slots, which this slot can't take. */
    unavailableFundIds: Set<String>,
    /** Funds whose details show they can't be bought as [purchaseMode]; listed but not pickable. */
    unselectableFundIds: Set<String>,
    selectedFundId: String?,
    /** A tapped fund's details are loading. */
    isLoadingFund: Boolean,
    onBackClick: () -> Unit,
    onFundSelected: (MutualFundDomain) -> Unit,
    onSaveClick: () -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var submittedQuery by rememberSaveable { mutableStateOf("") }

    val tag = categoryName.takeIf { it in categoryTags }
    val search = submittedQuery.ifBlank { if (tag == null) categoryTitle else "" }
    val amountType = when (purchaseMode) {
        PurchaseMode.DAILY -> MfFilterIds.AMOUNT_DAILY_10
        PurchaseMode.MONTHLY, PurchaseMode.ONE_TIME -> null
    }

    val viewModel: MutualFundSearchResultViewModel = koinViewModel(
        key = "explore:$categoryName:${purchaseMode.name}:$submittedQuery",
        parameters = { parametersOf(search, tag, null, amountType) }
    )
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    val funds by viewModel.sortedFunds.collectAsStateWithLifecycle()
    val hasNextPage by viewModel.hasNextPage.collectAsStateWithLifecycle()

    ExploreCategoryFundScreen(
        categoryTitle = categoryTitle,
        purchaseMode = purchaseMode,
        loadingState = loadingState,
        funds = funds,
        hasNextPage = hasNextPage,
        searchText = searchText,
        unavailableFundIds = unavailableFundIds,
        unselectableFundIds = unselectableFundIds,
        selectedFundId = selectedFundId,
        isLoadingFund = isLoadingFund,
        onBackClick = onBackClick,
        onFundSelected = onFundSelected,
        onSaveClick = onSaveClick,
        onSearchTextChange = { searchText = it },
        onSearchClick = { submittedQuery = searchText.trim() },
        loadNext = viewModel::loadNext,
        onRetryClick = viewModel::loadFunds
    )
}

@Composable
fun ExploreCategoryFundScreen(
    categoryTitle: String,
    purchaseMode: PurchaseMode,
    loadingState: LoadingState,
    funds: List<MutualFundDomain>,
    hasNextPage: Boolean,
    searchText: String,
    unavailableFundIds: Set<String>,
    unselectableFundIds: Set<String>,
    selectedFundId: String?,
    isLoadingFund: Boolean,
    onBackClick: () -> Unit,
    onFundSelected: (MutualFundDomain) -> Unit,
    onSaveClick: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    loadNext: () -> Unit,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clearFocusOnTap()
    ) {
        ExploreHeader(
            categoryTitle = categoryTitle,
            onBackClick = onBackClick,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp)
        )

        AppSearchBar(
            value = searchText,
            onTextChange = onSearchTextChange,
            onSearchClick = onSearchClick,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (loadingState) {
                is LoadingState.Error -> ErrorScreen(loadingState.error, onRetryClick = onRetryClick)

                LoadingState.Loading -> LoaderScreen()

                LoadingState.Success -> ExploreFundList(
                    funds = funds,
                    categoryTitle = categoryTitle,
                    hasNextPage = hasNextPage,
                    unavailableFundIds = unavailableFundIds,
                    unselectableFundIds = unselectableFundIds,
                    unavailableNote = purchaseMode.unavailableNote(),
                    selectedFundId = selectedFundId,
                    onFundSelected = onFundSelected,
                    loadNext = loadNext
                )
            }
        }

        NextButtonFooter(
            onClick = onSaveClick,
            value = "Save Fund",
            enabled = selectedFundId != null && !isLoadingFund,
            loading = isLoadingFund
        )
    }
}

@Composable
private fun ExploreHeader(
    categoryTitle: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier.size(20.dp).clickable(onClick = onBackClick)
            )
            Text(
                text = "EXPLORE FUNDS",
                style = tinyLabel.copy(fontWeight = FontWeight.Bold),
                color = Secondary
            )
        }

        Column {
            Text(
                text = "Explore $categoryTitle Funds",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Pick any fund from the full $categoryTitle list",
                style = MaterialTheme.typography.bodySmall,
                color = BundleCardSubtitle
            )
        }
    }
}

@Composable
private fun ExploreFundList(
    funds: List<MutualFundDomain>,
    categoryTitle: String,
    hasNextPage: Boolean,
    unavailableFundIds: Set<String>,
    unselectableFundIds: Set<String>,
    unavailableNote: String,
    selectedFundId: String?,
    onFundSelected: (MutualFundDomain) -> Unit,
    loadNext: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    // A fund another slot of the category holds can't be picked for this one, so it isn't listed.
    val availableFunds = funds.filter { it.id !in unavailableFundIds }

    PaginationEffect(lazyListState = lazyListState, onLoadMore = loadNext)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            FundSectionHeader(
                title = "$categoryTitle funds",
                subtitle = "Showing ${availableFunds.size} funds. You can choose any one of them.",
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        items(availableFunds, key = { it.id }) { fund ->
            val name = fund.name.toTitleCase()
            // Known only once the fund has been tapped and its details fetched.
            val isSelectable = fund.id !in unselectableFundIds
            FundOptionCard(
                name = fundDisplayName(name),
                subtitle = if (isSelectable) fund.subtitle(name) else unavailableNote,
                subtitleColor = if (isSelectable) BundleCardSubtitle else appRed,
                enabled = isSelectable,
                iconUrl = fund.icon,
                return1Y = fund.returnYearsRate.year1,
                return3Y = fund.returnYearsRate.year3,
                return5Y = fund.returnYearsRate.year5,
                isSelected = fund.id == selectedFundId,
                onSelect = { onFundSelected(fund) }
            )
        }

        item {
            PaginationFooter(hasNextPage = hasNextPage)
        }
    }
}

/** "Direct • Growth" when the name says so, otherwise the fund's category. */
private fun MutualFundDomain.subtitle(titleCasedName: String): String =
    fundPlanLabel(titleCasedName) ?: category

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun ExploreCategoryFundScreenPreview() {
    JantaNiveshTheme {
        ExploreCategoryFundScreen(
            categoryTitle = "Flexi Cap",
            purchaseMode = PurchaseMode.DAILY,
            loadingState = LoadingState.Success,
            funds = listOf(
                MutualFundDomain(
                    id = "1",
                    name = "PARAG PARIKH FLEXI CAP FUND - DIRECT PLAN - GROWTH",
                    icon = "",
                    category = "Flexi-cap Fund",
                    riskText = "Very High",
                    type = "Equity",
                    returnYearsRate = ReturnYearsRateDomain(
                        month3 = 1.1, month6 = -3.3, year1 = -1.5, year3 = 14.0, year5 = 13.3
                    )
                ),
                MutualFundDomain(
                    id = "2",
                    name = "HDFC FLEXI CAP FUND - DIRECT PLAN - GROWTH OPTION",
                    icon = "",
                    category = "Flexi-cap Fund",
                    riskText = "Very High",
                    type = "Equity",
                    returnYearsRate = ReturnYearsRateDomain(
                        month3 = 4.3, month6 = -1.7, year1 = 2.7, year3 = 17.1, year5 = 17.9
                    )
                ),
                MutualFundDomain(
                    id = "3",
                    name = "KOTAK FLEXICAP FUND",
                    icon = "",
                    category = "Flexi-cap Fund",
                    returnYearsRate = ReturnYearsRateDomain(
                        month3 = 2.0, month6 = 1.2, year1 = 12.9, year3 = 18.1, year5 = null
                    )
                )
            ),
            hasNextPage = false,
            searchText = "",
            unavailableFundIds = setOf("2"),
            unselectableFundIds = setOf("1"),
            selectedFundId = "3",
            isLoadingFund = false,
            onBackClick = {},
            onFundSelected = {},
            onSaveClick = {},
            onSearchTextChange = {},
            onSearchClick = {},
            loadNext = {},
            onRetryClick = {}
        )
    }
}
