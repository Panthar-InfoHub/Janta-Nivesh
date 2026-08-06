package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.core.utils.AppBackHandler
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewConfig
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewScreen
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewUrlMatchType
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens.KycContractScreen
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens.KycFormScreen
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens.KycImageUploadScreen
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens.KycIntroScreen
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.screens.KycSuccessScreen
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCFormScreenEffect
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCFormScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCImageUploaderEffect
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCImageUploaderScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCScreenEffect
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KYCScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KycContractEffect
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KycContractEvent
import org.velvetinvesting.jantanivesh.app.features.kyc.ui.viewmodels.KycContractViewModel

private const val KYC_CONTRACT_WEBVIEW_RESULT = "kyc_contract_webview_completed"

@Composable
fun KycNavigation(
    onBackNavigation: () -> Unit,
    navigateToTradingAccountFlow: () -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.KycIntro
    ) {
        composable<Route.KycIntro> {
            val viewModel: KYCScreenViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel.effect) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is KYCScreenEffect.OpenBrowser ->{
                            navController.navigate(
                                Route.WebViewScreen(
                                    url = effect.url,
                                    exitUrlPatterns = listOf(
                                        "https://digilocker.signzy.tech/success",
                                        "https://digilocker.signzy.tech/digilocker-auth-complete"
                                    ),
                                    title = "DigiLocker Verification",
                                    completionRouteKey = "kyc_form"
                                )
                            )
                        }
                        KYCScreenEffect.NavigateToForm -> navController.navigate(Route.KycForm)
                        is KYCScreenEffect.ShowError -> {
                            SnackBarController.showError(effect.message)
                        }
                    }
                }
            }

            KycIntroScreen(
                state = state,
                onEvent = viewModel::handleEvent,
                onBack = onBackNavigation
            )
        }
        composable<Route.KycForm> {
            val viewModel: KYCFormScreenViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel.effect) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        KYCFormScreenEffect.NavigateToImageUpload -> navController.navigate(Route.KycImageUpload)
                        is KYCFormScreenEffect.ShowError -> {
                            if (effect.navigateBack){ navController.popBackStack() }
                            SnackBarController.showError(effect.message)
                        }
                    }
                }
            }

            KycFormScreen(
                state = state,
                onEvent = viewModel::handleEvent,
                onBack = { navController.popBackStack() }
            )
        }
        composable<Route.KycImageUpload> {
            val viewModel: KYCImageUploaderScreenViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel.effect) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        KYCImageUploaderEffect.NavigateToContract -> navController.navigate(Route.KycContract)
                        is KYCImageUploaderEffect.ShowError -> {
                            SnackBarController.showError(effect.message)
                        }
                    }
                }
            }

            KycImageUploadScreen(
                state = state,
                onEvent = viewModel::handleEvent,
                onBack = { navController.popBackStack() }
            )
        }
        composable<Route.KycContract> {
            val viewModel: KycContractViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val eSignWebViewReturned by it.savedStateHandle
                .getStateFlow(KYC_CONTRACT_WEBVIEW_RESULT, false)
                .collectAsStateWithLifecycle()

            LaunchedEffect(eSignWebViewReturned) {
                if (eSignWebViewReturned) {
                    it.savedStateHandle[KYC_CONTRACT_WEBVIEW_RESULT] = false
                    viewModel.handleEvent(KycContractEvent.OnESignCompleted)
                }
            }

            LaunchedEffect(viewModel.effect) {
                viewModel.effect.collect { effect ->
                    when (effect) {

                        is KycContractEffect.OpenBrowser -> {
                            navController.navigate(
                                Route.WebViewScreen(
                                    url = effect.url,
                                    exitUrlPatterns = listOf("https://www.signzy.com/"),
                                    title = "eSign Verification",
                                    completionRouteKey = "kyc_contract",
                                    matchType = WebViewUrlMatchType.EXACT.name
                                )
                            )
                        }

                        KycContractEffect.NavigateToSuccess -> {
                            navController.navigate(Route.KycSuccess)
                        }

                        is KycContractEffect.ShowError -> {
                            SnackBarController.showError(effect.message)
                        }
                    }

                }
            }

            KycContractScreen(
                state = state,
                onEvent = viewModel::handleEvent,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.KycSuccess> {
            AppBackHandler(enabled = true) {
                onBackNavigation()
            }
            KycSuccessScreen(
                onBackClick = onBackNavigation,
                onTradingAccountSetupClick= navigateToTradingAccountFlow
            )
        }

        composable<Route.WebViewScreen> {
            val route = it.toRoute<Route.WebViewScreen>()

            val onWebViewDone: () -> Unit = {
                when (route.completionRouteKey) {
                    "kyc_form" -> navController.navigate(Route.KycForm) {
                        popUpTo<Route.WebViewScreen> { inclusive = true }
                        launchSingleTop = true
                    }

                    "kyc_contract" -> {
                        // Come back to the contract screen and let it finalize the eSign.
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(KYC_CONTRACT_WEBVIEW_RESULT, true)
                        navController.popBackStack()
                    }

                    else -> navController.popBackStack()
                }
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
