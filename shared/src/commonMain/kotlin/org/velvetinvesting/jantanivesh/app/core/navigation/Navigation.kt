package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val prefs: AuthPrefs = koinInject()
    val isLoggedIn = prefs.isLoggedIn()
    val onboardingCompleted = prefs.isOnboardingCompleted()

    val startDestination = when {
        !isLoggedIn -> Route.LoginGraph
        !onboardingCompleted -> Route.OnboardingGraph
        else -> Route.MainAppFlow
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
                    navController.navigate(Route.MainAppFlow)
                }
            )
        }

        composable<Route.OnboardingGraph> { 
            OnboardingNavigation(
                onCompleted = {
                    navController.navigate(Route.MainAppFlow)
                }
            )
        }

        composable<Route.MainAppFlow> {

        }
    }
}
