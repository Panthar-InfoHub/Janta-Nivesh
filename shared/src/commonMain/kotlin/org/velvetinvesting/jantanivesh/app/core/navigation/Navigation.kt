package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountSuccess

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val prefs: AuthPrefs = koinInject()
    val isLoggedIn = prefs.isLoggedIn()
    val onboardingCompleted = prefs.isOnboardingCompleted()

    val startDestination = when {
        !isLoggedIn -> Route.LoginGraph
        !onboardingCompleted -> Route.OnboardingGraph
        else -> Route.KycGraph
    }

    NavHost(
        navController = navController,
        startDestination =startDestination
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

        //KYC Graph
        composable<Route.KycGraph> {
            KycNavigation(
                onBackNavigation = {navController.popBackStack()},
            )
        }

        //Trading Account Graph
        composable<Route.TradingAccountNavigation> {
            TradingAccountNavigation(
                onBackClick = {navController.popBackStack()},
                onCompletion = {
                    navController.navigate(Route.TradingAccountSuccess) {
                        popUpTo<Route.TradingAccountNavigation> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Route.TradingAccountSuccess> {
            TradingAccountSuccess(
                onButtonClick = {
                    //TODO: Add navigation to mutual fund type selection screen
//                    navController.navigate(Route.MutualFundTypeSelectionScreen){
//                        launchSingleTop= true
//                    }
                },
                buttonText = "Start Investing"
            )
        }
    }
}
