package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.download_ic
import jantanivesh.shared.generated.resources.holdings_ic
import jantanivesh.shared.generated.resources.icon_download
import jantanivesh.shared.generated.resources.tax_savings_ic
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.presentation.portfolio.models.SelectedPortfolio
import org.sharad.velvetinvestment.presentation.portfolio.models.label
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.PortfolioScreenViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.PathGray
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Secondary
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.subHeading
import org.velvetinvesting.jantanivesh.app.core.theme.subHeadingMedium
import org.velvetinvesting.jantanivesh.app.core.theme.tinyLabel
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.formatMoneyAfterL
import org.velvetinvesting.jantanivesh.app.core.utils.trimTo
import org.velvetinvesting.jantanivesh.app.core.utils.withInterRupee
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BarHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.GenericTabSwitcher
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.MutualFundIcon
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.mutualfund.utils.toTitleCase
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.InvestedAmountBreakdownDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PendingOrderDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioAllocationDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioAllocationItemDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDashboardDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.TotalInvestmentsDomain
import kotlin.math.abs

@Composable
fun PortfolioScreenMain(
    viewModel: PortfolioScreenViewModel,
    onFolioItemClick: (MutualFundPortfolioDomain) -> Unit,
    onFDClick: (String) -> Unit,
    navigateToCategoryMutualFundScreen: () -> Unit,
    navigateToCategoryFDScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    val isExportingCapital by viewModel.isExportingCapital.collectAsStateWithLifecycle()
    val isExportingTax by viewModel.isExportingTax.collectAsStateWithLifecycle()
    val isExportingPortfolio by viewModel.isExportingPortfolio.collectAsStateWithLifecycle()

    val pendingOrders by viewModel.pendingOrders.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 3 })

    Box(
        modifier=modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier=Modifier.fillMaxSize()
        ){
            BackHeader("Portfolio", onBack = {}, showBack = false )
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                UiStateContainer(
                    uiState = screenState,
                    onRetry = viewModel::loadPortfolio
                ) { data ->
                    PortfolioScreen(
                        selectedTab = selectedTab,
                        portfolioData = data,
                        changeTab = viewModel::onTabSelected,
                        onSIPClick = onFolioItemClick,
                        onFDClick = onFDClick,
                        navigateToCategoryFDScreen = navigateToCategoryFDScreen,
                        navigateToCategoryMutualFundScreen = navigateToCategoryMutualFundScreen,
                        reload = viewModel::loadPortfolio,
                        onDownloadPortfolioReport = viewModel::downloadPortfolioReport,
                        onDownloadCapitalReport = viewModel::downloadCapitalReport,
                        onCancelPendingOrder= viewModel::cancelPendingOrder,
                        onDownloadTaxReport = { viewModel.downloadTaxReport() },
                        isExportingPortfolio = isExportingPortfolio,
                        isExportingCapital = isExportingCapital,
                        isExportingTax = isExportingTax,
                        pendingOrders = pendingOrders,
                        pagerState=pagerState
                    )
                }
            }
        }
    }

}

