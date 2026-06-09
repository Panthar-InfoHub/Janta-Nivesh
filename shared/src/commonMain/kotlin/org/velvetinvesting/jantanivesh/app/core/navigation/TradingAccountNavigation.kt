package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountAddressScreen
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountBankDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountBasicDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountClientInfoScreen
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

    val lifecycleOwner = LocalLifecycleOwner.current


    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && uiState.launchedBrowser) {
                viewModel.handleEvent(
                    TradingAccountEvent.ConfirmAccount
                    {
                        onCompletion()
                    }
                )
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier=Modifier.fillMaxSize(),
        containerColor = Color.White
    ){pv->
        NavHost(
            navController = navController,
            startDestination = Route.TradingAccountBasicDetails
        ) {

            composable<Route.TradingAccountBasicDetails> {
                TradingAccountBasicDetailsScreen(
                    pv = pv,
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
                    pv = pv,
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
                    pv = pv,
                    onClick = {
                        navController.navigate(Route.TradingAccountClientInfo){
                            launchSingleTop=true
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    uiState = uiState,
                    handleEvent = viewModel::handleEvent
                )
            }

            composable<Route.TradingAccountClientInfo> {
                TradingAccountClientInfoScreen(
                    pv = pv,
                    onClick = {
                        navController.navigate(Route.TradingAccountBankDetails){
                            launchSingleTop=true
                        }
                    },
                    onBackClick = {navController.popBackStack()},
                    uiState = uiState,
                    handleEvent = viewModel::handleEvent)
            }

            composable<Route.TradingAccountBankDetails> {
                TradingAccountBankDetailsScreen(
                    pv = pv,
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
                    pv = pv,
                    onClick = {
                        viewModel.handleEvent(TradingAccountEvent.SubmitForm{})
                    },
                    onBackClick = {navController.popBackStack()},
                    uiState = uiState,
                    handleEvent = viewModel::handleEvent
                )
            }

            composable<Route.TradingAccountGuardianDetails> {
                TradingAccountGuardianPanScreen(
                    pv = pv,
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
                    pv = pv,
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
        }
    }
}
