package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.core.theme.White
import org.velvetinvesting.jantanivesh.app.core.utils.isAndroid
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
                    navigateToOnboardingGraph = {
                        navController.navigate(Route.OnboardingGraph) {
                            launchSingleTop = true

                            popUpTo<Route.LoginGraph> {
                                inclusive = true
                            }
                        }
                    },
                    navigateToMainAppFlow = {
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
                OnboardingNavigation(
                    onCompleted = {
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
                    onSignOut = {
                        navController.navigate(Route.LoginGraph) {
                            launchSingleTop = true

                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}
