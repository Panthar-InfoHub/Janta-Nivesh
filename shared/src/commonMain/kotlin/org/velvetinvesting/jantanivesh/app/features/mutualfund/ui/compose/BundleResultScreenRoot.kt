package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundItemDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.FundMetricsDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.InvestmentFrequency
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.AmountChipsGrid
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.CartFab
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.DropDownField
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.FundBadge
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.LumpSumCart
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.ShadowlessTextField
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.generateInvestmentChips
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.BundleCartUiState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.BundleResultViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.SelectedReturnRatePeriod
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.appGreen
import org.velvetinvesting.jantanivesh.app.core.theme.appRed
import org.velvetinvesting.jantanivesh.app.core.theme.subHeading
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.core.theme.titlesStyle
import org.velvetinvesting.jantanivesh.app.core.utils.LoadingState
import org.velvetinvesting.jantanivesh.app.core.utils.trimTo
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppDialogList
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BarHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.LoaderScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.NextButtonFooter
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.features.mutualfund.CartInfo
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.FundTypeSelector
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.SelectedFundType
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleResultScreenRoot(
    bundleKey: String,
    heading: String,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onFundClick: (String) -> Unit
) {

    val viewModel: BundleResultViewModel = koinViewModel {
        parametersOf(bundleKey)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val uiState by viewModel.loadingState.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val bundleData by viewModel.bundleData.collectAsStateWithLifecycle()
    val bundleCartState by viewModel.bundleCartState.collectAsStateWithLifecycle()
    val showBottomSheet by viewModel.showCartSheet.collectAsStateWithLifecycle()
    val cartAmount by CartInfo.fundAmount.collectAsStateWithLifecycle()

    BundleResultScreenContent(
        uiState = uiState,
        selectedYear = selectedYear,
        bundleData = bundleData,
        bundleCartState = bundleCartState,
        showBottomSheet = showBottomSheet,
        cartAmount = cartAmount,
        heading = heading,
        sheetState = sheetState,
        onBackClick = onBackClick,
        onCartClick = onCartClick,
        onFundClick = onFundClick,
        onRetryClick = viewModel::loadBundleFunds,
        onCycleReturnRate = viewModel::cycleReturnRatePeriod,
        onShowCartSheet = viewModel::showCartSheet,
        onHideCartSheet = viewModel::hideCartSheet,
        onBundleAmountChange = viewModel::onBundleAmountChange,
        onBundleFrequencyChange = viewModel::onBundleFrequencyChange,
        onSipDaySelected = viewModel::onSipDaySelected,
        onDismissFrequencyDropDown = viewModel::dismissFrequencyDropDown,
        onDismissSipDayDropDown = viewModel::dismissSipDayDropDown,
        onShowFrequencyDropDown = viewModel::showFrequencyDropDown,
        onShowSipDayDropDown = viewModel::showSipDayDropDown,
        onAddBundleToCart = viewModel::addBundleToCart
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleResultScreenContent(
    uiState: LoadingState,
    selectedYear: SelectedReturnRatePeriod,
    bundleData: BundledMutualFundDomain?,
    bundleCartState: BundleCartUiState,
    showBottomSheet: Boolean,
    cartAmount: Int,
    heading: String,
    sheetState: SheetState,
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    onFundClick: (String) -> Unit,
    onRetryClick: () -> Unit,
    onCycleReturnRate: () -> Unit,
    onShowCartSheet: () -> Unit,
    onHideCartSheet: () -> Unit,
    onBundleAmountChange: (String) -> Unit,
    onBundleFrequencyChange: (InvestmentFrequency) -> Unit,
    onSipDaySelected: (Int) -> Unit,
    onDismissFrequencyDropDown: () -> Unit,
    onDismissSipDayDropDown: () -> Unit,
    onShowFrequencyDropDown: () -> Unit,
    onShowSipDayDropDown: () -> Unit,
    onAddBundleToCart: () -> Unit
) {
    when (uiState) {

        is LoadingState.Error -> {
            ErrorScreen(
                errorMessage = uiState.error,
                onRetryClick = onRetryClick
            )
        }

        LoadingState.Loading -> {
            LoaderScreen()
        }

        LoadingState.Success -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    BackHeader(
                        title = heading,
                        onBack = onBackClick,
                        modifier = Modifier.padding(horizontal = Spacing.dp16)
                    )
                },
                floatingActionButton = {
                    CartFab(
                        onClick = { onCartClick() },
                        cartAmount = cartAmount,
                    )
                },
                bottomBar = {
                    NextButtonFooter(
                        onClick = onShowCartSheet,
                        pv = PaddingValues(0.dp),
                        value = "Add To Cart"
                    )
                },
                containerColor = Color.White
            ) { pv ->
                BundleResultScreen(
                    data = bundleData,
                    selectedYear = selectedYear,
                    toggleRateYear = onCycleReturnRate,
                    onFundClick = onFundClick,
                    modifier = Modifier.padding(pv)
                )
            }
            if (bundleCartState.frequencyDropDownExpanded) {
                AppDialogList(
                    items = bundleData?.allowedFrequencies ?: emptyList(),
                    textFormatter = { it.label },
                    onSelect = onBundleFrequencyChange,
                    onDismiss = onDismissFrequencyDropDown
                )
            }

            if (bundleCartState.sipDayDropDownExpanded) {
                AppDialogList(
                    items = bundleData?.sipDates ?: emptyList(),
                    textFormatter = { it.toString() },
                    onSelect = onSipDaySelected,
                    onDismiss = onDismissSipDayDropDown
                )
            }

            if (showBottomSheet) {
                BundleCartPopup(
                    sheetState = sheetState,
                    onDismiss = onHideCartSheet,
                    title = "Add Bundle To Cart",
                    state = bundleCartState,
                    onAmountChange = onBundleAmountChange,
                    onAddClick = onAddBundleToCart,
                    showFrequencyDropDown = onShowFrequencyDropDown,
                    showSipDayDropDown = onShowSipDayDropDown,
                    minAmount = bundleData?.minAmount?.toLong() ?: 500L
                )
            }

        }
    }
}

