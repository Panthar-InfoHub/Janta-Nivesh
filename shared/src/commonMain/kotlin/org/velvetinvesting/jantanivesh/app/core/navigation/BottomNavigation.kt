package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.component.BottomNavBar
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.compose.ExploreFundsScreen
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.compose.HomeScreen
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.ExploreFundsEffect
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.ExploreFundsViewModel
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.HomeScreenEvent
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.HomeScreenSideEffect
import org.velvetinvesting.jantanivesh.app.features.bottomNavigation.ui.viewmodels.HomeScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEvent
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.compose.InsuranceIntroScreen
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.MutualFundPortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.PortfolioScreenMain
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.PortfolioScreenViewModel
import org.velvetinvesting.jantanivesh.app.features.profile.ui.compose.ProfileIntroScreen
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileEffect
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileViewModel

@Composable
fun BottomNavigation(
    navigateToSIPDetailsScreen: (MutualFundPortfolioDomain) -> Unit,
    navigateToFDDetailsScreen: (String) -> Unit,
    navigateToMutualFundTypeSelectionScreen: () -> Unit,
    navigateToGoalScreen: () -> Unit,
    navigateToNotification: () -> Unit,
    navigateToCategoryFDScreen: () -> Unit,
    navigateToMutualFundDetailScreen: (String) -> Unit,
    navigateToHealthInsurance: () -> Unit,
    navigateToTermInsurance: () -> Unit,
    navigateToOtherInsurance: () -> Unit,
    onSignOut: () -> Unit,
    navigateToAddGoal: () -> Unit,
    navigateToSpecificGoalProjection: (String) -> Unit,
    navigateToFD: () -> Unit,
    navigateToTradingAccountSetup: () -> Unit,
    navigateToPrivacyPolicy: () -> Unit,
    navigateToTermsAndConditions: () -> Unit,
    navigateToAboutUs: () -> Unit,
    navigateToAboutVelvet: () -> Unit = {},
    navigateToAboutFire: () -> Unit = {},
    navigateToKYC: () -> Unit,
    navigateToPortfolioFdDetailsScreen: (String) -> Unit,
    navigateToRequestCallBack: () -> Unit,
    navigateToLanguageSettings: () -> Unit,
    navigateToProfileSettigns: () -> Unit
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val homeViewModel: HomeScreenViewModel = koinViewModel()
    val homeState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val portfolioViewModel: PortfolioScreenViewModel =koinViewModel()

    LaunchedEffect(Unit){
        AppEventsController.appEvent.collect {
            when(it){
                AppEvent.HomeEventRefresh -> {
                    homeViewModel.onEvent(HomeScreenEvent.LoadData)
                    AppEventsController.clear()
                }

                AppEvent.GoalEventRefresh -> {
                    homeViewModel.onEvent(HomeScreenEvent.LoadData)
                    AppEventsController.clear()
                }

                AppEvent.FireRefreshEvent -> {
                    homeViewModel.onEvent(HomeScreenEvent.LoadData)
                    AppEventsController.clear()
                }
                AppEvent.PortfolioRefreshEvent -> {
                    portfolioViewModel.refresh()
                    AppEventsController.clear()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentDestination = currentDestination,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        containerColor = Color.White
    ) {
        val pv=it
        NavHost(
            navController = navController,
            modifier=Modifier.fillMaxSize().padding(bottom = pv.calculateBottomPadding()),
            startDestination = Route.Home,
            // Dynamic horizontal animations based on bottom nav index
            enterTransition = {
                val initialIndex = getRouteIndex(initialState.destination)
                val targetIndex = getRouteIndex(targetState.destination)
                when {
                    initialIndex == -1 || targetIndex == -1 -> EnterTransition.None
                    initialIndex == targetIndex -> EnterTransition.None
                    targetIndex > initialIndex -> slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                    else -> slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                }
            },
            exitTransition = {
                val initialIndex = getRouteIndex(initialState.destination)
                val targetIndex = getRouteIndex(targetState.destination)
                when {
                    initialIndex == -1 || targetIndex == -1 -> ExitTransition.None
                    initialIndex == targetIndex -> ExitTransition.None
                    targetIndex > initialIndex -> slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                    else -> slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                }
            },
            popEnterTransition = {
                val initialIndex = getRouteIndex(initialState.destination)
                val targetIndex = getRouteIndex(targetState.destination)
                when {
                    initialIndex == -1 || targetIndex == -1 -> EnterTransition.None
                    initialIndex == targetIndex -> EnterTransition.None
                    targetIndex > initialIndex -> slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                    else -> slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                }
            },
            popExitTransition = {
                val initialIndex = getRouteIndex(initialState.destination)
                val targetIndex = getRouteIndex(targetState.destination)
                when {
                    initialIndex == -1 || targetIndex == -1 -> ExitTransition.None
                    initialIndex == targetIndex -> ExitTransition.None
                    targetIndex > initialIndex -> slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                    else -> slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(350, easing = FastOutSlowInEasing)
                    )
                }
            }
        ){

            composable<Route.Home> {
                LaunchedEffect(homeViewModel.sideEffect){
                    homeViewModel.sideEffect.collect{
                        when(it){
                            HomeScreenSideEffect.NavigateToCreateGoal -> navigateToAddGoal()
                            HomeScreenSideEffect.NavigateToGoals -> navigateToGoalScreen()
                            HomeScreenSideEffect.NavigateToInsurance -> {
                                navController.navigate(Route.Insurance) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            HomeScreenSideEffect.NavigateToInvestFd -> navigateToFD()
                            HomeScreenSideEffect.NavigateToInvestMf -> navigateToMutualFundTypeSelectionScreen()
                            HomeScreenSideEffect.NavigateToKycVerification -> navigateToKYC()
                            HomeScreenSideEffect.NavigateToNotifications -> navigateToNotification()
                            is HomeScreenSideEffect.NavigateToSpecificGoal -> navigateToSpecificGoalProjection(it.goalId)
                            HomeScreenSideEffect.NavigateToTradingVerification -> navigateToTradingAccountSetup()
                        }
                    }
                }
                HomeScreen(
                    state = homeState,
                    onEvent = homeViewModel::onEvent,
                    modifier = Modifier.padding(top = pv.calculateTopPadding())
                )
            }
            composable<Route.FundScreener> {
                val vm: ExploreFundsViewModel = koinViewModel()
                val state by vm.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(vm.effect){
                    vm.effect.collect {
                        when(it){
                            is ExploreFundsEffect.NavigateToFixedDepositDetail -> navigateToFDDetailsScreen(it.fdId)
                            ExploreFundsEffect.NavigateToFixedDeposits -> navigateToCategoryFDScreen()
                            is ExploreFundsEffect.NavigateToMutualFundDetail -> navigateToMutualFundDetailScreen(it.fundId)
                            ExploreFundsEffect.NavigateToMutualFunds -> navigateToMutualFundTypeSelectionScreen()
                        }
                    }
                }

                ExploreFundsScreen(
                    uiState = state,
                    handleEvent = vm::handleEvent,
                    modifier = Modifier.padding(top = pv.calculateTopPadding())
                )
            }
            composable<Route.PortFolio> {
                PortfolioScreenMain(
                    viewModel = portfolioViewModel,
                    onFolioItemClick = {
                        navigateToSIPDetailsScreen(it)
                    },
                    onFDClick = navigateToPortfolioFdDetailsScreen,
                    navigateToCategoryMutualFundScreen=navigateToMutualFundTypeSelectionScreen,
                    navigateToCategoryFDScreen=navigateToCategoryFDScreen,
                    modifier = Modifier.padding(top = pv.calculateTopPadding())
                )
            }
            composable<Route.Profile> {
                val vm: ProfileViewModel = koinViewModel()
                LaunchedEffect(vm.effect){
                    vm.effect.collect {
                        when (it) {
                            ProfileEffect.NavigateToContactUs -> {

                            }
                            ProfileEffect.NavigateToHelpFaq -> {

                            }
                            ProfileEffect.NavigateToKycStatus -> navigateToKYC()
                            ProfileEffect.NavigateToSecondaryLanguage -> navigateToLanguageSettings()
                            ProfileEffect.NavigateToSettings -> navigateToProfileSettigns()
                            ProfileEffect.NavigateToTradingAccountStatus -> navigateToTradingAccountSetup()
                            ProfileEffect.ShowLogoutDialog -> onSignOut()
                        }
                    }
                }
                ProfileIntroScreen(
                    state = homeState,
                    onEvent = vm::handleEvent,
                    modifier = Modifier.padding(top = pv.calculateTopPadding())
                )
            }
            composable<Route.Insurance> {
                InsuranceIntroScreen(
                    navigateToHealthInsurance = navigateToHealthInsurance,
                    navigateToTermInsurance = navigateToTermInsurance,
                    navigateToOtherInsurance = navigateToOtherInsurance,
                    navigateToRequestCallBackScreen = navigateToRequestCallBack,
                    modifier = Modifier.padding(top = pv.calculateTopPadding())
                )
            }

        }

    }
}

private fun getRouteIndex(destination: NavDestination?): Int {
    if (destination == null) return -1
    return when {
        destination.hasRoute<Route.Home>() -> 0
        destination.hasRoute<Route.FundScreener>() -> 1
        destination.hasRoute<Route.PortFolio>() -> 2
        destination.hasRoute<Route.Insurance>() -> 3
        destination.hasRoute<Route.Profile>() -> 4
        else -> -1
    }
}
