package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.icon_filter
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.MutualFundSearchResultViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SelectedReturnRatePeriod
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.defaultFilters
import org.velvetinvesting.jantanivesh.app.shared.compose.PaginationEffect
import org.velvetinvesting.jantanivesh.app.shared.compose.PaginationFooter
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.ShadowColor
import org.velvetinvesting.jantanivesh.app.core.theme.subHeading
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.LoadingState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppSearchBar
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.FilterChip
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.utils.LabelFilter
import org.velvetinvesting.jantanivesh.app.features.core.utils.MutualFundLabel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.InvestmentFilter
import org.velvetinvesting.jantanivesh.app.features.core.utils.fundfiltersystem.createInitialInvestmentFilter
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.ReturnYearsRateDomain

@Composable
fun MutualFundSearchScreenRoot(
    onBackClick: () -> Unit,
    onFundClick: (String) -> Unit,
    heading: String,
    searchText: String,
    onSearchClick: (String) -> Unit,
    category: String?,
) {

    val viewModel: MutualFundSearchResultViewModel = koinViewModel {
        parametersOf(searchText, category)
    }
    val uiState by viewModel.loadingState.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val sortedFunds by viewModel.sortedFunds.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val showFilterScreen by viewModel.showFilterScreen.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val searchTextState by viewModel.searchText.collectAsStateWithLifecycle()
    val isLoadingNext by viewModel.isLoadingNext.collectAsStateWithLifecycle()
    val hasNextPage by viewModel.hasNextPage.collectAsStateWithLifecycle()

    MutualFundSearchScreenContent(
        uiState = uiState,
        selectedYear = selectedYear,
        sortedFunds = sortedFunds,
        selectedFilter = selectedFilter,
        showFilterScreen = showFilterScreen,
        filterState = filterState,
        searchText = searchTextState,
        isLoadingNext = isLoadingNext,
        hasNextPage = hasNextPage,
        heading = heading,
        onBackClick = onBackClick,
        onFundClick = onFundClick,
        onSearchClick = onSearchClick,
        onRetryClick = { viewModel.loadFunds() },
        loadNext = { viewModel.loadNext() },
        toggleRateYear = { viewModel.cycleReturnRatePeriod() },
        onFilterSelected = { viewModel.onFilterSelected(it) },
        toggleFilterScreen = { viewModel.toggleFilterScreen() },
        onSearchTextChange = { viewModel.onSearchTextChange(it) },
        applyFilter = {
            viewModel.applyFilter(it)
            viewModel.toggleFilterScreen()
        }
    )
}

@Composable
private fun MutualFundSearchScreenContent(
    uiState: LoadingState,
    selectedYear: SelectedReturnRatePeriod,
    sortedFunds: List<MutualFundDomain>,
    selectedFilter: LabelFilter?,
    showFilterScreen: Boolean,
    filterState: InvestmentFilter,
    searchText: String,
    isLoadingNext: Boolean,
    hasNextPage: Boolean,
    heading: String,
    onBackClick: () -> Unit,
    onFundClick: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onRetryClick: () -> Unit,
    loadNext: () -> Unit,
    toggleRateYear: () -> Unit,
    onFilterSelected: (LabelFilter) -> Unit,
    toggleFilterScreen: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    applyFilter: (InvestmentFilter) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize())
        {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                BackHeader(heading, onBack = onBackClick, modifier = Modifier.padding(horizontal = Spacing.dp16))

                Box(
                    modifier = Modifier.weight(1f)
                        .fillMaxSize()
                ) {
                    when (uiState) {
                        is LoadingState.Error -> {
                            ErrorScreen(
                                uiState.error,
                                onRetryClick = onRetryClick
                            )
                        }

                        LoadingState.Loading -> {
                            LoaderScreen()
                        }

                        LoadingState.Success -> {
                            MutualFundSearchScreen(
                                result = sortedFunds,
                                onFundClick = onFundClick,
                                isLoadingNext = isLoadingNext,
                                hasNextPage = hasNextPage,
                                loadNext = loadNext,
                                toggleRateYear = toggleRateYear,
                                selectedYear = selectedYear,
                                selectedFilter = selectedFilter,
                                onFilterSelected = onFilterSelected,
                                toggleFilterScreen = toggleFilterScreen,
                                searchText = searchText,
                                onTextChange = onSearchTextChange,
                                onSearchClick = onSearchClick
                            )
                        }
                    }
                }

            }
            AnimatedVisibility(
                showFilterScreen,
                enter = slideInHorizontally(
                    initialOffsetX = { -it }, // From Left
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                )
            ) {
                InvestmentFilterScreen(
                    appliedFilter = filterState,
                    onClose = {
                        toggleFilterScreen()
                    },
                    onCancelClick = {
                        toggleFilterScreen()
                    },
                    onApplyClick = {
                        applyFilter(it)
                    },
                )
            }

        }
    }
}

