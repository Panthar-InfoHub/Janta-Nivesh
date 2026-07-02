package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.back_arrow
import jantanivesh.shared.generated.resources.icon_warning
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.core.utils.isoUtcToDisplayDate
import org.velvetinvesting.jantanivesh.app.core.utils.trimDoubleTo
import org.velvetinvesting.jantanivesh.app.core.utils.trimTo
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppDialogList
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ContinueBackButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NavLineChart
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ShadowCard
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.mutualfund.CartInfo
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CalculatorInputState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.CartBottomSheetState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.DetailsState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.Duration
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.GraphDurationSelection
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.GraphState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.InvestmentFrequency
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MFPurchaseTypes
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.Metrics
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundGraphDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundGraphPointsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundScreenState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.StableMetricUi
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.FundTypeSelector
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.CartFab
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.CartPopup
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.MFBottomSheetType
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.MutualFundDetailsScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MutualFundDetailsScreenRoot(
    id: String,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onKycClick: () -> Unit,
    onTradingAccountClick: () -> Unit,
    folioId: String?,
) {

    val viewModel = koinViewModel<MutualFundDetailsScreenViewModel> { parametersOf(id) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val bottomSheetVisibility by viewModel.bottomSheetVisibility.collectAsStateWithLifecycle()
    val displayPercent by viewModel.stableMetric.collectAsStateWithLifecycle()
    val calculatorState by viewModel.calculatorInput.collectAsStateWithLifecycle()
    val cartState by viewModel.cartSheetState.collectAsStateWithLifecycle()

    val cartAmount by CartInfo.fundAmount.collectAsStateWithLifecycle()

    MutualFundDetailsScreenContent(
        uiState = uiState,
        selectedYear = selectedYear,
        bottomSheetVisibility = bottomSheetVisibility,
        displayPercent = displayPercent,
        calculatorState = calculatorState,
        cartState = cartState,
        cartAmount = cartAmount,
        folioId = folioId,
        onBackClick = onBackClick,
        onCartClick = onCartClick,
        onKycClick = onKycClick,
        onTradingAccountClick = onTradingAccountClick,
        onRetry = viewModel::loadInitial,
        onShowBottomSheet = viewModel::showBottomSheet,
        onHideBottomSheet = viewModel::hideBottomSheet,
        onSelectedYearChange = viewModel::onSelectedYearChange,
        onCalcInvestmentChange = viewModel::onInvestmentChange,
        onCalcSipToggle = viewModel::onSipToggle,
        onCalcTimeChange = viewModel::onTimeChange,
        onGraphRetry = viewModel::loadGraph,
        onCartAmountChange = viewModel::onCartAmountChange,
        onCartTypeChange = viewModel::onCartTypeChange,
        onAddToCart = { viewModel.addToCart(folioId = folioId) },
        showCartFrequenciesDropDown = viewModel::showCartFrequenciesDropDown,
        showCartDateDropDown = viewModel::showCartDateDropDown,
        showDurationDropDown = viewModel::showCartDurationDropDown,
        onCartDateChange = { viewModel.onCartDateChange(it.toString()) },
        onCartDropDownDismiss = viewModel::onCartDropDownDismiss,
        onCartDurationChange = viewModel::onCartDurationChange,
        onCartFrequencyChange = viewModel::onCartFrequencyChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MutualFundDetailsScreenContent(
    uiState: MutualFundScreenState,
    selectedYear: GraphDurationSelection,
    bottomSheetVisibility: MFBottomSheetType?,
    displayPercent: StableMetricUi?,
    calculatorState: CalculatorInputState,
    cartState: CartBottomSheetState,
    cartAmount: Int,
    folioId: String?,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onKycClick: () -> Unit,
    onTradingAccountClick: () -> Unit,
    onRetry: () -> Unit,
    onShowBottomSheet: () -> Unit,
    onHideBottomSheet: () -> Unit,
    onSelectedYearChange: (GraphDurationSelection) -> Unit,
    onCalcInvestmentChange: (Long) -> Unit,
    onCalcSipToggle: (Boolean) -> Unit,
    onCalcTimeChange: (Int) -> Unit,
    onGraphRetry: () -> Unit,
    onCartAmountChange: (String) -> Unit,
    onCartTypeChange: (MFPurchaseTypes) -> Unit,
    onAddToCart: () -> Unit,
    showCartFrequenciesDropDown: () -> Unit,
    showCartDateDropDown: () -> Unit,
    showDurationDropDown: () -> Unit,
    onCartDateChange: (Int) -> Unit,
    onCartDropDownDismiss: () -> Unit,
    onCartDurationChange: (Duration) -> Unit,
    onCartFrequencyChange: (InvestmentFrequency) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()
        )
        {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(Res.drawable.back_arrow),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp, start = 16.dp)
                        .size(24.dp).clickable(
                            onClick = onBackClick,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                )
            }

            Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                UiStateContainer(
                    uiState = uiState.detailsState.toUiState(),
                    onRetry = onRetry
                ) { data ->
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = Color.White,
                        floatingActionButton = {
                            CartFab(
                                onClick = { onCartClick() },
                                cartAmount = cartAmount,
                            )
                        },
                        bottomBar = {
                            if (!folioId.isNullOrBlank()) {
                                ContinueBackButtonFooter(
                                    continueText = "SIP",
                                    backText = "LumpSum",
                                    onContinue = {
                                        FundTypeSelector.updateFundTypeToSIP()
                                        onShowBottomSheet()
                                    },
                                    onBack = {
                                        FundTypeSelector.updateFundTypeToLumpSum()
                                        onShowBottomSheet()
                                    },
                                    pv = PaddingValues(),
                                    enabled = true
                                )
                            } else {
                                NextButtonFooter(
                                    onClick = onShowBottomSheet,
                                    value = "Add to Cart",
                                )
                            }
                        }
                    ) { padding ->
                        MutualFundDetailsScreen(
                            modifier = Modifier.padding(bottom = padding.calculateBottomPadding()),
                            detailsState = data,
                            graphState = uiState.graphState,
                            graphPoints = uiState.chartPoints,
                            selectedYear = selectedYear,
                            onSelectedYearChange = onSelectedYearChange,
                            calculatorState = calculatorState,
                            onCalcInvestmentChange = onCalcInvestmentChange,
                            onCalcSipToggle = onCalcSipToggle,
                            onCalcTimeChange = onCalcTimeChange,
                            onGraphRetry = onGraphRetry
                        )

                        if (cartState.dayDropDownExpanded) {
                            AppDialogList(
                                items = data.sipAllowedDated.ifEmpty {
                                    (1..30).toList()
                                },
                                textFormatter = {
                                    it.toString()
                                },
                                onSelect = onCartDateChange,
                                onDismiss = onCartDropDownDismiss,
                            )
                        }

                        if (cartState.durationDropDownExpanded) {
                            AppDialogList(
                                items = Duration.entries,
                                textFormatter = {
                                    it.label
                                },
                                onSelect = onCartDurationChange,
                                onDismiss = onCartDropDownDismiss,
                            )
                        }

                        if (cartState.frequencyDropDownExpanded) {
                            AppDialogList(
                                items = data.investmentFrequency,
                                textFormatter = {
                                    it.label
                                },
                                onSelect = onCartFrequencyChange,
                                onDismiss = onCartDropDownDismiss
                            )
                        }

                        bottomSheetVisibility?.let {
                            when (it) {
                                MFBottomSheetType.KYC -> {
                                    KYCPopup(
                                        sheetState = sheetState,
                                        onDismiss = onHideBottomSheet,
                                        onClick = {
                                            onKycClick()
                                            onHideBottomSheet()
                                        }
                                    )
                                }

                                MFBottomSheetType.TRADING_ACCOUNT -> {
                                    KYCPopup(
                                        sheetState = sheetState,
                                        onDismiss = onHideBottomSheet,
                                        onClick = {
                                            onTradingAccountClick()
                                            onHideBottomSheet()
                                        }
                                    )
                                }

                                MFBottomSheetType.CART -> {
                                    CartPopup(
                                        detailState = data,
                                        sheetState = sheetState,
                                        cartState = cartState,
                                        onDismiss = onHideBottomSheet,
                                        onAmountChange = onCartAmountChange,
                                        onTypeChange = onCartTypeChange,
                                        onAddClick = onAddToCart,
                                        showFrequencyDropDown = showCartFrequenciesDropDown,
                                        showDateDropDown = showCartDateDropDown,
                                        showDurationDropDown = showDurationDropDown,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun DetailsState.toUiState(): UiState<MutualFundDetailsDomain> {
    return when (this) {
        is DetailsState.Loading -> UiState.Loading
        is DetailsState.Success -> UiState.Success(this.data)
        is DetailsState.Error -> UiState.Error(this.error)
    }
}

@Composable
fun MutualFundDetailsScreen(
    detailsState: MutualFundDetailsDomain,
    graphState: GraphState,
    selectedYear: GraphDurationSelection,
    onSelectedYearChange: (GraphDurationSelection) -> Unit,
    graphPoints: List<MutualFundGraphPointsDomain>,
    calculatorState: CalculatorInputState,
    onCalcInvestmentChange: (Long) -> Unit,
    onCalcSipToggle: (Boolean) -> Unit,
    onCalcTimeChange: (Int) -> Unit,
    modifier: Modifier,
    onGraphRetry: () -> Unit
) {

    var calculatorExpanded by remember { mutableStateOf(false) }
    var riskExpanded by remember { mutableStateOf(false) }
    var returnsExpanded by remember { mutableStateOf(false) }
    var isShortTerm by remember { mutableStateOf(true) }
    var overviewsExpanded by remember { mutableStateOf(false) }

    val progressAnim = remember { Animatable(0f) }

    LaunchedEffect(selectedYear, graphState) {
        progressAnim.snapTo(0f)
        progressAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1400,
                easing = FastOutSlowInEasing
            )
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    )
    {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            item {
                InfoCard(detailsState=detailsState, selectedYear=selectedYear)
            }
            item { Spacer(Modifier.height(20.dp)) }
            item("graph") {


                val progress = progressAnim.value
                GraphCard(
                    graphState = graphState,
                    graphPoints = graphPoints,
                    selectedYear = selectedYear,
                    onSelectedYearChange = onSelectedYearChange,
                    chartAnimationProgress=progress,
                    onReload=onGraphRetry
                )
            }
            item {
                FundInfo(
                    date = detailsState.latest_nav_date,
                    nav = detailsState.latest_nav,
                    assetType = detailsState.asset_type,
                    riskLevel = detailsState.risk_level,
                    metrics = detailsState.metrics,
                    selectedYear =selectedYear
                )
            }

            item { Spacer(Modifier.height(12.dp)) }

            item {
                WhatIfCalculator(
                    modifier =Modifier.padding(horizontal = 16.dp),
                    expanded =calculatorExpanded,
                    onExpandToggle = {calculatorExpanded=!calculatorExpanded},
                    input = calculatorState,
                    metrics = detailsState.metrics,
                    onModeChange = onCalcSipToggle,
                    onInvestmentChange = onCalcInvestmentChange,
                    onTimeChange = onCalcTimeChange
                )
            }

            item {
                HorizontalDivider(color = titleColor.copy(0.6f), modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                RiskSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    expanded = riskExpanded,
                    onExpandToggle ={ riskExpanded=!riskExpanded},
                    riskLevel = detailsState.risk_level,
                    riskText = detailsState.risk_name
                )
            }

            item {
                HorizontalDivider(color = titleColor.copy(0.6f), modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                ReturnsSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    expanded = returnsExpanded,
                    onExpandToggle = { returnsExpanded=!returnsExpanded},
                    isShortTerm = isShortTerm,
                    onShortTermClick = { isShortTerm=true },
                    onLongTermClick = { isShortTerm=false },
                    metrics = detailsState.metrics
                )
            }

            item {
                HorizontalDivider(color = titleColor.copy(0.6f), modifier = Modifier.padding(horizontal = 16.dp))
            }

            item {
                OverviewSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    expanded =overviewsExpanded,
                    onExpandToggle = { overviewsExpanded=!overviewsExpanded},
                    data = detailsState
                )
            }

            item {
                HorizontalDivider(color = titleColor.copy(0.6f), modifier = Modifier.padding(horizontal = 16.dp))
            }

            item { Spacer(Modifier.height(20.dp)) }

            item {
                InvestmentRiskCard()
            }

            item {
                Spacer(Modifier.height( 20.dp))
            }
        }

    }
}

@Composable
fun InvestmentRiskCard() {
    ShadowCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        backgroundColor = Secondary.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_warning),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint=Secondary
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Investment Risk",
                    style = titlesStyle.copy(fontWeight = FontWeight.SemiBold),
                    color = Primary
                )

                Text(
                    text = "Mutual fund investments are subject to market risks. Past performance is not indicative of future returns. Please read all scheme related documents carefully before investing.",
                    style = titlesStyle.copy(fontSize = 12.sp),
                    color = titleColor
                )

            }
        }
    }
}
//
//@Composable
//fun TopRatedFunds(topFunds: List<MutualFundDomain>, onFundClick: (String) -> Unit) {
//    LazyVerticalGrid(
//        columns = GridCells.Adaptive(minSize = 150.dp),
//        modifier = Modifier.fillMaxWidth().heightIn(max = 600.dp),
//        verticalArrangement = Arrangement.spacedBy(20.dp),
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp)
//    ){
//        items(
//            items = topFunds,
//            key = { it.id }
//        ) { fund ->
//            MutualFundGridCard(fund, onClick = { onFundClick(fund.id) })
//        }
//    }
//}

