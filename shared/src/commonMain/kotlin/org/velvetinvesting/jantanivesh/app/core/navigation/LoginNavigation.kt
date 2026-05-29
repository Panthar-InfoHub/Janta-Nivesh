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
import org.velvetinvesting.jantanivesh.app.features.login.ui.compose.EnterOtpScreen
import org.velvetinvesting.jantanivesh.app.features.login.ui.compose.LoginWithPhoneNumberScreen
import org.velvetinvesting.jantanivesh.app.features.login.ui.compose.OnboardingChooseLanguage
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.ChooseLanguageEffect
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.ChooseLanguageViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpEffect
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberEffect
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberViewModel

@Composable
fun LoginNavigation(
    onLoginSuccess:()-> Unit
){
    val navController = rememberNavController()
    NavHost(
        startDestination = Route.ChooseLanguage,
        navController = navController
    ) {
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

            LaunchedEffect(viewModel.effect) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        EnterOtpEffect.NavigateToNextScreen -> {
                           onLoginSuccess()
                        }
                        EnterOtpEffect.NavigateBack -> {
                            navController.popBackStack()
                        }
                        is EnterOtpEffect.ShowToast -> {

                        }
                    }
                }
            }

            EnterOtpScreen(
                state = state.copy(phoneNumber = route.phoneNumber),
                onEvent = viewModel::handleEvent
            )
        }
    }
}