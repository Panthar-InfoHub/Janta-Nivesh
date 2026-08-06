package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewConfig
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewScreen
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewUrlMatchType
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountAddressScreen
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountBankDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountBasicDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountFinancialDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountGuardianDetailScreen
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountGuardianPanScreen
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountPANDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountEvent
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.viewmodels.TradingAccountViewModel

@Composable
fun TradingAccountNavigation(onBackClick: () -> Unit, onCompletion: () -> Unit) {

    val navController = rememberNavController()
    val viewModel: TradingAccountViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier=Modifier.fillMaxSize(),
        containerColor = Color.White
    ){
        NavHost(
            navController = navController,
            startDestination = Route.TradingAccountBasicDetails
        ) {

            composable<Route.TradingAccountBasicDetails> {
                TradingAccountBasicDetailsScreen(
                    onClick = {
                        if (uiState.isMinor) {
                            navController.navigate(
                                Route.TradingAccountGuardianDetails
                            ) {
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(Route.TradingAccountPANDetails) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onBackClick = onBackClick,
                    uiState = uiState,
                    handleEvent = viewModel::handleEvent
                )
            }

            composable<Route.TradingAccountPANDetails> {
                TradingAccountPANDetailsScreen(
                    onClick= {navController.navigate(Route.TradingAccountFinancialDetails){
                        launchSingleTop=true
                    } },
                    onBackClick = {navController.popBackStack()},
                    uiState = uiState,
                    handleEvent = viewModel::handleEvent
                )
            }

            composable<Route.TradingAccountFinancialDetails> {
                TradingAccountFinancialDetailsScreen(
                    onClick = {
                        navController.navigate(Route.TradingAccountBankDetails){
                            launchSingleTop=true
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    uiState = uiState,
                    handleEvent = viewModel::handleEvent
                )
            }

            composable<Route.TradingAccountBankDetails> {
                TradingAccountBankDetailsScreen(
                    onClick = {
                        navController.navigate(Route.TradingAccountAddressDetails){
                            launchSingleTop=true
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    uiState = uiState,
                    handleEvent = viewModel::handleEvent
                )
            }

            composable<Route.TradingAccountAddressDetails> {
                TradingAccountAddressScreen(
                    onClick = {
                        viewModel.handleEvent(
                            TradingAccountEvent.SubmitForm { url ->
                                navController.navigate(
                                    Route.WebViewScreen(
                                        url = url,
                                        exitUrlPatterns = emptyList(),
                                        title = "Trading Account"
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    },
                    onBackClick = {navController.popBackStack()},
                    uiState = uiState,
                    handleEvent = viewModel::handleEvent
                )
            }

            composable<Route.TradingAccountGuardianDetails> {
                TradingAccountGuardianPanScreen(
                    onClick = {
                        navController.navigate(Route.TradingAccountGuardiansPANDetails){
                            launchSingleTop=true
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    uiState = uiState,
                    handleEvent = viewModel::handleEvent
                )
            }

            composable<Route.TradingAccountGuardiansPANDetails> {
                TradingAccountGuardianDetailScreen(
                    onClick = {
                        navController.navigate(Route.TradingAccountFinancialDetails){
                            launchSingleTop=true
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    uiState = uiState,
                    handleEvent = viewModel::handleEvent
                )
            }

            composable<Route.WebViewScreen> {
                val route = it.toRoute<Route.WebViewScreen>()
                val onWebViewDone: () -> Unit = {
                    navController.popBackStack()
                    viewModel.handleEvent(
                        TradingAccountEvent.ConfirmAccount { onCompletion() }
                    )
                }

                WebViewScreen(
                    config = WebViewConfig(
                        url = route.url,
                        exitUrlPatterns = route.exitUrlPatterns,
                        matchType = WebViewUrlMatchType.valueOf(route.matchType),
                        title = route.title
                    ),
                    onExitUrlReached = { onWebViewDone() },
                    onBackClick = { onWebViewDone() }
                )
            }
        }
    }
}
