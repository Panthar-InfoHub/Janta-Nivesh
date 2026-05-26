package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.compose.LoginWithPhoneNumberScreen
import org.velvetinvesting.jantanivesh.app.features.login.ui.compose.EnterOtpScreen
import org.velvetinvesting.jantanivesh.app.features.login.ui.compose.OnboardingChooseLanguage
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.ChooseLanguageEffect
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.ChooseLanguageViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpEffect
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.EnterOtpViewModel
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberEffect
import org.velvetinvesting.jantanivesh.app.features.login.ui.viewmodels.LoginWithPhoneNumberViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.AddYourEmailScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.EnterNameFromPanScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.EnterYourDOBScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddYourEmailViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterNameFromPanEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterNameFromPanViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EnterYourDOBViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.LoginGraph
    ) {
        navigation<Route.LoginGraph>(
            startDestination = Route.ChooseLanguage
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
                                navController.navigate(Route.OnboardingGraph)
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

        navigation<Route.OnboardingGraph>(
            startDestination = Route.EnterName
        ) {
            composable<Route.EnterName> {
                val viewModel: EnterNameFromPanViewModel = koinViewModel()
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(viewModel.effect) {
                    viewModel.effect.collect { effect ->
                        when (effect) {
                            EnterNameFromPanEffect.NavigateToNextScreen -> {
                                navController.navigate(Route.EnterDob)
                            }
                            EnterNameFromPanEffect.NavigateBack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }

                EnterNameFromPanScreen(
                    state = state,
                    onEvent = viewModel::handleEvent
                )
            }

            composable<Route.EnterDob> {
                val viewModel: EnterYourDOBViewModel = koinViewModel()
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(viewModel.effect) {
                    viewModel.effect.collect { effect ->
                        when (effect) {
                            EnterYourDOBEffect.NavigateToNextScreen -> {
                                navController.navigate(Route.EnterEmail)
                            }
                            EnterYourDOBEffect.NavigateBack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }

                EnterYourDOBScreen(
                    state = state,
                    onEvent = viewModel::handleEvent
                )
            }

            composable<Route.EnterEmail> {
                val viewModel: AddYourEmailViewModel = koinViewModel()
                val state by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(viewModel.effect) {
                    viewModel.effect.collect { effect ->
                        when (effect) {
                            AddYourEmailEffect.NavigateToNextScreen -> {

                            }
                            AddYourEmailEffect.NavigateBack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }

                AddYourEmailScreen(
                    state = state,
                    onEvent = viewModel::handleEvent
                )
            }
        }
    }
}