@Composable
fun BundleResultScreen(
    data: BundledMutualFundDomain?,
    selectedYear: SelectedReturnRatePeriod,
    toggleRateYear: () -> Unit,
    onFundClick: (String) -> Unit,
    modifier: Modifier
) {

    val funds = data?.mutualFunds.orEmpty()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 6.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                BarHeader(
                    title = data?.categoryName.orEmpty(),
                )
            }
        }

        item {
            YearRow(
                selectedYear = selectedYear,
                totalFunds = funds.size,
                toggleRateYear = toggleRateYear
            )
        }

        items(funds, key = { it.id }) { fund ->

            BundleMutualFundListCard(
                fund = fund,
                selectedYear = selectedYear,
                onClick = {
                    onFundClick(fund.id)
                }
            )
        }
    }
}

@Composable
fun BundleMutualFundListCard(
    fund: BundledMutualFundItemDomain,
    selectedYear: SelectedReturnRatePeriod,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(1.dp, Color(0xffE2E8F8), RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                SubcomposeAsyncImage(
                    modifier = Modifier.size(44.dp)
                        .genericDropShadow(LocalShapes.current.roundedDp12)
                        .clip(LocalShapes.current.roundedDp12)
                        .background(Color.White),
                    model = fund.icon,
                    contentDescription = null,
                    loading = {
                        MutualFundIcon(
                            schemeName = fund.scheme_name,
                            size = 40.dp
                        )
                    },
                    error = {
                        MutualFundIcon(
                            schemeName = fund.scheme_name,
                            size = 40.dp
                        )
                    },
                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )

                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = fund.scheme_name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${fund.scheme_type} . " + "\nAllocation "+"${fund.allocation_percentage}.0%",
                        style = titlesStyle,
                        color = titleColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 4.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    val returnRate = when (selectedYear) {
                        SelectedReturnRatePeriod.THREE_MONTH -> fund.metrics?.return90D
                        SelectedReturnRatePeriod.SIX_MONTH -> fund.metrics?.return6M
                        SelectedReturnRatePeriod.ONE_YEAR -> fund.metrics?.return1Y
                        SelectedReturnRatePeriod.THREE_YEAR -> fund.metrics?.return3Y
                    }

                    val isPositive = (returnRate ?: 0.0) >= 0

                    val displayColor = if (isPositive) appGreen else appRed
                    Text(
                        text = returnRate?.let { it.trimTo(2) + "%" } ?: "N/A",
                        style = subHeading,
                        color = if (returnRate == null) titleColor else displayColor
                    )


                    Text(
                        text = selectedYear.displayText,
                        style = titlesStyle,
                        color = titleColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleCartPopup(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    title: String,
    state: BundleCartUiState,
    onAmountChange: (String) -> Unit,
    onAddClick: () -> Unit,
    showFrequencyDropDown: () -> Unit,
    showSipDayDropDown: () -> Unit,
    minAmount: Long,
) {

    val fundType by FundTypeSelector.fundType.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    FundBadge(
                        text = when (fundType) {
                            SelectedFundType.SIP -> "SIP"
                            SelectedFundType.LUMSUM -> "Lump Sum"
                        }
                    )
                }

                Text(
                    text = "Bundle Investment",
                    style = titlesStyle,
                    color = Color.Black
                )
            }

            when (fundType) {

                SelectedFundType.LUMSUM -> {
                    LumpSumCart(
                        amount = state.amount,
                        minAmount = minAmount,
                        loading = state.loading,
                        onChipClick = { onAmountChange(it.toString()) },
                        onAmountChange = onAmountChange,
                        onAddClick = onAddClick
                    )
                }

                SelectedFundType.SIP -> {
                    SIPBundleCart(
                        amount = state.amount,
                        minAmount = minAmount,
                        loading = state.loading,
                        frequency = state.selectedFrequency?.label,
                        sipDay = state.sipDay?.toString(),
                        onAmountChange = onAmountChange,
                        onChipClick = { onAmountChange(it.toString()) },
                        onAddClick = onAddClick,
                        showFrequencyDropDown = showFrequencyDropDown,
                        showSipDayDropDown = showSipDayDropDown
                    )
                }
            }
        }
    }
}

