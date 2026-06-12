package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.FundTypeSelector
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.AllBundlesScreen
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.BundleResultScreenRoot
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.CategoryMutualFundScreenRoot
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.InvestmentMethodScreen
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.MutualFundDetailsScreenRoot
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.MutualFundSearchScreenRoot
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.CartScreen
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountSuccess
import androidx.navigation.toRoute

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
        startDestination = Route.MutualFundTypeSelectionScreen
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
                    navController.navigate(Route.MutualFundTypeSelectionScreen){
                        launchSingleTop= true
                    }
                },
                buttonText = "Start Investing"
            )
        }



        composable<Route.MutualFundTypeSelectionScreen> {
            InvestmentMethodScreen(
                onStartSipClick = {
                    FundTypeSelector.updateFundTypeToSIP()
                    navController.navigate(Route.CategoryMutualFund) {
                        launchSingleTop = true
                    }
                },
                onLumpsumClick = {
                    FundTypeSelector.updateFundTypeToLumpSum()
                    navController.navigate(Route.CategoryMutualFund) {
                        launchSingleTop = true
                    }
                },
                onExistingSIPFundClick = {
                    navController.navigate(Route.ExistingFundScreen)
                },
                onExistingLumpSumFundClick = {
                    navController.navigate(Route.ExistingFundLumpSumScreen)
                },
                onBackClick = { navController.popBackStack() },
            )
        }

        composable<Route.CategoryMutualFund> {
            CategoryMutualFundScreenRoot(
                onBackClick = { navController.popBackStack() },
                onIconClick = {
                    navController.navigate(Route.CartScreen) {
                        launchSingleTop = true
                    }
                },
                onFundClick = { id: String ->
                    navController.navigate(Route.MutualFundDetails(id)) {
                        launchSingleTop = true
                    }
                },
                onSearchClick = { search: String ->
                    navController.navigate(Route.MutualFundSearchResult(search)) {
                        launchSingleTop = true
                    }
                },
                onCategoryClick = { id: String ->
                    navController.navigate(Route.MutualFundSearchResult(fundCategory = id)) {
                        launchSingleTop = true
                    }
                },
                onBundledFundClick = { bundleKey: String ->
                    navController.navigate(Route.BundleResultScreen(bundleKey))
                },
                onBundleClick = {
                    navController.navigate(Route.AllBundleScreen)
                }
            )
        }

        composable<Route.MutualFundSearchResult> {
            val route = it.toRoute<Route.MutualFundSearchResult>()
            MutualFundSearchScreenRoot(
                onBackClick = { navController.popBackStack() },
                onFundClick = { id: String ->
                    navController.navigate(Route.MutualFundDetails(id)) {
                        launchSingleTop = true
                    }
                },
                searchText = route.search,
                category = route.fundCategory,
                onSearchClick = { search: String ->
                    navController.navigate(Route.MutualFundSearchResult(search = search))
                },
                heading = "Mutual Funds"
            )
        }

        composable<Route.MutualFundDetails> {
            val route = it.toRoute<Route.MutualFundDetails>()
            MutualFundDetailsScreenRoot(
                id = route.id,
                folioId = route.folioId,
                onBackClick = { navController.popBackStack() },
                onCartClick = {
                    navController.navigate(Route.CartScreen)
                },
                onKycClick = {
                    navController.navigate(Route.KycGraph) {
                        launchSingleTop = true
                    }
                },
                onTradingAccountClick = {
                    navController.navigate(Route.TradingAccountNavigation) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Route.CartScreen> {
            CartScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Route.BundleResultScreen> {
            val route = it.toRoute<Route.BundleResultScreen>()
            BundleResultScreenRoot(
                bundleKey = route.bundleKey,
                heading = "Bundle Funds",
                onBackClick = {
                    navController.popBackStack()
                },
                onCartClick = {
                    navController.navigate(Route.CartScreen)
                },
                onFundClick = { id: String ->
                    navController.navigate(Route.MutualFundDetails(id)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Route.AllBundleScreen> {
            AllBundlesScreen(
                onBackClick = { navController.popBackStack() },
                onBundleClick = { bundleKey: String ->
                    navController.navigate(Route.BundleResultScreen(bundleKey)) {
                        launchSingleTop = true
                    }
                },
                onCartClick = {
                    navController.navigate(Route.CartScreen)
                }
            )
        }
    }
}
