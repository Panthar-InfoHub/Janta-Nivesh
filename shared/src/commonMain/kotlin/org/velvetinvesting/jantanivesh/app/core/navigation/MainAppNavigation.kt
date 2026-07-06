package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.utils.rememberBrowserReturnLauncher
import org.velvetinvesting.jantanivesh.app.features.fd.ui.compose.ExploreFdScreen
import org.velvetinvesting.jantanivesh.app.features.fd.ui.compose.FdDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.fd.ui.compose.SetInvestmentDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.ExploreFdEffect
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.ExploreFdViewModel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FdDetailsEffect
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.FdDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.SetInvestmentDetailsEffect
import org.velvetinvesting.jantanivesh.app.features.fd.ui.viewmodels.SetInvestmentDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.YourGoalsUiData
import org.velvetinvesting.jantanivesh.app.features.goals.ui.compose.FinancialGoalScreen
import org.velvetinvesting.jantanivesh.app.features.goals.ui.compose.ProjectedImpactScreen
import org.velvetinvesting.jantanivesh.app.features.goals.ui.compose.YourGoalsScreen
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.AddGoalEffect
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.AddGoalViewModel
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectedImpactEffect
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectedImpactViewModel
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.YourGoalsEffect
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.YourGoalsEvent
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.YourGoalsViewModel
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.compose.GeneralInsuranceScreen
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.compose.HealthInsuranceScreen
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.compose.RequestCallbackScreen
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.compose.TermInsuranceScreen
import org.velvetinvesting.jantanivesh.app.features.insurance.ui.viewmodels.RequestCallbackViewModel
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.FundTypeSelector
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.AllBundlesScreen
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.BundleResultScreenRoot
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.CategoryMutualFundScreenRoot
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.InvestmentMethodScreen
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.MutualFundDetailsScreenRoot
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.MutualFundSearchScreenRoot
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.cart.CartScreen
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.CancelSIPConfirmationScreen
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.ExistingFundLumpSumScreen
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.ExistingFundScreenRoot
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.FDPortfolioDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.FolioFundMFScreen
import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.screens.MFPortfolioDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.profile.ui.compose.NotificationScreen
import org.velvetinvesting.jantanivesh.app.features.profile.ui.compose.ProfileLanguageScreen
import org.velvetinvesting.jantanivesh.app.features.profile.ui.compose.ProfileSettingScreen
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileLanguageEffect
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileLanguageViewModel
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileSettingEffect
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileSettingViewModel
import org.velvetinvesting.jantanivesh.app.features.tradingaccount.ui.compose.TradingAccountSuccess

