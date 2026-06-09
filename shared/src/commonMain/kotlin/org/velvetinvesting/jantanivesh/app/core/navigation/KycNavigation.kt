package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.core.utils.AppBackHandler
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.utils.rememberBrowserReturnLauncher
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

@Composable
fun KycNavigation(
    onBackNavigation:() -> Unit,
) {
    val navController = rememberNavController()
    val browserLauncher = rememberBrowserReturnLauncher()

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
                            browserLauncher.launch(effect.url) {
                                navController.navigate(Route.KycForm)
                            }
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
            val browserReturnLauncher = rememberBrowserReturnLauncher()

            LaunchedEffect(viewModel.effect) {
                viewModel.effect.collect { effect ->
                    when (effect) {

                        is KycContractEffect.OpenBrowser -> {
                            browserReturnLauncher.launch(
                                url = effect.url,
                                onReturn = {
                                   viewModel.handleEvent(KycContractEvent.OnESignCompleted)
                                }
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
                onCompleted = onBackNavigation
            )
        }
    }
}