@Composable
fun PortfolioScreen(
    selectedTab: SelectedPortfolio,
    portfolioData: PortfolioDomain,
    changeTab: (SelectedPortfolio) -> Unit,
    onSIPClick: (MutualFundPortfolioDomain) -> Unit,
    onFDClick: (String) -> Unit,
    navigateToCategoryFDScreen: () -> Unit,
    navigateToCategoryMutualFundScreen: () -> Unit,
    reload: () -> Unit,
    onDownloadPortfolioReport: () -> Unit,
    onDownloadCapitalReport: () -> Unit,
    onDownloadTaxReport: () -> Unit,
    isExportingPortfolio: Boolean,
    isExportingCapital: Boolean,
    isExportingTax: Boolean,
    pendingOrders: List<PendingOrderDomain>,
    pagerState: PagerState,
    onCancelPendingOrder: (PendingOrderDomain) -> Unit
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                changeTab(SelectedPortfolio.tabs[page])
            }
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {

        GenericTabSwitcher(
            tabs = SelectedPortfolio.tabs,
            selectedTab = selectedTab,
            onTabSelected = {
                changeTab(it)
                scope.launch {
                    pagerState.animateScrollToPage(
                        SelectedPortfolio.tabs.indexOf(it)
                    )
                }
            }
        ) { tab, selected ->
            Text(
                text = tab.label(),
                style = subHeadingMedium,
                color = if (selected) Primary else titleColor,
                softWrap = false,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ){
            when(it){
                0 -> {
                    DashboardPortfolio(
                        totalInvestments = portfolioData.totalInvestments,
                        mutualFunds = portfolioData.mutualFunds,
                        fixedDeposits = portfolioData.fixedDeposits,
                        onSeeAllMF = {
                            changeTab(SelectedPortfolio.MutualFunds)
                            scope.launch { pagerState.animateScrollToPage(1) }
                        },
                        onSeeAllFD = {
                            changeTab(SelectedPortfolio.FixedDeposits)
                            scope.launch { pagerState.animateScrollToPage(2) }

                        },
                        onSIPClick = onSIPClick,
                        onFDClick = onFDClick,
                        reload = reload
                    )
                }
                1-> {
                    MutualFundPortfolio(
                        mutualFund = portfolioData.mutualFunds,
                        investedBreakdown = portfolioData.investedAmountBreakdown,
                        onFundClick = onSIPClick,
                        onEmptyButtonClick = navigateToCategoryMutualFundScreen,
                        reload = reload,
                        onDownloadCapitalReport = onDownloadCapitalReport,
                        onDownloadTaxReport = onDownloadTaxReport,
                        isExportingCapital = isExportingCapital,
                        isExportingTax = isExportingTax,
                        onDownloadPortfolioReport = onDownloadPortfolioReport,
                        isExportingPortfolio = isExportingPortfolio,
                        pendingOrders = pendingOrders,
                        onCancelPendingOrder=onCancelPendingOrder
                    )
                }
                2-> {
                    FixedDepositPortfolio(
                        fixedDeposits = portfolioData.fixedDeposits,
                        onFDClick = onFDClick,
                        onEmptyButtonClick = navigateToCategoryFDScreen,
                        reload = reload
                    )
                }
            }
        }
    }

}

@Composable
fun DashboardPortfolio(
    totalInvestments: TotalInvestmentsDomain,
    mutualFunds: List<MutualFundPortfolioDomain>,
    fixedDeposits: List<FixedDepositPortfolioDomain>,
    onSeeAllMF: () -> Unit,
    onSeeAllFD: () -> Unit,
    onSIPClick: (MutualFundPortfolioDomain) -> Unit,
    onFDClick: (String) -> Unit,
    reload: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = reload
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }
            item { TotalInvestmentCard(totalInvestments) }

            if (mutualFunds.isNotEmpty()) {
                item {
                    BarHeader(
                        title = "Mutual Funds",
                        showArrow = true,
                        onArrowClick = onSeeAllMF
                    )
                }
                items(mutualFunds.take(2), key = { "mf_${it.folio}_${it}" }) { item ->
                    FolioFundCard(fundItem = item, onClick = { onSIPClick(item) })
                }
            }

            if (fixedDeposits.isNotEmpty()) {
                item {
                    BarHeader(
                        title = "Fixed Deposits",
                        showArrow = true,
                        onArrowClick = onSeeAllFD
                    )
                }
                items(fixedDeposits.take(2), key = { "fd_${it.id}" }) { fd ->
                    FixedDepositCard(fdData = fd, onClick = { onFDClick(fd.id) })
                }
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}



