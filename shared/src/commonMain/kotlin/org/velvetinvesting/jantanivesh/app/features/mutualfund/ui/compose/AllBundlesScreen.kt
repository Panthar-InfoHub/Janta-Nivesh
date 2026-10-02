package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.ui.tooling.preview.Preview
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.UiState
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundleDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundleMetaDataDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.CartFab
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.viewmodel.AllBundlesViewModel
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.mutualfund.CartInfo

@Composable
fun AllBundlesScreen(
    onBackClick: () -> Unit,
    onBundleClick: (String) -> Unit,
    onCartClick: () -> Unit
) {
    val viewModel: AllBundlesViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartAmount by CartInfo.fundAmount.collectAsStateWithLifecycle()

    AllBundlesContent(
        uiState = uiState,
        cartAmount = cartAmount,
        onBackClick = onBackClick,
        onBundleClick = onBundleClick,
        onCartClick = onCartClick,
        onRetry = { viewModel.loadBundles() }
    )
}

@Composable
fun AllBundlesContent(
    uiState: UiState<List<BundleDomain>>,
    cartAmount: Int,
    onBackClick: () -> Unit,
    onBundleClick: (String) -> Unit,
    onCartClick: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            BackHeader(
                title = "Curated Bundles",
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
        containerColor = Color.White
    ) { innerPadding ->
        UiStateContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            uiState = uiState,
            onRetry = onRetry
        ) { bundles ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(bundles, key = { _, bundle -> bundle.id }) { index, bundle ->
                    BundleCard(
                        bundle = bundle,
                        style = bundleCardStyleFor(index),
                        onClick = { onBundleClick(bundle.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
                }
            }
        }
    }
}

@Preview
@Composable
private fun AllBundlesScreenPreview() {
    val sampleBundles = listOf(
        BundleDomain(
            id = "1",
            name = "Janta Balance",
            description = "Balanced growth for your long-term goals",
            equityPercentage = 70,
            commodityPercentage = 25,
            debtPercentage = 0,
            hybridPercentage = 5,
            imgUrl = "",
            metaData = BundleMetaDataDomain("", "", ""),
            categories = emptyList()
        ),
        BundleDomain(
            id = "2",
            name = "Janta Growth",
            description = "Equity-led growth over the long run",
            equityPercentage = 100,
            commodityPercentage = 0,
            debtPercentage = 0,
            hybridPercentage = 0,
            imgUrl = "",
            metaData = BundleMetaDataDomain("", "", ""),
            categories = emptyList()
        )
    )

    JantaNiveshTheme {
        AllBundlesContent(
            uiState = UiState.Success(sampleBundles),
            cartAmount = 5000,
            onBackClick = {},
            onBundleClick = {},
            onCartClick = {},
            onRetry = {}
        )
    }
}