@Composable
fun SIPBundleCart(
    amount: Long?,
    minAmount: Long,
    loading: Boolean,
    frequency: String?,
    sipDay: String?,
    onAmountChange: (String) -> Unit,
    onChipClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    showFrequencyDropDown: () -> Unit,
    showSipDayDropDown: () -> Unit,
) {

    val chips = generateInvestmentChips(
        minAmount = minAmount,
        isSip = true
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            ShadowlessTextField(
                value = amount?.toString() ?: "",
                onValueChange = onAmountChange,
                placeHolder = "Enter amount (min. ₹${minAmount})",
                label = "Investment Amount"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (amount != null && amount < minAmount) {
                    Text(
                        text = "Amount less than min ₹$minAmount",
                        color = appRed,
                        style = titlesStyle.copy(fontSize = 12.sp),
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    text = "Min ₹$minAmount",
                    style = titlesStyle.copy(fontSize = 14.sp),
                    color = titleColor
                )
            }
        }

        AmountChipsGrid(
            amounts = chips,
            currentAmount = amount,
            onChipClick = onChipClick
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            DropDownField(
                text = frequency ?: "",
                placeholder = "Select Frequency",
                onClick = showFrequencyDropDown,
                label = "Frequency",
                modifier = Modifier.weight(1f)
            )

            DropDownField(
                text = sipDay ?: "",
                placeholder = "Select SIP Day",
                onClick = showSipDayDropDown,
                label = "SIP Day",
                modifier = Modifier.weight(1f)
            )
        }

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Start SIP",
            loading = loading,
            enabled = amount != null && amount>=minAmount && frequency!=null && sipDay!=null,
            onClick = onAddClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BundleMutualFundListCardPreview() {

    val mockFund = BundledMutualFundItemDomain(
        id = "1",
        scheme_id = "SCH123",
        isin = "INF000000001",
        mapping_code = "MAP123",
        nse_scheme_code = "NSE123",
        platform_code = "PLT123",
        scheme_name = "Axis Bluechip Fund Direct Growth",
        amc_id = "AMC1",
        amc_code = "AXIS",
        amc_name = "Axis Mutual Fund",
        asset_type = "Equity",
        scheme_type = "Large Cap",
        structure = "Open Ended",
        risk_name = "High Risk",
        risk_level = 5,
        latest_nav = "58.42",
        latest_nav_date = "2026-05-20",
        purchase_allowed = true,
        sip_allowed = true,
        redemption_allowed = true,
        switch_allowed = true,
        maturity_date = null,
        nfo_end_date = null,
        createdAt = "",
        updatedAt = "",
        allocation_percentage = 35,
        minAmount = "500",
        metrics = FundMetricsDomain(
            return1Y = 14.56,
            return3Y = 18.32,
            return6M = 8.74,
            return90D = 4.25
        ),
        icon = ""
    )

    BundleMutualFundListCard(
        fund = mockFund,
        selectedYear = SelectedReturnRatePeriod.ONE_YEAR,
        onClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BundleResultScreenRootPreview() {
    JantaNiveshTheme {
        val mockFundItem = BundledMutualFundItemDomain(
            id = "1",
            scheme_id = "SCH123",
            isin = "INF000000001",
            mapping_code = "MAP123",
            nse_scheme_code = "NSE123",
            platform_code = "PLT123",
            scheme_name = "Axis Bluechip Fund Direct Growth",
            amc_id = "AMC1",
            amc_code = "AXIS",
            amc_name = "Axis Mutual Fund",
            asset_type = "Equity",
            scheme_type = "Large Cap",
            structure = "Open Ended",
            risk_name = "High Risk",
            risk_level = 5,
            latest_nav = "58.42",
            latest_nav_date = "2026-05-20",
            purchase_allowed = true,
            sip_allowed = true,
            redemption_allowed = true,
            switch_allowed = true,
            maturity_date = null,
            nfo_end_date = null,
            createdAt = "",
            updatedAt = "",
            allocation_percentage = 35,
            minAmount = "500",
            metrics = FundMetricsDomain(
                return1Y = 14.56,
                return3Y = 18.32,
                return6M = 8.74,
                return90D = 4.25
            ),
            icon = ""
        )

        val mockBundleData = BundledMutualFundDomain(
            categoryName = "Large Cap Bundle",
            key = "large_cap",
            mutualFunds = listOf(
                mockFundItem,
                mockFundItem.copy(id = "2", scheme_name = "SBI Bluechip Fund")
            ),
            sipDates = listOf(1, 5, 10, 15, 20, 25),
            minAmount = 500.0,
            allowedFrequencies = listOf(InvestmentFrequency.MONTHLY, InvestmentFrequency.WEEKLY),
            img_url = ""
        )

        BundleResultScreenContent(
            uiState = LoadingState.Success,
            selectedYear = SelectedReturnRatePeriod.THREE_YEAR,
            bundleData = mockBundleData,
            bundleCartState = BundleCartUiState(
                amount = 5000L,
                selectedFrequency = InvestmentFrequency.MONTHLY,
                sipDay = 5
            ),
            showBottomSheet = false,
            cartAmount = 0,
            heading = "Top Rated Bundles",
            sheetState = rememberModalBottomSheetState(),
            onBackClick = {},
            onCartClick = {},
            onFundClick = {},
            onRetryClick = {},
            onCycleReturnRate = {},
            onShowCartSheet = {},
            onHideCartSheet = {},
            onBundleAmountChange = {},
            onBundleFrequencyChange = {},
            onSipDaySelected = {},
            onDismissFrequencyDropDown = {},
            onDismissSipDayDropDown = {},
            onShowFrequencyDropDown = {},
            onShowSipDayDropDown = {},
            onAddBundleToCart = {}
        )
    }
}
