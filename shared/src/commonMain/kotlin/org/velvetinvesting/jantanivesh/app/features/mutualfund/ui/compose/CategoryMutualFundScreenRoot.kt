package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.back_arrow
import jantanivesh.shared.generated.resources.cart_icon
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.ShadowColor
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.LoadingState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppSearchBarButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BarHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CategoryMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.ReturnYearsRateDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SelectedReturnRatePeriod
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.CategoryMutualFundViewModel
import org.velvetinvesting.jantanivesh.app.features.search.ui.compose.SearchOverlay
import org.velvetinvesting.jantanivesh.app.features.search.ui.viewmodels.SearchOverlayEffect
import org.velvetinvesting.jantanivesh.app.features.search.ui.viewmodels.SearchOverlayViewModel

@Composable
fun CategoryMutualFundScreenRoot(
    onBackClick: () -> Unit,
    onIconClick: () -> Unit,
    /** The whole fund: the buy screen needs its ISIN and name, not only its product id. */
    onFundClick: (MutualFundDomain) -> Unit,
    onSearchClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onBundleClick:() -> Unit,
    onBundledFundClick: (String) -> Unit,
    onStartSipClick: () -> Unit,
    onBookFdClick: () -> Unit
){

    val viewModel: CategoryMutualFundViewModel = koinViewModel()
    val categories by viewModel.mutualFunds.collectAsStateWithLifecycle()
    val uiState by viewModel.loadingState.collectAsStateWithLifecycle()

    // Kept here rather than in the view model: the overlay is a presentation concern, and
    // surviving configuration changes is all the persistence it needs.
    var showSearchOverlay by rememberSaveable { mutableStateOf(false) }

    val searchViewModel: SearchOverlayViewModel = koinViewModel()
    val searchState by searchViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(searchViewModel.effect) {
        searchViewModel.effect.collect { effect ->
            when (effect) {
                is SearchOverlayEffect.Search -> {
                    // Close before navigating so the overlay is not left standing on the back
                    // stack behind the results screen.
                    showSearchOverlay = false
                    searchViewModel.resetQuery()
                    onSearchClick(effect.query)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ){
        Box(modifier = Modifier.fillMaxSize()) {
            CategoryMutualFundScreenRootContent(
                uiState = uiState,
                categories = categories,
                onBackClick = onBackClick,
                onIconClick = onIconClick,
                onFundClick = onFundClick,
                onSearchBarClick = { showSearchOverlay = true },
                onCategoryClick = onCategoryClick,
                onBundleClick = onBundleClick,
                onBundledFundClick = onBundledFundClick,
                onRetryClick = { viewModel.loadMutualFunds() }
            )

            if (showSearchOverlay) {
                SearchOverlay(
                    state = searchState,
                    handleEvent = searchViewModel::handleEvent,
                    onDismiss = {
                        showSearchOverlay = false
                        searchViewModel.resetQuery()
                    },
                    onStartSipClick = {
                        showSearchOverlay = false
                        onStartSipClick()
                    },
                    onBookFdClick = {
                        showSearchOverlay = false
                        onBookFdClick()
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryMutualFundScreenRootContent(
    uiState: LoadingState,
    categories: List<CategoryMutualFundDomain>,
    onBackClick: () -> Unit,
    onIconClick: () -> Unit,
    onFundClick: (MutualFundDomain) -> Unit,
    onSearchBarClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onBundleClick: () -> Unit,
    onBundledFundClick: (String) -> Unit,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
    ) {
        ScreenHeader(
            onBackClick = { onBackClick() },
            onIconClick = { onIconClick() }
        )
        Box(
            modifier = Modifier.weight(1f)
                .fillMaxSize()
        ){
            when(uiState){
                is LoadingState.Error->{
                    ErrorScreen(uiState.error, onRetryClick = onRetryClick)
                }
                LoadingState.Loading -> {
                    LoaderScreen()
                }

                LoadingState.Success -> {
                    CategoryMutualFundScreen(
                        funds = categories,
                        onCategoryClick = onCategoryClick,
                        onFundClick = {onFundClick(it)},
                        onSearchBarClick = onSearchBarClick,
                        onBundledFundClick = {onBundledFundClick(it)},
                        onBundleClick = {onBundleClick()}
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryMutualFundScreen(
    onCategoryClick: (String) -> Unit,
    onFundClick: (MutualFundDomain) -> Unit,
    onSearchBarClick: () -> Unit,
    funds: List<CategoryMutualFundDomain>,
    onBundledFundClick: (String) -> Unit,
    onBundleClick: () -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = Spacing.dp16),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = Spacing.dp20)
    ) {
        item {
            AppSearchBarButton(
                onClick = onSearchBarClick,
                placeholder = "Search Mutual funds.",
                modifier = Modifier.fillMaxWidth()
            )
        }

        funds.forEach {category->
            item {
                BarHeader(
                    title = category.categoryName,
                    showArrow = true,
                    onArrowClick = {onCategoryClick(category.categorySearchReference)},
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(
                items = category.mutualFunds,
                key = {fund -> category.categorySearchReference + fund.id }
            ) {  fund ->

                Column(
                ) {
                    MutualFundListCard(
                        onClick = { onFundClick(fund) },
                        fund = fund,
                        selectedYear = SelectedReturnRatePeriod.THREE_YEAR,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp )
                            .height(1.dp)
                            .clip(CircleShape)
                            .background(ShadowColor)
                    )
                }
            }
        }
    }

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
private fun ScreenHeader(onIconClick: () -> Unit, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Mutual Funds",
            style = MaterialTheme.typography.headlineSmall,
            color = Primary
        )
        Icon(
            painter = painterResource(Res.drawable.back_arrow),
            contentDescription = null,
            modifier = Modifier.size(22.dp).clickable(
                onClick = onBackClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ).align(Alignment.CenterStart)
        )

        Box(
            modifier=Modifier
                .size(52.dp)
                .genericDropShadow(CircleShape)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(
                    onClick = onIconClick
                ).align(Alignment.CenterEnd)
        ){
            Icon(
                painter = painterResource(Res.drawable.cart_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp).align(Alignment.Center),
                tint= Secondary
            )
        }
    }
}


@Preview
@Composable
private fun CategoryMutualFundScreenRootPreview() {

    val sampleFunds = listOf(
        MutualFundDomain(
            id = "1",
            name = "SBI Bluechip Fund",
            icon = "",
            category = "Equity: Large Cap",
            remark = null,
            riskText = "Very High",
            type = "Equity",
            returnYearsRate = ReturnYearsRateDomain(
                month3 = 5.0,
                month6 = 10.0,
                year1 = 15.0,
                year3 = 45.0
            ),
            latestNav = "78.5"
        ),
        MutualFundDomain(
            id = "2",
            name = "Axis Midcap Fund",
            icon = "",
            category = "Equity: Mid Cap",
            remark = null,
            riskText = "High",
            type = "Equity",
            returnYearsRate = ReturnYearsRateDomain(
                month3 = 4.2,
                month6 = 9.1,
                year1 = 13.4,
                year3 = 38.0
            ),
            latestNav = "52.7"
        ),
        MutualFundDomain(
            id = "3",
            name = "ICICI Flexi Cap Fund",
            icon = "",
            category = "Flexi Cap",
            remark = null,
            riskText = "Moderately High",
            type = "Equity",
            returnYearsRate = ReturnYearsRateDomain(
                month3 = 3.5,
                month6 = 7.8,
                year1 = 11.2,
                year3 = 31.5
            ),
            latestNav = "102.3"
        ),
        MutualFundDomain(
            id = "4",
            name = "HDFC Small Cap Fund",
            icon = "",
            category = "Small Cap",
            remark = null,
            riskText = "Very High",
            type = "Equity",
            returnYearsRate = ReturnYearsRateDomain(
                month3 = 6.5,
                month6 = 12.4,
                year1 = 18.7,
                year3 = 49.2
            ),
            latestNav = "36.2"
        )
    )

    val sampleCategories = listOf(
        CategoryMutualFundDomain(
            categoryName = "Large Cap",
            categorySearchReference = "large_cap",
            mutualFunds = sampleFunds
        ),
        CategoryMutualFundDomain(
            categoryName = "Mid Cap",
            categorySearchReference = "mid_cap",
            mutualFunds = sampleFunds
        ),
        CategoryMutualFundDomain(
            categoryName = "Flexi Cap",
            categorySearchReference = "flexi_cap",
            mutualFunds = sampleFunds
        ),
        CategoryMutualFundDomain(
            categoryName = "Small Cap",
            categorySearchReference = "small_cap",
            mutualFunds = sampleFunds
        )
    )

    JantaNiveshTheme {
        CategoryMutualFundScreenRootContent(
            uiState = LoadingState.Success,
            categories = sampleCategories,
            onBackClick = {},
            onIconClick = {},
            onFundClick = {},
            onSearchBarClick = {},
            onCategoryClick = {},
            onBundleClick = {},
            onBundledFundClick = {},
            onRetryClick = {}
        )
    }
}