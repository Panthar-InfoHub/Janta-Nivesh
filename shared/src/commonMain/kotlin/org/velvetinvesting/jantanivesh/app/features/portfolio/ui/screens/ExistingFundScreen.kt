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
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.PortfolioScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer

@Composable
fun ExistingFundScreenRoot(
    onBack: () -> Unit,
    onFundClick: (id: String, folio: String) -> Unit,
) {
    val viewModel: PortfolioScreenViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BackHeader(
            title = "Existing Fund",
            showBack = true,
            onBack = onBack
        )

        UiStateContainer(
            uiState = uiState,
            onRetry = viewModel::loadPortfolio
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
                        onClick = { onFundClick(fund.id, fund.folio) }
                    )
                }
            }
        }
    }
}
