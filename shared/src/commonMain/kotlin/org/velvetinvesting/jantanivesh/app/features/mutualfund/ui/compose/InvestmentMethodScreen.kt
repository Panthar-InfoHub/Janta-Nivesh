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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.back_arrow
import jantanivesh.shared.generated.resources.ic_callended_filled
import jantanivesh.shared.generated.resources.ic_ruppee_filled
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.LocalShapes
import org.velvetinvesting.jantanivesh.app.core.theme.Primary
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.theme.bgColor4
import org.velvetinvesting.jantanivesh.app.core.theme.buttonTextStyle
import org.velvetinvesting.jantanivesh.app.core.theme.titleColor
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButton
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.AppButtonDefaults
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.ErrorScreen
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.ui.modifierextensions.genericDropShadow
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.ActiveSipDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.InvestedAmountBreakdownDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioAllocationDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioAllocationItemDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDashboardDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.TotalInvestmentsDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.PortfolioScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentMethodScreen(
    onStartSipClick: () -> Unit = {},
    onLumpsumClick: () -> Unit = {},
    onExistingSIPFundClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    onExistingLumpSumFundClick: () -> Unit = {}
) {
    val vm: PortfolioScreenViewModel = koinViewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    InvestmentMethodContent(
        uiState = uiState,
        onRetry = vm::refresh,
        onStartSipClick = onStartSipClick,
        onLumpsumClick = onLumpsumClick,
        onExistingSIPFundClick = onExistingSIPFundClick,
        onBackClick = onBackClick,
        modifier = modifier,
        onExistingLumpSumFundClick = onExistingLumpSumFundClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentMethodContent(
    uiState: UiState<PortfolioDomain>,
    onRetry: () -> Unit,
    onStartSipClick: () -> Unit = {},
    onLumpsumClick: () -> Unit = {},
    onExistingSIPFundClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    onExistingLumpSumFundClick: () -> Unit = {}
) {
    var showSIPBottomSheet by remember { mutableStateOf(false) }
    var showLumpSumBottomSheet by remember { mutableStateOf(false) }
    val sheetStateSIP = rememberModalBottomSheetState()
    val sheetStateLumpSum = rememberModalBottomSheetState()

    UiStateContainer(
        uiState = uiState,
        onRetry = onRetry,
        errorContent = {
            Column(
                modifier = Modifier.fillMaxSize()
                    .statusBarsPadding()
            ) {
                BackHeader(
                    title = "",
                    onBack = onBackClick,
                    modifier = Modifier.padding(horizontal = Spacing.dp16)
                )
                ErrorScreen(
                    errorMessage = it,
                    onRetryClick = onRetry,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    ) {
        val hasFunds = it.mutualFunds.isNotEmpty()
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.White,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.back_arrow),
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 24.dp,
                    end = 24.dp,
                    top = 8.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                item {
                    Column {
                        Text(
                            text = "Invest Your Way",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = InterFontFamily,
                            color = Color.Black
                        )

                        Text(
                            text = "Pick an option to start your investment journey today. ",
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp,
                            lineHeight = 16.sp,
                            color = Color.Gray
                        )
                    }
                }

                item {
                    InvestmentOptionCard(
                        title = "Start a SIP",
                        description = "Automated monthly investments to build wealth steadily over time.",
                        buttonText = "Start SIP ",
                        icon = Res.drawable.ic_callended_filled,
                        onButtonClick = {
                            if (hasFunds){
                                showSIPBottomSheet = true
                            }
                            else {
                                onStartSipClick()
                            }
                        }
                    )
                }

                item {
                    InvestmentOptionCard(
                        title = "Invest a Lump-Sum",
                        description = "Make a one-time investment with your available funds today.",
                        buttonText = "Invest Lump Sum",
                        icon = Res.drawable.ic_ruppee_filled,
                        onButtonClick = {
                            if (hasFunds){
                                showLumpSumBottomSheet = true
                            }
                            else
                            {
                                onLumpsumClick()
                            }
                        }
                    )
                }
            }

            if (showSIPBottomSheet) {
                SIPSelectionBottomSheet(
                    sheetState = sheetStateSIP,
                    onDismiss = { showSIPBottomSheet = false },
                    onExistingFundClick = {
                        showSIPBottomSheet = false
                        onExistingSIPFundClick()
                    },
                    onExploreNewFundsClick = {
                        showSIPBottomSheet = false
                        onStartSipClick()
                    }
                )
            }
            if (showLumpSumBottomSheet) {
                SIPSelectionBottomSheet(
                    sheetState = sheetStateLumpSum,
                    onDismiss = { showLumpSumBottomSheet = false },
                    onExistingFundClick = {
                        showLumpSumBottomSheet = false
                        onExistingLumpSumFundClick()
                    },
                    onExploreNewFundsClick = {
                        showLumpSumBottomSheet = false
                        onLumpsumClick()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SIPSelectionBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onExistingFundClick: () -> Unit,
    onExploreNewFundsClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            BottomSheetOptionItem(
                title = "Top Up existing Funds",
                subtitle = "Invest more in existing funds",
                onClick = onExistingFundClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            BottomSheetOptionItem(
                title = "Explore New Funds",
                subtitle = "Discover new funds and start a fresh investment.",
                onClick = onExploreNewFundsClick
            )
        }
    }
}

@Composable
fun BottomSheetOptionItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = title,
            style = buttonTextStyle,
            color = titleColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.titleSmall,
            color = Color(0xffAAAAAA)
        )
    }
}

@Composable
fun InvestmentOptionCard(
    title: String,
    description: String,
    buttonText: String,
    icon: DrawableResource,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = bgColor4.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = bgColor4.copy(0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFontFamily,
                color = Color.Black
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = description,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = InterFontFamily,
            color = Color.Gray,
            modifier = Modifier.padding(end = 40.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            modifier =Modifier.fillMaxWidth(),
            text = buttonText,
            onClick = onButtonClick,
            style = AppButtonDefaults.style(shape = LocalShapes.current.roundedDp12)
        )
    }
}

@Composable
@Preview
fun InvestmentMethodScreenPreview() {
    JantaNiveshTheme {
        InvestmentMethodContent(
            uiState = UiState.Success(
                PortfolioDomain(
                    dashboard = PortfolioDashboardDomain(0.0, 0.0, 0, 0.0),
                    totalInvestments = TotalInvestmentsDomain(
                        0.0, 0.0, 0.0,
                        PortfolioAllocationDomain(
                            PortfolioAllocationItemDomain(0.0, 0.0),
                            PortfolioAllocationItemDomain(0.0, 0.0)
                        )
                    ),
                    investedAmountBreakdown = InvestedAmountBreakdownDomain(0.0, 0, 0.0, 0.0),
                    mutualFunds = emptyList(),
                    fixedDeposits = emptyList(),
                    mutualFundSummary = MutualFundSummaryDomain(
                        investedAmount = 80000.0,
                        currentValue = 94130.0,
                        returnsAmount = 14130.0,
                        returnsPercent = 17.66,
                    ),
                    activeSips = ActiveSipDomain(
                        totalInvestedAmount = 500000.0,
                        monthlySips = emptyList(),
                        dailySips = emptyList()
                    )
                )
            ),
            onRetry = {}
        )
    }
}