@Composable
fun FundInfo(
    date: String,
    nav: String,
    assetType: String,
    riskLevel: Int,
    metrics: Metrics,
    selectedYear: GraphDurationSelection
) {

    val returnValue = when (selectedYear) {
        GraphDurationSelection.OneMonth -> metrics.return_30d
        GraphDurationSelection.SixMonths -> metrics.return_6m
        GraphDurationSelection.OneYear -> metrics.return_1y
        GraphDurationSelection.ThreeYears -> metrics.return_3y
        GraphDurationSelection.FiveYears -> metrics.return_5y
        GraphDurationSelection.All -> metrics.nav_change_pct
    }

    val returnText = returnValue?.let { "${it.trimTo(2)}%" + when(selectedYear){
        GraphDurationSelection.ThreeYears ->  " p.a."
        GraphDurationSelection.FiveYears -> " p.a."
        else -> ""
    } } ?: "n/a"

    val returnLabel = when(selectedYear){
        GraphDurationSelection.OneMonth -> "Return (1M)"
        GraphDurationSelection.SixMonths -> "Return (6M)"
        GraphDurationSelection.OneYear -> "Return (1Y)"
        GraphDurationSelection.ThreeYears -> "Return (3Y)"
        GraphDurationSelection.FiveYears -> "Return (5Y)"
        GraphDurationSelection.All -> "Return (1D)"
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        modifier=Modifier.fillMaxWidth().heightIn(max=800.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical =20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ){

        item{
            FundInfoCard(
                title = "NAV: ${date.isoUtcToDisplayDate()}",
            ){
                Text(
                    text= "₹ ${nav.toDouble().trimDoubleTo(2)}".withInterRupee(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            }
        }
        item{
            FundInfoCard(
                title = "Asset Type",
            ){
                Text(
                    text= assetType,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            }
        }

        item{
            FundInfoCard(
                title = "Risk Level",
            ){
                Text(
                    text= riskLevel.toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black
                )
            }
        }
        item{
            FundInfoCard(
                title = returnLabel,
            ){
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ){
                    Text(
                        text = returnText,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun FundInfoCard(title: String,content: @Composable () -> Unit) {

    ShadowCard(
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier=Modifier.fillMaxWidth().height(82.dp).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(
                text=title,
                style = titlesStyle.copy(fontSize = 12.sp),
                color = titleColor
            )
            content()
        }
    }

}

@Composable
fun GraphCard(
    graphState: GraphState,
    selectedYear: GraphDurationSelection,
    onSelectedYearChange: (GraphDurationSelection) -> Unit,
    chartAnimationProgress: Float,
    graphPoints: List<MutualFundGraphPointsDomain>,
    onReload: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(248.dp),
            contentAlignment = Alignment.Center
        ) {
            when (graphState) {
                is GraphState.Error -> {
                    ErrorScreen(graphState.error,onReload)
                }

                GraphState.Loading -> {
                    LoadingIndicator()
                }

                is GraphState.Success -> {
                    NavLineChart(
                        data = graphPoints,
                        modifier = Modifier.fillMaxSize(),
                        progress = chartAnimationProgress
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            GraphDurationSelection.entries.forEach {item->
                GraphDurationSelectionItemLabel(
                    label = item.label,
                    selected = item == selectedYear,
                    onClick = {
                        onSelectedYearChange(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun GraphDurationSelectionItemLabel(
    label:String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Text(
        text = label,
        style = titlesStyle.copy(fontWeight = FontWeight.Bold),
        color = if (selected) Secondary else Primary,
        modifier = Modifier.clickable(onClick = onClick, indication = null, interactionSource = remember{MutableInteractionSource()})
    )

}

@Composable
fun InfoCard(
    detailsState: MutualFundDetailsDomain,
    selectedYear: GraphDurationSelection
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {

        Row(
            modifier=Modifier.fillMaxWidth().padding(top=8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {

            SubcomposeAsyncImage(
                modifier = Modifier.size(48.dp)
                    .shadow(
                        elevation = 16.dp
                    ),
                model = detailsState.icon,
                contentDescription = null,
                loading = {
                    MutualFundIcon(
                        schemeName = detailsState.scheme_name,
                        size = 48.dp
                    )
                },
                error = {
                    MutualFundIcon(
                        schemeName = detailsState.scheme_name,
                        size = 48.dp
                    )
                },
                success = {
                    SubcomposeAsyncImageContent()
                }
            )

//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .genericDropShadow(CircleShape)
//                    .clip(CircleShape)
//                    .background(Color.White)
//                    .clickable(onClick = {}),
//                contentAlignment = Alignment.Center
//            ){
//                Icon(
//                    painter = painterResource(Res.drawable.icon_share),
//                    contentDescription = null,
//                    modifier = Modifier.size(32.dp),
//                    tint = Secondary
//                )
//            }

        }

        Text(
            text=detailsState.scheme_name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(top=8.dp)
        )

        Text(
            text= detailsState.risk_name +" | " + detailsState.scheme_type ,
            style = titlesStyle,
            color = Color.Black
        )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val returnValue = when (selectedYear) {
                    GraphDurationSelection.OneMonth -> detailsState.metrics.return_30d
                    GraphDurationSelection.SixMonths -> detailsState.metrics.return_6m
                    GraphDurationSelection.OneYear -> detailsState.metrics.return_1y
                    GraphDurationSelection.ThreeYears -> detailsState.metrics.return_3y
                    GraphDurationSelection.FiveYears -> detailsState.metrics.return_5y
                    GraphDurationSelection.All -> detailsState.metrics.return_1y
                }

                val returnText = returnValue?.let { "${it.trimTo(2)}%" } ?: "n/a"

                val returnColor = when {
                    returnValue == null -> Color.Gray
                    returnValue > 0 -> appGreen
                    returnValue < 0 -> Color.Red
                    else -> Color.Unspecified
                }

                Text(
                    text = returnText,
                    fontFamily = InterFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = returnColor
                )

                val returnLabel = when (selectedYear) {
                    GraphDurationSelection.OneMonth -> "1M return"
                    GraphDurationSelection.SixMonths -> "6M return"
                    GraphDurationSelection.OneYear -> "1Y annualised"
                    GraphDurationSelection.ThreeYears -> "3Y annualised"
                    GraphDurationSelection.FiveYears -> "5Y annualised"
                    GraphDurationSelection.All -> "1Y annualised"
                }

                if (returnValue != null) {
                    Text(
                        text = returnLabel,
                        style = titlesStyle,
                        color = titleColor
                    )
                }

            }

        Row(
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val navChange = detailsState.metrics.nav_change_pct

            Text(
                text = navChange?.let {
                    (if (it > 0) "+" else "") + it.toString() + "%"
                } ?: "N/A",
                style = titlesStyle,
                color = when {
                    navChange == null -> titleColor
                    navChange > 0 -> appGreen
                    else -> Color.Red
                }
            )
            Text(
                text= "1D",
                style = titlesStyle,
                color = titleColor
            )
        }

    }
}

@Composable
fun MutualFundIcon(
    schemeName: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    cornerRadius: Dp = 12.dp,
    backgroundColor: Color = Primary,
    textColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = schemeName
                .take(1).capitalize(Locale.current),
            style = MaterialTheme.typography.headlineSmall,
            color = textColor
        )
    }
}

private val sampleMetrics = Metrics(
    nav_change_pct = 1.2,
    return_1y = 15.5,
    return_30d = 2.1,
    return_3y = 12.0,
    return_6m = 5.0,
    return_90d = 3.5,
    return_5y = 10.5
)

private val sampleMutualFundDetails = MutualFundDetailsDomain(
    amc_code = "AMC001",
    amc_id = "1",
    amc_name = "Sample AMC",
    asset_type = "Equity",
    createdAt = "2023-01-01T00:00:00Z",
    id = "fund123",
    isin = "INE123A01011",
    latest_nav = "150.25",
    latest_nav_date = "2023-10-27T00:00:00Z",
    mapping_code = "M123",
    maturity_date = null,
    metrics = sampleMetrics,
    nfo_end_date = null,
    nse_scheme_code = "S123",
    platform_code = "P123",
    purchase_allowed = true,
    redemption_allowed = true,
    risk_level = 4,
    risk_name = "Moderately High",
    scheme_id = "SCH123",
    scheme_name = "Sample Bluechip Fund",
    scheme_type = "Open Ended",
    sip_allowed = true,
    structure = "Growth",
    switch_allowed = true,
    sipAllowedDated = listOf(1, 5, 10, 15, 20, 25),
    investmentFrequency = listOf(InvestmentFrequency.MONTHLY, InvestmentFrequency.QUARTERLY),
    updatedAt = "2023-10-27T00:00:00Z",
    icon = "",
    minAmount = 5000,
    minSipAmount = 500,
    minLumpSumAmount = 5000
)

private val sampleGraphPoints = listOf(
    MutualFundGraphPointsDomain(140.0, "2023-01-01", "Jan"),
    MutualFundGraphPointsDomain(142.0, "2023-02-01", "Feb"),
    MutualFundGraphPointsDomain(145.0, "2023-03-01", "Mar"),
    MutualFundGraphPointsDomain(143.0, "2023-04-01", "Apr"),
    MutualFundGraphPointsDomain(148.0, "2023-05-01", "May"),
    MutualFundGraphPointsDomain(150.25, "2023-06-01", "Jun")
)

@Preview(heightDp = 5000)
@Composable
fun MutualFundDetailsScreenPreview() {
    JantaNiveshTheme {
        MutualFundDetailsScreenContent(
            uiState = MutualFundScreenState(
                detailsState = DetailsState.Success(sampleMutualFundDetails),
                graphState = GraphState.Success(MutualFundGraphDomain(sampleGraphPoints)),
                chartPoints = sampleGraphPoints
            ),
            selectedYear = GraphDurationSelection.OneYear,
            bottomSheetVisibility = null,
            displayPercent = StableMetricUi("1Y", 15.5),
            calculatorState = CalculatorInputState(),
            cartState = CartBottomSheetState(),
            cartAmount = 0,
            folioId = null,
            onBackClick = {},
            onCartClick = {},
            onKycClick = {},
            onTradingAccountClick = {},
            onRetry = {},
            onShowBottomSheet = {},
            onHideBottomSheet = {},
            onSelectedYearChange = {},
            onCalcInvestmentChange = {},
            onCalcSipToggle = {},
            onCalcTimeChange = {},
            onGraphRetry = {},
            onCartAmountChange = {},
            onCartTypeChange = {},
            onAddToCart = {},
            showCartFrequenciesDropDown = {},
            showCartDateDropDown = {},
            showDurationDropDown = {},
            onCartDateChange = {},
            onCartDropDownDismiss = {},
            onCartDurationChange = {},
            onCartFrequencyChange = {}
        )
    }
}