@Composable
fun MainAppNavigation(
    onSignOut: () -> Unit
) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.BottomNav
    ) {

        //KYC Graph
        composable<Route.KycGraph> {
            KycNavigation(
                onBackNavigation = { navController.popBackStack() },
                navigateToTradingAccountFlow={
                    navController.navigate(Route.TradingAccountNavigation) {
                        launchSingleTop = true
                    }
                }
            )
        }

        //Trading Account Graph
        composable<Route.TradingAccountNavigation> {
            TradingAccountNavigation(
                onBackClick = { navController.popBackStack() },
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
                    navController.navigate(Route.MutualFundTypeSelectionScreen) {
                        launchSingleTop = true
                    }
                },
                onBack={navController.popBackStack()},
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


        //Bottom Navigation
        composable<Route.BottomNav> {
            BottomNavigation(
                navigateToSIPDetailsScreen = { folio ->
                    navController.navigate(Route.FolioFundScreen(folio.folio)) {
                        launchSingleTop = true
                    }
                },
                navigateToFDDetailsScreen = { id ->
                    navController.navigate(Route.FixedDepositDetails(id)) {
                        launchSingleTop = true
                    }
                },
                navigateToMutualFundTypeSelectionScreen = {
                    navController.navigate(Route.MutualFundTypeSelectionScreen) {
                        launchSingleTop = true
                    }
                },
                navigateToCategoryFDScreen = {
                    navController.navigate(Route.FixedDepositSearchResult()) {
                        launchSingleTop = true
                    }
                },
                navigateToGoalScreen = {
                    navController.navigate(Route.GoalsScreen) {
                        launchSingleTop = true
                    }
                },
                navigateToNotification = {
                    navController.navigate(Route.Notifications) {
                        launchSingleTop = true
                    }
                },
                navigateToMutualFundDetailScreen = {
                    navController.navigate(Route.MutualFundDetails(it)) {
                        launchSingleTop = true
                    }
                },
                navigateToHealthInsurance = {
                    navController.navigate(Route.HealthInsurance) {
                        launchSingleTop = true
                    }
                },
                navigateToTermInsurance = {
                    navController.navigate(Route.TermInsurance) {
                        launchSingleTop = true
                    }
                },
                navigateToOtherInsurance = {
                    navController.navigate(Route.OtherInsurance) {
                        launchSingleTop = true
                    }

                },
                navigateToAddGoal = {
                    navController.navigate(Route.SingleGoalAdd) {
                        launchSingleTop = true
                    }
                },
                navigateToSpecificGoalProjection = { id ->
                    navController.navigate(Route.GoalProjectionFlow(id))
                },

                navigateToTradingAccountSetup = {
                    navController.navigate(
                        Route.TradingAccountNavigation
                    )
                },
                navigateToFD = {
                    navController.navigate(
                        Route.FixedDepositSearchResult()
                    ) {
                        launchSingleTop = true
                    }
                },
                navigateToPrivacyPolicy = {
                    navController.navigate(Route.PrivacyPolicy) {
                        launchSingleTop = true
                    }
                },
                navigateToTermsAndConditions = {
                    navController.navigate(Route.TermsAndConditions) {
                        launchSingleTop = true
                    }
                },
                navigateToAboutUs = {
                    navController.navigate(Route.AboutUs) {
                        launchSingleTop = true
                    }
                },
                navigateToAboutVelvet = {
                    navController.navigate(Route.AboutVelvet) {
                        launchSingleTop = true
                    }
                },
                navigateToAboutFire = {
                    navController.navigate(Route.AboutFire) {
                        launchSingleTop = true
                    }
                },
                navigateToKYC = {
                    navController.navigate(Route.KycGraph) {
                        launchSingleTop = true
                    }
                },
                navigateToPortfolioFdDetailsScreen={id->
                    navController.navigate(Route.FDPortfolioDetailsScreen(id)){
                        launchSingleTop=true
                    }
                },
                navigateToRequestCallBack={
                    navController.navigate(Route.RequestCallBack){
                        launchSingleTop=true
                    }
                },
                navigateToLanguageSettings = {
                  navController.navigate(Route.LanguageSelectionSettings){
                      launchSingleTop=true
                  }
                },
                navigateToProfileSettigns = {
                    navController.navigate(Route.ProfileSettingsScreen){
                        launchSingleTop=true
                    }
                },
                onSignOut = onSignOut
            )
        }

        //FD
        composable<Route.FixedDepositSearchResult> {
            val search = it.toRoute<Route.FixedDepositSearchResult>().search
            val vm: ExploreFdViewModel = koinViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(vm.effect) {
                vm.effect.collect { effect ->
                    when (effect) {
                        ExploreFdEffect.NavigateBack -> navController.popBackStack()
                        is ExploreFdEffect.NavigateToFdDetails -> navController.navigate(
                            Route.FixedDepositDetails(
                                effect.id
                            )
                        )
                    }
                }
            }
            ExploreFdScreen(
                state = uiState,
                onEvent = vm::handleEvent
            )
        }
        composable<Route.FixedDepositDetails> {
            val id = it.toRoute<Route.FixedDepositDetails>().id
            val vm: FdDetailsViewModel = koinViewModel(parameters = { parametersOf(id) })
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(vm.effect) {
                vm.effect.collect { effect ->
                    when (effect) {
                        FdDetailsEffect.NavigateBack -> navController.popBackStack()
                        is FdDetailsEffect.NavigateToSetInvestmentDetails -> {
                            navController.navigate(Route.PurchaseFixedDeposit(id))
                        }
                    }
                }
            }
            FdDetailsScreen(
                state = uiState,
                onEvent = vm::handleEvent
            )
        }

        composable<Route.PurchaseFixedDeposit> {
            val id = it.toRoute<Route.PurchaseFixedDeposit>().id
            val vm: SetInvestmentDetailsViewModel = koinViewModel(parameters = { parametersOf(id) })
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(vm.effect) {
                vm.effect.collect { effect ->
                    when (effect) {
                        SetInvestmentDetailsEffect.NavigateBack -> navController.popBackStack()
                    }
                }
            }
            SetInvestmentDetailsScreen(
                state = uiState,
                onEvent = vm::handleEvent
            )
        }

        //Insurance

        composable<Route.HealthInsurance> {
            HealthInsuranceScreen(
                onBack={navController.popBackStack()},
                onRequestCallBackClick={
                    navController.navigate(Route.RequestCallBack){
                        launchSingleTop=true
                    }
                }
            )
        }
        composable<Route.TermInsurance> {
            TermInsuranceScreen(
                onBack={navController.popBackStack()},
                onRequestCallBackClick={
                    navController.navigate(Route.RequestCallBack){
                        launchSingleTop=true
                    }
                }
            )
        }
        composable<Route.OtherInsurance> {
            GeneralInsuranceScreen(
                onBack={navController.popBackStack()},
                onRequestCallBackClick={
                    navController.navigate(Route.RequestCallBack){
                        launchSingleTop=true
                    }
                }
            )
        }
        composable<Route.RequestCallBack> {
            val vm: RequestCallbackViewModel = koinViewModel()
            val state by vm.uiState.collectAsStateWithLifecycle()
            RequestCallbackScreen(
                onBack = { navController.popBackStack() },
                state = state,
                onEvent = vm::handleEvent
            )
        }

        //Goal
        composable<Route.SingleGoalAdd> {
            val vm: AddGoalViewModel = koinViewModel()
            val state by vm.state.collectAsStateWithLifecycle()
            val loading by vm.loading.collectAsStateWithLifecycle()
            LaunchedEffect(vm.effect) {
                vm.effect.collect { effect ->
                    when (effect) {
                        AddGoalEffect.NavigateBack -> navController.popBackStack()
                        is AddGoalEffect.ShowError -> {
                            SnackBarController.showError(effect.message)
                        }
                    }
                }
            }
            FinancialGoalScreen(
                state = state,
                loading = loading,
                handleEvent = vm::handleEvent,
            )
        }
        composable<Route.GoalProjectionFlow> {
            val route = it.toRoute<Route.GoalProjectionFlow>()
            val vm: ProjectedImpactViewModel = koinViewModel(parameters = { parametersOf(route.id) })
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(vm.effect) {
                vm.effect.collect { effect ->
                    when (effect) {
                        ProjectedImpactEffect.NavigateBack -> navController.popBackStack()
                        ProjectedImpactEffect.NavigateToInvest -> navController.navigate(Route.MutualFundSearchResult())
                        ProjectedImpactEffect.OpenPortfolioBottomSheet -> { /* handle */ }
                        ProjectedImpactEffect.ClosePortfolioBottomSheet -> { /* handle */ }
                        is ProjectedImpactEffect.ShowError -> {
                            SnackBarController.showError(effect.message)
                        }
                    }
                }
            }
            ProjectedImpactScreen(
                state = uiState, handleEvent = vm::handleEvent
            )
        }

        composable<Route.GoalsScreen> {
            val vm: YourGoalsViewModel = koinViewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(vm.effect) {
                vm.effect.collect { effect ->
                    when (effect) {
                        YourGoalsEffect.NavigateBack -> navController.popBackStack()
                        YourGoalsEffect.NavigateToAddGoal -> navController.navigate(Route.SingleGoalAdd)
                        is YourGoalsEffect.NavigateToGoalDetails -> {
                            navController.navigate(Route.GoalProjectionFlow(effect.goalId))
                        }
                        YourGoalsEffect.NavigateToInvest -> navController.navigate(Route.MutualFundSearchResult())
                    }
                }
            }
            UiStateContainer(
                uiState = uiState,
                onRetry = { vm.handleEvent(YourGoalsEvent.LoadGoals) }
            ) { data: YourGoalsUiData ->
                YourGoalsScreen(
                    state = data,
                    handleEvent = vm::handleEvent
                )
            }
        }

        //Profile
        composable<Route.LanguageSelectionSettings> {
            val vm: ProfileLanguageViewModel = koinViewModel()
            val state by vm.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(vm.effect){
                vm.effect.collect {
                    when(it){
                        ProfileLanguageEffect.NavigateBack -> {
                            navController.popBackStack()
                        }
                        is ProfileLanguageEffect.ShowError -> {
                            SnackBarController.showError(it.message)
                        }
                    }
                }
            }
            ProfileLanguageScreen(
                state = state,
                onEvent = vm::handleEvent
            )
        }

        composable<Route.ProfileSettingsScreen> {
            val vm: ProfileSettingViewModel = koinViewModel()
            val state by vm.uiState.collectAsStateWithLifecycle()
            val browserLauncher = rememberBrowserReturnLauncher()

            LaunchedEffect(vm.effect){
                vm.effect.collect {
                    when(it){
                        ProfileSettingEffect.NavigateBack -> navController.popBackStack()
                        ProfileSettingEffect.NavigateToDeleteAccount -> {
                            browserLauncher.launch(
                                "https://velvetinvesting.com/delete-account"
                            ){}
                        }
                        ProfileSettingEffect.NavigateToNotification -> {
                            navController.navigate(
                                Route.Notifications
                            ){
                                launchSingleTop=true
                            }
                        }
                        ProfileSettingEffect.NavigateToPrivacyPolicy -> {

                        }
                        ProfileSettingEffect.NavigateToTermsOfService -> {

                        }
                    }
                }
            }

            ProfileSettingScreen(
                state = state ,
                onEvent = vm::handleEvent
            )
        }

        composable<Route.Notifications> {
            NotificationScreen(
                onBack = { navController.popBackStack() }
            )
        }

        //Portfolio

        composable<Route.ExistingFundScreen> {
            ExistingFundScreenRoot(
                onBack = { navController.popBackStack() },
                onFundClick = { id, folio ->
                    navController.navigate(Route.MutualFundDetails(id, folio)) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<Route.ExistingFundLumpSumScreen> {
            ExistingFundLumpSumScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable<Route.FDPortfolioDetailsScreen> {
            val id = it.toRoute<Route.FDPortfolioDetailsScreen>().id
            FDPortfolioDetailsScreen(
                id = id,
                onBackClick = { navController.popBackStack() },
            )
        }

        composable<Route.FolioFundScreen> { data ->
            val id = data.toRoute<Route.FolioFundScreen>().folioId
            FolioFundMFScreen(
                folioId = id,
                onBack = {
                    navController.popBackStack()
                },
                onFundClick = {
                    navController.navigate(
                        Route.SIPPortfolioDetails(
                            id = it.schemeId,
                            title = it.title,
                            category = it.category,
                            amount = it.amount.toDouble(),
                            isSip = it.isSip,
                            startDate = it.startDate,
                            returnPercentage = it.returnPercentage,
                            returnAmount = it.`return`.toInt(),
                            xirr = it.xirr,
                            currentNav = it.currentNav,
                            avgNav = it.avgNav,
                            folio = it.folio,
                            balanceUnits = it.balanceUnits,
                            img_url = it.imgUrl,
                            orderId = it.orderId,
                            actualFolio = it.actualFolio
                        )
                    ) {
                        launchSingleTop = true
                    }
                },
                onTopUp = { prod_id ->
                    navController.navigate(
                        Route.MutualFundDetails(
                            id = prod_id,
                            folioId = id
                        )
                    ) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<Route.SIPPortfolioDetails> {
            val data = it.toRoute<Route.SIPPortfolioDetails>()
            MFPortfolioDetailsScreen(
                onBackClick = { navController.popBackStack() },
                data = data,
            )
        }

        composable<Route.SIPCancellationScreen> {
            val id = it.toRoute<Route.SIPCancellationScreen>().id
            CancelSIPConfirmationScreen(
                id = id,
                onConfirmClick = { id ->
                    navController.navigate(Route.CancelSIPReason(id)) {
                        launchSingleTop = true
                    }
                },
                onCancelClick = { navController.popBackStack() },
            )
        }

    }
}
