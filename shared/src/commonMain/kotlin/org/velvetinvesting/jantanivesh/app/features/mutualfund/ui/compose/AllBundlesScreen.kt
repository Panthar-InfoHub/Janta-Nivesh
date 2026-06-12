package org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundDomain
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
    uiState: UiState<List<BundledMutualFundDomain>>,
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
                items(bundles) { bundle ->
                    BundleCardExtended(
                        onClick = { onBundleClick(bundle.key) },
                        bundleData = bundle
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
        BundledMutualFundDomain(
            categoryName = "Velvet Preserve",
            key = "preserve",
            mutualFunds = emptyList(),
            minAmount = 10000.0,
            img_url = ""
        ),
        BundledMutualFundDomain(
            categoryName = "Velvet Growth",
            key = "growth",
            mutualFunds = emptyList(),
            minAmount = 5000.0,
            img_url = ""
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
