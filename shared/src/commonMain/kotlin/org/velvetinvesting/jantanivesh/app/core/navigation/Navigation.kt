package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs

@Composable
fun BaseNavigation() {
    val navController = rememberNavController()
    val prefs: AuthPrefs = koinInject()
    val isLoggedIn = prefs.isLoggedIn()
    val onboardingCompleted = prefs.isOnboardingCompleted()

    val startDestination = when {
        !isLoggedIn -> Route.LoginGraph
        !onboardingCompleted -> Route.OnboardingGraph
        else -> Route.MainAppGraph
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        //Login Graph
        composable<Route.LoginGraph> {
            LoginNavigation(
                navigateToOnboardingGraph = {
                    navController.navigate(Route.OnboardingGraph)
                },
                navigateToMainAppFlow = {
                    navController.navigate(Route.KycGraph)
                }
            )
        }

        //Onboarding Graph
        composable<Route.OnboardingGraph> {
            OnboardingNavigation(
                onCompleted = {
                    navController.navigate(Route.KycGraph)
                }
            )
        }

        //Main App Graph
        composable<Route.MainAppGraph> {
            MainAppNavigation (
                onSignOut={navController.navigate(Route.LoginGraph){popUpTo(0) { inclusive=true }} }
            )
        }
    }
}
