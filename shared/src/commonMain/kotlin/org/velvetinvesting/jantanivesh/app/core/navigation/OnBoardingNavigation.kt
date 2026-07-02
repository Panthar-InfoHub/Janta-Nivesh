package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.JantaNiveshAndVelvetLogo
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.AddYourEmailScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.EnterNameFromPanScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.EnterYourDOBScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.OnboardingEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.OnboardingViewModel

private const val TOTAL_STEPS = 3
@Composable
fun OnboardingNavigation(
    onCompleted: () -> Unit
){

    val navController = rememberNavController()
    val onboardingViewModel: OnboardingViewModel = koinViewModel()
    val state by onboardingViewModel.uiState.collectAsStateWithLifecycle()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentStep = when (
        currentBackStackEntry?.destination?.route

    ) {
        Route.EnterName::class.qualifiedName -> 1
        Route.EnterEmail::class.qualifiedName -> 2
        Route.EnterDob::class.qualifiedName -> 3
        else -> 1
    }

    LaunchedEffect(onboardingViewModel.effect) {
        onboardingViewModel.effect.collect { effect ->
            when (effect) {
                OnboardingEffect.EnterNameFromPan_NavigateToNext -> {
                    navController.navigate(Route.EnterEmail)
                }
                OnboardingEffect.EnterNameFromPan_NavigateBack -> {
                    navController.popBackStack()
                }
                OnboardingEffect.EnterYourDOB_NavigateToNext -> {
                    onCompleted()
                }
                OnboardingEffect.EnterYourDOB_NavigateBack -> {
                    navController.popBackStack()
                }
                OnboardingEffect.AddYourEmail_NavigateToNext -> {
                    navController.navigate(Route.EnterDob)
                }
                OnboardingEffect.AddYourEmail_NavigateBack -> {
                    navController.popBackStack()
                }
                is OnboardingEffect.ShowError -> {
                    SnackBarController.showError(effect.message)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ){pv->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(pv)
        ){
            Box(
                modifier = Modifier.padding(horizontal = Spacing.dp24)
            ) {
                TopAppBarWithBackButtonAndStepCount(
                    stepCount = currentStep,
                    totalSteps = TOTAL_STEPS,
                    onBack = {
                        navController.popBackStack()
                    },
                    modifier= Modifier.padding(bottom = Spacing.dp24)
                )
            }
            NavHost(
                navController = navController,
                startDestination = Route.EnterName,
                modifier = Modifier.weight(1f)
            ) {
                composable<Route.EnterName> {
                    EnterNameFromPanScreen(
                        state = state,
                        onEvent = onboardingViewModel::handleEvent
                    )
                }


                composable<Route.EnterEmail> {
                    AddYourEmailScreen(
                        state = state,
                        onEvent = onboardingViewModel::handleEvent
                    )
                }

                composable<Route.EnterDob> {
                    EnterYourDOBScreen(
                        state = state,
                        onEvent = onboardingViewModel::handleEvent
                    )
                }
            }

            //App Identifier
            JantaNiveshAndVelvetLogo()
        }
    }
}