@Composable
fun MutualFundSearchScreen(
    result: List<MutualFundDomain>,
    onFundClick: (String) -> Unit,
    selectedYear: SelectedReturnRatePeriod,
    isLoadingNext: Boolean,
    hasNextPage: Boolean,
    loadNext: () -> Unit,
    selectedFilter: LabelFilter?,
    onFilterSelected: (LabelFilter) -> Unit,
    toggleFilterScreen: () -> Unit,
    searchText: String,
    onTextChange: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    toggleRateYear: () -> Unit
) {

    val lazyListState = rememberLazyListState()

    PaginationEffect(lazyListState = lazyListState, onLoadMore = loadNext)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top=4.dp),
        state = lazyListState
    ) {
        item {
            AppSearchBar(
                value = searchText,
                onTextChange = { onTextChange(it) },
                onSearchClick = { onSearchClick(searchText) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
        item{Spacer(Modifier.height(20.dp))}
        item {
            FundFilterRowMF(
                filters = defaultFilters,
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected,
                toggleFilterScreen=toggleFilterScreen
            )
        }

        item{Spacer(Modifier.height(20.dp))}

        item{
            YearRow(
                selectedYear = selectedYear,
//                incrementYear = incrementYear,
//                decrementYear = decrementYear,
                toggleRateYear =toggleRateYear,
                totalFunds = result.size
            )
        }

        item{Spacer(Modifier.height(10.dp))}

        itemsIndexed(result, key = { _, item -> item.id }){idx, fund ->
            MutualFundListCard(onClick = { onFundClick(fund.id) }, fund =fund,selectedYear=selectedYear,
                modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp, horizontal = 16.dp))
            if (idx!=result.size-1){
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 16.dp)
                        .clip(CircleShape)
                        .background(ShadowColor)
                )
            }
        }
        item {
            PaginationFooter(hasNextPage = hasNextPage)
        }

    }

}