@Composable
fun MutualFundPortfolio(
    mutualFund: List<MutualFundPortfolioDomain>,
    investedBreakdown: InvestedAmountBreakdownDomain,
    onFundClick: (MutualFundPortfolioDomain) -> Unit,
    onEmptyButtonClick: () -> Unit,
    reload: () -> Unit,
    onDownloadCapitalReport: () -> Unit,
    onDownloadTaxReport: () -> Unit,
    isExportingCapital: Boolean,
    isExportingTax: Boolean,
    onDownloadPortfolioReport: () -> Unit,
    isExportingPortfolio: Boolean,
    pendingOrders: List<PendingOrderDomain>,
    onCancelPendingOrder: (PendingOrderDomain) -> Unit
) {
    if (mutualFund.isEmpty() && pendingOrders.isEmpty()) {
        EmptyFundScreen(
            onBrowseClick = onEmptyButtonClick,
            text = "Grow your wealth with Mutual Funds through SIPs for steady investing or Lumpsum for one-time opportunities.",
            buttonText = "Browse SIP"
        )
    } else {
        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = reload
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item {
                    MFInvestmentsCard(
                        investedBreakdown,
                        onInvestMore = onEmptyButtonClick
                    )
                }

                item {
                    PdfReportsRow(
                        onDownloadCapitalReport = onDownloadCapitalReport,
                        onDownloadTaxReport = onDownloadTaxReport,
                        onDownloadPortfolioReport=onDownloadPortfolioReport,
                        isExportingPortfolio= isExportingPortfolio,
                        isExportingCapital = isExportingCapital,
                        isExportingTax = isExportingTax
                    )
                }

                if (mutualFund.isNotEmpty()){ item { BarHeader(title = "Mutual Funds") } }
                items(mutualFund, key = { it.folio}) { item ->
                    FolioFundCard(fundItem = item, onClick = {
                        onFundClick(item)
                    })
                }
                if (pendingOrders.isNotEmpty()) {
                    item { BarHeader(title = "Pending Payments") }
                    items(pendingOrders, key = { it.id+ it.startDate }) { item ->
                        PendingPaymentsCard(item, onCancel = {
                            onCancelPendingOrder(item)
                        }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
private fun PdfReportsRow(
    onDownloadCapitalReport: () -> Unit,
    onDownloadTaxReport: () -> Unit,
    isExportingCapital: Boolean,
    isExportingTax: Boolean,
    onDownloadPortfolioReport: () -> Unit,
    isExportingPortfolio: Boolean
) {

    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
        )
    {
        PdfDownloadCard(
            text = "Holdings PDF",
            loading = isExportingCapital,
            onClick = onDownloadCapitalReport,
            iconTint = Color(0xff1FAE6E),
            icon= Res.drawable.holdings_ic,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        PdfDownloadCard(
            text = "Capital PDF",
            loading = isExportingTax,
            onClick = onDownloadTaxReport,
            iconTint = Color(0xff006495),
            icon= Res.drawable.tax_savings_ic,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

        PdfDownloadCard(
            text = "Portfolio PDF",
            loading = isExportingPortfolio,
            onClick = onDownloadPortfolioReport,
            iconTint = Color(0xffF97316),
            icon= Res.drawable.holdings_ic,
            modifier = Modifier.weight(1f).fillMaxHeight()
        )

    }

}

@Composable
private fun PdfDownloadCard(
    text: String,
    loading: Boolean,
    onClick: () -> Unit,
    iconTint: Color,
    icon: DrawableResource,
    modifier: Modifier= Modifier
) {

    Box(
        modifier = modifier
            .clip(LocalShapes.current.roundedDp15)
            .background(iconTint.copy(0.03f))
            .border(1.dp,iconTint.copy(0.1f), LocalShapes.current.roundedDp15 )
            .clickable(
                onClick = onClick,
                enabled = !loading
            ),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier= Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(36.dp).clip(CircleShape).background(iconTint.copy(0.08f)).padding(8.dp),
                tint = iconTint
            )
            Text(
                text = text,
                style = tinyLabel,
                lineHeight = 12.sp,
                color = Primary,
                textAlign = TextAlign.Center
            )
            AnimatedContent(loading){it->
                if (it){
                    CircularProgressIndicator(
                        modifier = Modifier.size(10.dp),
                        strokeWidth = 1.dp,
                        color = iconTint
                    )
                }else{
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (loading) "Generating..." else "Download",
                            fontSize = 10.sp,
                            lineHeight = 10.sp,
                            fontFamily = InterFontFamily,
                            color = iconTint,
                            textAlign = TextAlign.Center
                        )
                        Icon(
                            painter = painterResource(Res.drawable.icon_download),
                            contentDescription = null,
                            modifier = Modifier.size(8.dp),
                            tint = iconTint
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FixedDepositPortfolio(
    fixedDeposits: List<FixedDepositPortfolioDomain>,
    onFDClick: (String) -> Unit,
    onEmptyButtonClick: () -> Unit,
    reload: () -> Unit
) {
    if (fixedDeposits.isEmpty()) {
        EmptyFundScreen(
            onBrowseClick = onEmptyButtonClick,
            text = "Lock in your savings with Fixed Deposits and earn stable, guaranteed returns over time.",
            buttonText = "Browse FD"
        )
    } else {
        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = reload
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item { BarHeader(title = "Fixed Deposits") }
                items(fixedDeposits, key = { it.id }) { fd ->
                    FixedDepositCard(fdData = fd, onClick = { onFDClick(fd.id) })
                }
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                        AppButton(
                            text = "Invest More",
                            onClick = onEmptyButtonClick,
                            modifier = Modifier.width(180.dp),
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun TotalInvestmentCard(totalInvestments: TotalInvestmentsDomain) {
    val shapes = LocalShapes.current
    val labelColorBlue= Color(0xff00658D)
    Box(
        modifier = Modifier.fillMaxWidth()
            .genericDropShadow(shapes.roundedDp15)
            .clip(shapes.roundedDp15)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column{
                Text(text = "Current Value", style = titlesStyle, color = Color.Black)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "₹${formatMoneyAfterL(totalInvestments.currentValue.toLong())}".withInterRupee(),
                        style = subHeading.copy(fontSize = 36.sp, fontWeight = FontWeight.ExtraBold),
                        color = Primary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clip(CircleShape)
                            .background(
                                if (totalInvestments.returnPercent >= 0) Color(0xffC6E7FF).copy(
                                    0.3f
                                ) else appRed.copy(0.1f)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${if (totalInvestments.returnPercent >= 0) "+" else ""}${
                                totalInvestments.returnPercent.trimTo(
                                    2
                                )
                            }%",
                            style = tinyLabel,
                            color = if (totalInvestments.returnPercent >= 0) labelColorBlue else Color.Red,
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Allocation", style = tinyLabel)
                    Text(
                        text = "Total: ₹${formatMoneyAfterL(totalInvestments.currentValue.toLong())}".withInterRupee(),
                        style = tinyLabel,
                        color = Primary
                    )
                }

                val mfPercent = totalInvestments
                    .allocation
                    .mutualFunds
                    .percent
                    .toFloat()

                val fdPercent = totalInvestments
                    .allocation
                    .fixedDeposits
                    .percent
                    .toFloat()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(PathGray)
                ) {

                    if (mfPercent > 0f) {
                        Box(
                            modifier = Modifier
                                .weight(mfPercent)
                                .fillMaxHeight()
                                .background(Primary)
                        )
                    }

                    if (fdPercent > 0f) {
                        Box(
                            modifier = Modifier
                                .weight(fdPercent)
                                .fillMaxHeight()
                                .background(Secondary)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Primary))
                        Text(
                            text = "Mutual Funds (${totalInvestments.allocation.mutualFunds.percent.trimTo(0)}%)",
                            style = tinyLabel,
                            color = Color.Black.copy(alpha = 0.8f)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Secondary))
                        Text(
                            text = "Fixed Deposits (${totalInvestments.allocation.fixedDeposits.percent.trimTo(0)}%)",
                            color = Color.Black.copy(alpha = 0.8f),
                            style = tinyLabel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MFInvestmentsCard(
    investedBreakdown: InvestedAmountBreakdownDomain,
    onInvestMore: () -> Unit,
) {
    val shapes = LocalShapes.current
    Box(
        modifier = Modifier.fillMaxWidth()
            .genericDropShadow(shapes.roundedDp15)
            .clip(shapes.roundedDp15)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(text = "Total Investments", style = titlesStyle, color = titleColor)
                    Text(
                        text = "₹${formatMoneyAfterL(investedBreakdown.investedAmount.toLong() + investedBreakdown.returnsAmount.toLong())}".withInterRupee(),
                        style = subHeading.copy(fontSize = 24.sp),
                        color = Primary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${if (investedBreakdown.returnsPercent >= 0) "+" else ""}${
                                investedBreakdown.returnsPercent.trimTo(
                                    2
                                )
                            }%",
                            style = titlesStyle,
                            color = if (investedBreakdown.returnsPercent >= 0) appGreen else Color.Red
                        )
                        Text(
                            text = "Total Returns (${if (investedBreakdown.returnsAmount < 0) "-₹" else "₹"}${
                                formatMoneyAfterL(
                                    abs(
                                        investedBreakdown.returnsAmount.toLong()
                                    )
                                )
                            })".withInterRupee(),
                            style = titlesStyle,
                            color = titleColor
                        )
                    }
                }
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 12.dp))

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Invested Amount", style = titlesStyle, color = titleColor)
                    Text(
                        text = "₹${formatMoneyAfterL(investedBreakdown.investedAmount.toLong())}".withInterRupee(),
                        style = subHeading,
                        color = Color.Black
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Return Percent", style = titlesStyle, color = titleColor)
                    Text(
                        text = investedBreakdown.returnsPercent.trimTo(2) + "%",
                        style = subHeading,
                        color = if (investedBreakdown.returnsPercent >= 0) appGreen else Color.Red
                    )
                }
                AppButton(
                    text = "INVEST MORE",
                    onClick = onInvestMore,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    style = AppButtonDefaults.style(shape = LocalShapes.current.roundedDp12)
                )
            }
        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun MFInvestmentsCardPreview() {
    JantaNiveshTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MFInvestmentsCard(
                investedBreakdown = previewPortfolioData.investedAmountBreakdown,
                onInvestMore = {},
            )
        }
    }
}
@Composable
private fun ReportDownloadItem(
    text: String,
    loading: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick, indication = null, interactionSource = remember { MutableInteractionSource() })
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                painter = painterResource(Res.drawable.download_ic),
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
        }

        Text(
            text = text,
            style = titlesStyle.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Primary
        )
    }
}
@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun DashboardPortfolioPreview() {
    val pagerState= rememberPagerState(initialPage = 0) { 3 }
    JantaNiveshTheme {
        PortfolioScreen(
            selectedTab = SelectedPortfolio.Dashboard,
            portfolioData = previewPortfolioData,
            changeTab = {},
            onSIPClick = {},
            onFDClick = {},
            navigateToCategoryFDScreen = {},
            navigateToCategoryMutualFundScreen = {},
            reload = {},
            onDownloadPortfolioReport = {},
            onDownloadCapitalReport = {},
            onDownloadTaxReport = {},
            isExportingPortfolio = false,
            isExportingCapital = false,
            isExportingTax = false,
            pendingOrders = samplePendingOrders,
            pagerState=pagerState,{}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun MutualFundPortfolioPreview() {
    val pagerState= rememberPagerState(initialPage = 1) { 3 }
    JantaNiveshTheme {
        PortfolioScreen(
            selectedTab = SelectedPortfolio.MutualFunds,
            portfolioData = previewPortfolioData,
            changeTab = {},
            onSIPClick = {},
            onFDClick = {},
            navigateToCategoryFDScreen = {},
            navigateToCategoryMutualFundScreen = {},
            reload = {},
            onDownloadPortfolioReport = {},
            onDownloadCapitalReport = {},
            onDownloadTaxReport = {},
            isExportingPortfolio = false,
            isExportingCapital = false,
            isExportingTax = false,
            pendingOrders = samplePendingOrders,
            pagerState=pagerState,{}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun FixedDepositPortfolioPreview() {
    JantaNiveshTheme {
        FixedDepositPortfolio(
            fixedDeposits = previewPortfolioData.fixedDeposits,
            onFDClick = {},
            onEmptyButtonClick = {},
            reload = {}
        )
    }
}

@Composable
fun PendingPaymentsCard(item: PendingOrderDomain, onCancel: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        SubcomposeAsyncImage(
                            modifier = Modifier.size(44.dp),
                            model = item.icon,
                            contentDescription = null,

                            loading = {
                                MutualFundIcon(
                                    schemeName = item.schemeName, size = 44.dp
                                )
                            },

                            error = {
                                MutualFundIcon(
                                    schemeName = item.schemeName, size = 44.dp,
                                    backgroundColor = Color(0xffEFEDF3),
                                    textColor = Primary
                                )
                            },

                            success = {
                                SubcomposeAsyncImageContent()
                            }
                        )
                        Text(
                            text = item.schemeName.toTitleCase(),
                            color = Color.Black,
                            style = MaterialTheme.typography.labelSmall.copy(lineHeight = 20.sp),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${formatMoneyAfterL(item.amount.toLong())}".withInterRupee(),
                        color = Color.Black,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = item.type,
                        style = tinyLabel,
                        color = Secondary
                    )
                }
            }

            HorizontalDivider(color = PathGray.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Status",
                        style = tinyLabel,
                        color = Color.Gray
                    )
                    Text(
                        text = item.status,
                        style = subHeadingMedium.copy(fontSize = 12.sp),
                        color = if (item.status.contains("Pending", true)) appRed else appGreen
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Date",
                        style = tinyLabel,
                        color = Color.Gray
                    )
                    Text(
                        text = item.date,
                        style = subHeadingMedium.copy(fontSize = 12.sp),
                        color = titleColor
                    )
                }
            }

            if (item.statusRemark.isNotEmpty()) {
                Text(
                    text = item.statusRemark,
                    style = tinyLabel.copy(fontSize = 10.sp),
                    color = appRed.copy(alpha = 0.8f)
                )
            }

            AppButton(
                modifier = Modifier
                    .padding(top = 4.dp).height(40.dp),
                text = "Cancel order",
                onClick = onCancel,
                enabled = true
            )
        }
    }
}

private val samplePendingOrders = listOf(
    PendingOrderDomain(
        id = "1",
        type = "SIP",
        schemeName = "SBI BLUECHIP FUND - DIRECT PLAN - GROWTH",
        amount = 5000.0,
        date = "2023-10-25T10:00:00Z",
        status = "Payment Pending",
        statusRemark = "Action required: Complete payment to start SIP",
        amc = "SBI Mutual Fund",
        frequency = "Monthly",
        startDate = "2023-11-01",
        ""
    ),
//    PendingOrderDomain(
//        id = "2",
//        type = "Lumpsum",
//        schemeName = "HDFC Index Fund - Nifty 50 Plan",
//        amount = 10000.0,
//        date = "2023-10-24T15:30:00Z",
//        status = "Processing",
//        statusRemark = "Waiting for AMC confirmation",
//        amc = "HDFC Mutual Fund",
//        frequency = "One-time",
//        startDate = "2023-10-24"
//    )
)

private val previewPortfolioData = PortfolioDomain(
    dashboard = PortfolioDashboardDomain(
        currentValue = 1250000.0,
        investedAmount = 1000000.0,
        totalReturns = 250000,
        returnPercent = 25.0
    ),
    totalInvestments = TotalInvestmentsDomain(
        currentValue = 1250000.0,
        totalReturns = 250000.0,
        returnPercent = -25.06,
        allocation = PortfolioAllocationDomain(
            mutualFunds = PortfolioAllocationItemDomain(value = 750000.0, percent = 60.0),
            fixedDeposits = PortfolioAllocationItemDomain(value = 500000.0, percent = 40.0)
        )
    ),
    investedAmountBreakdown = InvestedAmountBreakdownDomain(
        investedAmount = 1000000.0,
        investedItemsCount = 5,
        returnsAmount = 250000.0,
        returnsPercent = 25.0
    ),
    mutualFunds = listOf(
        MutualFundPortfolioDomain(
            id = "f49b4800-6016-4123-bd17-7303bc2b18c3",
            title = "Axis Bluechip Fund",
            category = "Equity",
            amount = 50000.0,
            currentValue = 57500.0,
            returnAmount = 7500.0,
            returnPercentage = "15.5%",
            folio = "12345678",
            icon = "",
            minSipAmount = 100,
            minLumpSumAmount = 500,
            schemeId = 1,
            balanceUnits = 40.04,
        ),
        MutualFundPortfolioDomain(
            id = "0e222090-712c-4748-bbf0-bddd989822ae",
            title = "SBI Small Cap Fund",
            category = "Equity",
            amount = 30000.0,
            currentValue = 36630.0,
            returnAmount = 6630.0,
            returnPercentage = "22.1%",
            folio = "87654321",
            icon = "",
            minSipAmount = 500,
            minLumpSumAmount = 1000,
            schemeId = 2,
            balanceUnits = 20.34,
        )
    ),
    fixedDeposits = listOf(
        FixedDepositPortfolioDomain(
            id = "fd1",
            amount = "100000",
            roiAtBooking = "7.5",
            tenureAtBooking = 12,
            fdIssuedAt = "2023-10-01",
            status = "ACTIVE",
            maturityAmount = "107500",
            userId = "user1",
            userFullName = "John Doe",
            userEmail = "john@example.com",
            issuerLogoUrl = "",
            issuerDisplayName = "HDFC Bank",
            maturityDate = "06 July,2024"
        )
    )
)

