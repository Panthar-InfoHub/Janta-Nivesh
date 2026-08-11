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
import org.velvetinvesting.jantanivesh.app.core.domain.model.OnboardingStage
import org.velvetinvesting.jantanivesh.app.features.login.ui.screens.EnterOtpScreen
import org.velvetinvesting.jantanivesh.app.features.login.ui.screens.LoginWithPhoneNumberScreen
import org.velvetinvesting.jantanivesh.app.features.login.ui.screens.OnboardingChooseLanguage
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.ChooseLanguageEffect
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.ChooseLanguageViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpEffect
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpEvent
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberEffect
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberViewModel
import org.velvetinvesting.jantanivesh.app.features.splashscreen.ui.compose.SplashScreen
import org.velvetinvesting.jantanivesh.app.features.splashscreen.ui.viewmodels.SplashScreenEffect
import org.velvetinvesting.jantanivesh.app.features.splashscreen.ui.viewmodels.SplashScreenViewModel

@Composable
fun LoginNavigation(
    navigateToOnboardingGraph: (stage: OnboardingStage) -> Unit,
    navigateToMainAppFlow: () -> Unit
){
    val navController = rememberNavController()
    NavHost(
        startDestination = Route.SplashScreen,
        navController = navController
    ) {
        composable<Route.SplashScreen> {
            val viewModel: SplashScreenViewModel = koinViewModel()
            LaunchedEffect(Unit){
                viewModel.effect.collect { effect ->
                    when(effect){
                        SplashScreenEffect.OnGetStartedClick -> {
                            navController.navigate(Route.ChooseLanguage){
                                launchSingleTop=true
                            }
                        }
                    }
                }
            }

            SplashScreen(
                handleEvent = viewModel::handleEvent,
            )
        }
        composable<Route.ChooseLanguage> {
            val viewModel: ChooseLanguageViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel.effect) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        ChooseLanguageEffect.NavigateToNextScreen -> {
                            navController.navigate(Route.LoginWithPhone)
                        }
                    }
                }
            }

            OnboardingChooseLanguage(
                state = state,
                onEvent = viewModel::handleEvent
            )
        }

        composable<Route.LoginWithPhone> {
            val viewModel: LoginWithPhoneNumberViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel.effect) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        LoginWithPhoneNumberEffect.NavigateToOtpScreen -> {
                            navController.navigate(Route.EnterOtp(state.phoneNumber))
                        }
                        LoginWithPhoneNumberEffect.NavigateBack -> {
                            navController.popBackStack()
                        }
                    }
                }
            }

            LoginWithPhoneNumberScreen(
                state = state,
                onEvent = viewModel::handleEvent
            )
        }

        composable<Route.EnterOtp> { backStackEntry ->
            val route: Route.EnterOtp = backStackEntry.toRoute()
            val viewModel: EnterOtpViewModel = koinViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit){
                viewModel.handleEvent(
                    EnterOtpEvent.OnPhoneNumberChanged(route.phoneNumber)
                )
            }

            LaunchedEffect(viewModel.effect) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is EnterOtpEffect.NavigateOnboardingFlow -> {
                           navigateToOnboardingGraph(effect.stage)
                        }
                        EnterOtpEffect.NavigateBack -> {
                            navController.popBackStack()
                        }
                        is EnterOtpEffect.ShowToast -> {

                        }

                        EnterOtpEffect.NavigateToMainAppFlow -> {
                            navigateToMainAppFlow()
                        }
                    }
                }
            }

            EnterOtpScreen(
                state = state.copy(),
                onEvent = viewModel::handleEvent
            )
        }
    }
}