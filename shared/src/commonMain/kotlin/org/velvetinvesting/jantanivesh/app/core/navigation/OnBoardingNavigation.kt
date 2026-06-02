package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.janta_nivesh_logo_desc
import jantanivesh.shared.generated.resources.jantanivesh_logo
import jantanivesh.shared.generated.resources.skip
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.TopAppBarWithBackButtonAndStepCount
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.AddYourEmailScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.EnterNameFromPanScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.EnterYourDOBScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.OnboardingEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.OnboardingEvent
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
                    // Handle error show (e.g. snackbar)
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
                if (currentStep==2){
                    Text(
                        stringResource(Res.string.skip),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = -Spacing.dp16, y = -Spacing.dp8)
                            .clickable {
                                onboardingViewModel.handleEvent(OnboardingEvent.OnEmailSkipClicked)
                            }
                    )
                }
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.dp16),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.jantanivesh_logo),
                    contentDescription = stringResource(Res.string.janta_nivesh_logo_desc),
                    modifier = Modifier.height(Spacing.dp58)
                )
            }
        }
    }
}
