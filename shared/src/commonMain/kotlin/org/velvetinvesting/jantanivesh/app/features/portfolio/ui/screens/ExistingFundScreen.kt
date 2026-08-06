package org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.InvestedAmountBreakdownDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioAllocationDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioAllocationItemDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDashboardDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.TotalInvestmentsDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.PortfolioScreenViewModel

@Composable
fun ExistingFundScreenRoot(
    onBack: () -> Unit,
    onFundClick: (id: String, folio: String) -> Unit,
) {
    val viewModel: PortfolioScreenViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExistingFundScreen(
        uiState = uiState,
        onBack = onBack,
        onFundClick = onFundClick,
        onRetry = viewModel::loadPortfolio
    )
}

@Composable
fun ExistingFundScreen(
    uiState: UiState<PortfolioDomain>,
    onBack: () -> Unit,
    onFundClick: (id: String, folio: String) -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BackHeader(
            title = "Existing Fund",
            showBack = true,
            onBack = onBack,
            modifier = Modifier.padding(horizontal = Spacing.dp16)
        )

        UiStateContainer(
            uiState = uiState,
            onRetry = onRetry
        ) { data ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                items(data.mutualFunds, key = { it.id }) { fund ->
                    FolioFundCard(
                        fundItem = fund,
                        onClick = { onFundClick(fund.id, fund.actualFolio) }
                    )
                }
            }
        }
    }
}

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
            actualFolio = "cwcsdc"
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
            actualFolio = "cwcsdc"
        )
    ),
    fixedDeposits = emptyList(),
    mutualFundSummary = MutualFundSummaryDomain(
        investedAmount = 80000.0,
        currentValue = 94130.0,
        returnsAmount = 14130.0,
        returnsPercent = 17.66
    )
)

@Preview(showBackground = true)
@Composable
fun ExistingFundScreenPreview() {
    JantaNiveshTheme {
        ExistingFundScreen(
            uiState = UiState.Success(previewPortfolioData),
            onBack = {},
            onFundClick = { _, _ -> },
            onRetry = {}
        )
    }
}
