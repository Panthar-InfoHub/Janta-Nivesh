package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.LoginGraph
    ) {

        //Login Graph
        composable<Route.LoginGraph> {
            LoginNavigation(
                onLoginSuccess = {
                    navController.navigate(Route.OnboardingGraph)
                }
            )
        }

        composable<Route.OnboardingGraph> { 
            OnboardingNavigation(
                onCompleted = {}
            )
        }
    }
}