@Composable
fun MutualFundListCard(
    onClick: () -> Unit,
    fund: MutualFundDomain,
    modifier: Modifier = Modifier,
    selectedYear: SelectedReturnRatePeriod
) {
    Row(
        modifier=modifier.fillMaxWidth().clickable(
            indication = null,
            interactionSource = remember{ MutableInteractionSource() }
        ) { onClick() },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.size(38.dp)
                .shadow(
                    elevation = 16.dp
                )
                .clip(LocalShapes.current.roundedDp12)
                .background(Color.White),
            model = fund.icon,
            contentDescription = null,
            loading = {
                MutualFundIcon(
                    schemeName = fund.name,
                    size = 38.dp
                )
            },
            error = {
                MutualFundIcon(
                    schemeName = fund.name,
                    size = 38.dp
                )
            },
            success = {
                SubcomposeAsyncImageContent()
            }
        )
        Column(
            modifier=Modifier.weight(1f)
        ) {
            Row{
                Text(
                    text = fund.name,
                    style = subHeading,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            Row{
                Text(
                    text = fund.category +
                            (fund.remark?.let { " • $it" }.orEmpty()) +
                            (fund.riskText?.let { " • $it" }.orEmpty()),

                    style = titlesStyle,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 4.dp)
        ) {

            val rate= when(selectedYear){
                SelectedReturnRatePeriod.SIX_MONTH -> fund.returnYearsRate.month6
                SelectedReturnRatePeriod.THREE_MONTH -> fund.returnYearsRate.month3
                SelectedReturnRatePeriod.ONE_YEAR -> fund.returnYearsRate.year1
                SelectedReturnRatePeriod.THREE_YEAR -> fund.returnYearsRate.year3
            }

            val text= when(selectedYear){
                SelectedReturnRatePeriod.SIX_MONTH -> "6M"
                SelectedReturnRatePeriod.THREE_MONTH -> "3M"
                SelectedReturnRatePeriod.ONE_YEAR -> "1Y"
                SelectedReturnRatePeriod.THREE_YEAR -> "3Y"
            }

            if (rate !=null){
                Text(
                    text= "$rate %",
                    style = subHeading,
                    color = if (rate>0) appGreen else Color.Red
                )
                Text(
                    text= text,
                    style = titlesStyle,
                    color = titleColor,
                )
            }
            else{
                Text(
                    text= "N/A",
                    style = subHeading,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun YearRow(
    selectedYear: SelectedReturnRatePeriod,
//    incrementYear: () -> Unit,
//    decrementYear: () -> Unit,
    totalFunds: Int,
    toggleRateYear: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
        ) {
        Text(
            text = "$totalFunds Funds",
            fontWeight = FontWeight.SemiBold,
            style = titlesStyle,
            color = titleColor
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row {
                Text(
                    text = "<",
                    style = titlesStyle.copy(lineHeight = 0.sp),
                    fontSize = 16.sp,
                    color = titleColor,
//                    modifier = Modifier.clickable { decrementYear() }
                )
                Text(
                    text = " ",
                    style = titlesStyle,
                    color = titleColor
                )
                Text(
                    text = ">",
                    style = titlesStyle.copy(lineHeight = 0.sp),
                    color = titleColor,
                    fontSize = 16.sp,
//                    modifier = Modifier.clickable { incrementYear() }
                )
            }
            Text(
                text = "${selectedYear.displayText} Returns",
                fontWeight = FontWeight.SemiBold,
                style = titlesStyle,
                color = titleColor,
                modifier = Modifier.clickable(onClick = {toggleRateYear()})
            )
        }
    }

}

@Composable
private fun FundFilterRowMF(
    filters: List<LabelFilter>,
    selectedFilter: LabelFilter?,
    onFilterSelected: (LabelFilter) -> Unit,
    toggleFilterScreen: () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            if (selectedFilter is MutualFundLabel.CustomLabel){
                FilterChip(
                    title=selectedFilter.title,
                    isSelected = true,
                    onClick = {
                        onFilterSelected(selectedFilter)
                    }
                )
            }else{
                Box(
                    modifier=Modifier.size(32.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFDEE2F6).copy(0.7f))
                        .clickable { toggleFilterScreen()  },
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(Res.drawable.icon_filter),
                        contentDescription = null,
                        tint= Color.Black,
                        modifier=Modifier.size(20.dp)
                    )
                }
            }
        }
        items(filters) { filter ->
            FilterChip(
                title=filter.title,
                isSelected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

@Preview
@Composable
fun MutualFundSearchScreenPreview() {
    JantaNiveshTheme {
        MutualFundSearchScreenContent(
            uiState = LoadingState.Success,
            selectedYear = SelectedReturnRatePeriod.ONE_YEAR,
            sortedFunds = listOf(
                MutualFundDomain(
                    id = "1",
                    name = "Axis Bluechip Fund",
                    icon = "",
                    category = "Equity",
                    remark = "Growth",
                    riskText = "Very High Risk",
                    type = "Open Ended",
                    returnYearsRate = ReturnYearsRateDomain(
                        month3 = 5.0,
                        month6 = 10.0,
                        year1 = 15.0,
                        year3 = 45.0
                    ),
                    latestNav = "50.5"
                ),
                MutualFundDomain(
                    id = "2",
                    name = "SBI Small Cap Fund",
                    icon = "",
                    category = "Equity",
                    remark = "Growth",
                    riskText = "Very High Risk",
                    type = "Open Ended",
                    returnYearsRate = ReturnYearsRateDomain(
                        month3 = 2.0,
                        month6 = 8.0,
                        year1 = 20.0,
                        year3 = 60.0
                    ),
                    latestNav = "120.3"
                )
            ),
            selectedFilter = null,
            showFilterScreen = false,
            filterState = createInitialInvestmentFilter(),
            searchText = "Axis",
            isLoadingNext = false,
            hasNextPage = true,
            heading = "Search Results",
            onBackClick = {},
            onFundClick = {},
            onSearchClick = {},
            onRetryClick = {},
            loadNext = {},
            toggleRateYear = {},
            onFilterSelected = {},
            toggleFilterScreen = {},
            onSearchTextChange = {},
            applyFilter = {}
        )
    }
}

