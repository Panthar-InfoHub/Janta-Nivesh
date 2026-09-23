package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.core.domain.model.OnboardingStage
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.utils.isAndroid
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.core.domain.usecase.LogoutUseCase

@Composable
fun BaseNavigation() {
    val navController = rememberNavController()
    val prefs: AuthPrefs = koinInject()
    val logout: LogoutUseCase = koinInject()
    // Held above the graph on purpose: the sign-out clean-up has to outlive the destination that
    // asked for it, which is torn down by the navigation that follows.
    val scope = rememberCoroutineScope()
    val isLoggedIn = prefs.isLoggedIn()

    // Set once the user belongs in the main app — either the server finished onboarding, or they
    // deferred the optional part of it and the app chases the rest from inside the main flow.
    val onboardingCompleted = prefs.isOnboardingCompleted()

    // Written on every step that succeeds, so a relaunch after process death picks up on the
    // screen the user actually reached rather than restarting the flow.
    val onboardingStage = OnboardingStage.resumePoint(prefs.getOnboardingStage())

    val startDestination: Route = when {
        !isLoggedIn -> Route.LoginGraph

        !onboardingCompleted -> Route.OnboardingGraph(
            stage = onboardingStage.id
        )

        else -> Route.MainAppGraph
    }

    val isAndroid = remember{ isAndroid() }

    Scaffold(
        containerColor = White
    ){pv->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(
                top = pv.calculateTopPadding(),
                bottom = if (isAndroid) pv.calculateBottomPadding() else 8.dp
            )
        ) {

            composable<Route.LoginGraph> {
                LoginNavigation(
                    navigateToOnboardingGraph = {stage->
                        navController.navigate(Route.OnboardingGraph(stage.id)) {
                            launchSingleTop = true

                            popUpTo<Route.LoginGraph> {
                                inclusive = true
                            }
                        }
                    },
                    navigateToMainAppFlow = {
                        prefs.setLoggedIn(true)
                        prefs.setOnboardingCompleted(true)
                        navController.navigate(Route.MainAppGraph) {
                            launchSingleTop = true

                            popUpTo<Route.LoginGraph> {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<Route.OnboardingGraph> {
                val route = it.toRoute<Route.OnboardingGraph>()
                OnboardingNavigation(
                    stage = route.stage,
                    // Reached from autopay finishing and from skipping the optional steps. The
                    // stored stage is deliberately left as it is: for a skip it still records
                    // what the user has left to do once they are inside the app.
                    onCompleted = {
                        prefs.setLoggedIn(true)
                        prefs.setOnboardingCompleted(true)
                        navController.navigate(Route.MainAppGraph) {
                            launchSingleTop = true

                            popUpTo<Route.OnboardingGraph> {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<Route.MainAppGraph> {
                MainAppNavigation(
                    navigateToKYC = {stage->
                        navController.navigate(Route.OnboardingGraph(stage=stage )) {
                            launchSingleTop = true
                        }
                    }
                ) {
                    // Both ways out — the log out button and an expired token — land here, so the
                    // wipe happens once, before the app is back at the splash with a clean slate.
                    scope.launch {
                        logout()
                        navController.navigate(Route.LoginGraph) {
                            launchSingleTop = true
                            popUpTo(0)
                        }
                    }
                }
            }
        }
    }
}
