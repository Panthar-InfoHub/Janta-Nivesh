package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewConfig
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewScreen
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewUrlMatchType
import org.velvetinvesting.jantanivesh.app.features.auth.ui.compose.BiometricSettingsScreen
import org.velvetinvesting.jantanivesh.app.features.auth.ui.compose.ChangePinScreen
import org.velvetinvesting.jantanivesh.app.features.auth.ui.compose.EnterPinScreen
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.BiometricLoginEffect
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.BiometricSettingsViewModel
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.ChangePinEffect
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.ChangePinViewModel
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.EnterPinEffect
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.EnterPinPurpose
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.EnterPinViewModel
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.UiStateContainer
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEvent
import org.velvetinvesting.jantanivesh.app.features.core.utils.AppEventsController
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
import org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchaseMode
import org.velvetinvesting.jantanivesh.app.features.plans.ui.compose.FundPurchaseScreen
import org.velvetinvesting.jantanivesh.app.features.plans.ui.compose.PurchaseSuccessScreen
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.FundPurchaseEffect
import org.velvetinvesting.jantanivesh.app.features.plans.ui.viewmodels.FundPurchaseViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.SetupAutopayScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.SetupAutopayEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.SetupAutopayEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.SetupAutopayViewModel
import org.velvetinvesting.jantanivesh.app.features.goals.ui.compose.FinancialGoalScreen
import org.velvetinvesting.jantanivesh.app.features.goals.ui.compose.MapSchemesScreen
import org.velvetinvesting.jantanivesh.app.features.goals.ui.compose.ProjectedImpactScreen
import org.velvetinvesting.jantanivesh.app.features.goals.ui.compose.YourGoalsScreen
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.AddGoalEffect
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.AddGoalViewModel
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectedImpactEffect
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectedImpactViewModel
import org.velvetinvesting.jantanivesh.app.features.goals.ui.viewmodels.ProjectionImpactViewModel
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
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.MutualFundDomain
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.CategoryMutualFundScreenRoot
import org.velvetinvesting.jantanivesh.app.features.mutualfund.ui.compose.InvestmentMethodScreen
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.models.BundledMutualFundItemDomain
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
import org.velvetinvesting.jantanivesh.app.features.profile.ui.compose.PrivacyPolicyScreen
import org.velvetinvesting.jantanivesh.app.features.profile.ui.compose.ProfileLanguageScreen
import org.velvetinvesting.jantanivesh.app.features.profile.ui.compose.ProfileSettingScreen
import org.velvetinvesting.jantanivesh.app.features.profile.ui.compose.TermsAndConditionsScreen
import org.velvetinvesting.jantanivesh.app.features.profile.ui.compose.TransactionHistoryScreen
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileLanguageEffect
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileLanguageViewModel
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileSettingEffect
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.ProfileSettingViewModel
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.TransactionHistoryEffect
import org.velvetinvesting.jantanivesh.app.features.profile.ui.viewmodels.TransactionHistoryViewModel

private const val KYC_CONTRACT_WEBVIEW_RESULT = "kyc_contract_webview_completed"
private const val CART_WEBVIEW_RESULT = "cart_webview_completed"
private const val SIP_DETAILS_WEBVIEW_RESULT = "sip_details_webview_completed"
private const val EXISTING_FUND_LUMPSUM_WEBVIEW_RESULT = "existing_fund_lumpsum_webview_completed"
private const val MF_DETAILS_LUMPSUM_WEBVIEW_RESULT = "mf_details_lumpsum_webview_completed"

/** Set on the purchase screen when autopay setup finishes, so it re-reads the mandate list. */
private const val MANDATE_ADDED_RESULT = "mandate_added"

/** Set on the autopay screen when the user returns from the bank's authorization page. */
private const val ADD_MANDATE_WEBVIEW_RESULT = "add_mandate_webview_completed"

/** Marks a web view that should pop back to the autopay screen and flag the return. */
private const val WEBVIEW_COMPLETION_ADD_MANDATE = "add_mandate"

/** Set on the purchase screen when the user returns from the lumpsum payment page. */
private const val PURCHASE_PAYMENT_RESULT = "purchase_payment_returned"

/** Marks the payment web view, which pops back to the purchase screen so it can poll. */
private const val WEBVIEW_COMPLETION_PURCHASE_PAYMENT = "mf_purchase_payment"
private const val FD_DETAILS_WEBVIEW_RESULT = "fd_details_webview_completed"

/**
 * Whether the app lock has already been cleared since the process started. Deliberately not saved
 * state: it dies with the process, so every cold start asks for the PIN once and every later entry
 * into the main app — a rotation, a return from another graph — goes straight to the bottom nav.
 */
private var pinVerifiedThisSession = false

@Composable
fun MainAppNavigation(
    navigateToKYC: (String) -> Unit,
    onSignOut: () -> Unit,
) {

    val navController = rememberNavController()
    val prefs: AuthPrefs = koinInject()
    LaunchedEffect(Unit) {
        AppEventsController.appEvent.collect {
            when (it) {
                AppEvent.LogOut -> {
                    AppEventsController.clear()
                    onSignOut()
                    SnackBarController.showInfo("Token Expired. Login Again.")
                }

                else -> {}
            }
        }
    }
    val startDestination: Any = remember {
        if (!prefs.isMpinSetup() || !prefs.isMpinEnabled() || pinVerifiedThisSession) {
            Route.BottomNav
        } else {
            Route.EnterPin(EnterPinPurpose.APP_LOCK)
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable<Route.EnterPin> { entry ->
            val purpose = entry.toRoute<Route.EnterPin>().purpose
            val vm: EnterPinViewModel = koinViewModel(parameters = { parametersOf(purpose) })
            val state by vm.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(vm.effect) {
                vm.effect.collect { effect ->
                    when (effect) {
                        EnterPinEffect.PinVerified -> when (purpose) {
                            EnterPinPurpose.CHANGE_PIN -> {
                                navController.navigate(Route.ChangePin) {
                                    launchSingleTop = true
                                    // Re-authentication is spent; back from the change screen
                                    // belongs to whatever asked for the change.
                                    popUpTo<Route.EnterPin> { inclusive = true }
                                }
                            }

                            else -> {
                                pinVerifiedThisSession = true
                                navController.navigate(Route.BottomNav) {
                                    launchSingleTop = true
                                    // The lock is cleared for this run — going back to it would
                                    // only strand the user on a screen they already passed.
                                    popUpTo<Route.EnterPin> { inclusive = true }
                                }
                            }
                        }
                    }
                }
            }

            EnterPinScreen(
                state = state,
                onEvent = vm::handleEvent
            )
        }

        composable<Route.ChangePin> {
            val vm: ChangePinViewModel = koinViewModel()
            val state by vm.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(vm.effect) {
                vm.effect.collect { effect ->
                    when (effect) {
                        ChangePinEffect.NavigateBack,
                        ChangePinEffect.PinUpdated -> navController.popBackStack()
                    }
                }
            }

            ChangePinScreen(
                state = state,
                onEvent = vm::handleEvent
            )
        }

        composable<Route.BiometricLogin> {
            val vm: BiometricSettingsViewModel = koinViewModel()
            val state by vm.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(vm.effect) {
                vm.effect.collect { effect ->
                    when (effect) {
                        BiometricLoginEffect.NavigateBack -> navController.popBackStack()
                    }
                }
            }

            BiometricSettingsScreen(
                state = state,
                onEvent = vm::handleEvent
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
                onFundClick = { fund: MutualFundDomain -> navController.openFund(fund) },
                onSearchClick = { search: String ->
                    navController.navigate(Route.MutualFundSearchResult(search)) {
                        launchSingleTop = true
                    }
                },
                onCategoryClick = { id: String ->
                    navController.navigate(Route.MutualFundSearchResult(tag = id)) {
                        launchSingleTop = true
                    }
                },
                onBundledFundClick = { bundleKey: String ->
                    navController.navigate(Route.BundleResultScreen(bundleKey))
                },
                onBundleClick = {
                    navController.navigate(Route.AllBundleScreen)
                },
                onStartSipClick = {
                    // The full fund list, where picking one opens the buy screen.
                    navController.navigate(Route.MutualFundSearchResult()) {
                        launchSingleTop = true
                    }
                },
                onBookFdClick = {
                    navController.navigate(Route.FixedDepositSearchResult()) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Route.MutualFundSearchResult> {
            val route = it.toRoute<Route.MutualFundSearchResult>()
            MutualFundSearchScreenRoot(
                onBackClick = { navController.popBackStack() },
                onFundClick = { fund: MutualFundDomain -> navController.openFund(fund) },
                searchText = route.search,
                tag = route.tag,
                category = route.category,
                amountType = route.amountType,
                onSearchClick = { search: String ->
                    navController.navigate(Route.MutualFundSearchResult(search = search))
                },
                heading = "Mutual Funds"
            )
        }

        composable<Route.MutualFundDetails> {
            val id = it.toRoute<Route.MutualFundDetails>().id
            val folioId = it.toRoute<Route.MutualFundDetails>().folioId
            val lumpSumWebViewReturned by it.savedStateHandle
                .getStateFlow(MF_DETAILS_LUMPSUM_WEBVIEW_RESULT, false)
                .collectAsStateWithLifecycle()
            MutualFundDetailsScreenRoot(
                onBackClick = { navController.popBackStack() },
                id = id,
                folioId=folioId,
                onCartClick = {
                    navController.navigate(Route.CartScreen)
                },
                onKycClick={
                    navController.navigate(Route.KycGraph){
                        launchSingleTop=true
                    }
                },
                onTradingAccountClick={

                },
                onLaunchWebView = { url ->
                    navController.navigate(
                        Route.WebViewScreen(
                            url = url,
                            exitUrlPatterns = emptyList(),
                            title = "Complete Payment",
                            completionRouteKey = "mf_details_lumpsum"
                        )
                    )
                },
                webViewReturned = lumpSumWebViewReturned,
                onWebViewConsumed = {
                    it.savedStateHandle[MF_DETAILS_LUMPSUM_WEBVIEW_RESULT] = false
                }
            )
        }

        composable<Route.FundPurchase> { entry ->
            val route = entry.toRoute<Route.FundPurchase>()
            val vm: FundPurchaseViewModel = koinViewModel {
                parametersOf(
                    route.mfProductId,
                    route.isin,
                    route.fundName,
                    route.fundSubtitle
                )
            }

            val mandateAdded by entry.savedStateHandle
                .getStateFlow(MANDATE_ADDED_RESULT, false)
                .collectAsStateWithLifecycle()

            LaunchedEffect(mandateAdded) {
                if (mandateAdded) {
                    entry.savedStateHandle[MANDATE_ADDED_RESULT] = false
                    vm.refreshMandates()
                }
            }

            val paymentReturned by entry.savedStateHandle
                .getStateFlow(PURCHASE_PAYMENT_RESULT, false)
                .collectAsStateWithLifecycle()

            LaunchedEffect(paymentReturned) {
                if (paymentReturned) {
                    entry.savedStateHandle[PURCHASE_PAYMENT_RESULT] = false
                    // Whether the user actually paid is only knowable from the server.
                    vm.onPaymentReturned()
                }
            }

            LaunchedEffect(vm.effect) {
                vm.effect.collect { effect ->
                    when (effect) {
                        FundPurchaseEffect.AddMandate -> {
                            navController.navigate(Route.AddMandate) {
                                launchSingleTop = true
                            }
                        }

                        is FundPurchaseEffect.OpenPayment -> {
                            navController.navigate(
                                Route.WebViewScreen(
                                    url = effect.url,
                                    exitUrlPatterns = emptyList(),
                                    title = "Complete Payment",
                                    completionRouteKey = WEBVIEW_COMPLETION_PURCHASE_PAYMENT
                                )
                            )
                        }

                        is FundPurchaseEffect.PurchaseConfirmed -> {
                            navController.navigate(
                                Route.PurchaseSuccess(
                                    mode = effect.mode.name,
                                    schemeName = effect.schemeName,
                                    amount = effect.amount,
                                    installmentDay = effect.installmentDay,
                                    startDate = effect.startDate
                                )
                            ) {
                                // The purchase is placed — going back to the form would only
                                // invite a duplicate.
                                popUpTo<Route.FundPurchase> { inclusive = true }
                            }
                        }
                    }
                }
            }

            val state by vm.uiState.collectAsStateWithLifecycle()
            FundPurchaseScreen(
                state = state,
                handleEvent = vm::handleEvent,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Route.AddMandate> { entry ->
            val vm: SetupAutopayViewModel = koinViewModel()

            val authorizationReturned by entry.savedStateHandle
                .getStateFlow(ADD_MANDATE_WEBVIEW_RESULT, false)
                .collectAsStateWithLifecycle()

            LaunchedEffect(authorizationReturned) {
                if (authorizationReturned) {
                    entry.savedStateHandle[ADD_MANDATE_WEBVIEW_RESULT] = false
                    vm.handleEvent(SetupAutopayEvent.OnAuthorizationReturned)
                }
            }

            LaunchedEffect(Unit) {
                vm.effect.collect { effect ->
                    when (effect) {
                        is SetupAutopayEffect.OpenMandateWebView -> {
                            navController.navigate(
                                Route.WebViewScreen(
                                    url = effect.url,
                                    title = "UPI Autopay",
                                    completionRouteKey = WEBVIEW_COMPLETION_ADD_MANDATE
                                )
                            )
                        }

                        SetupAutopayEffect.AutopayCompleted -> {
                            // Hand the result back to whoever asked for the mandate so it can
                            // pick the new one up without being rebuilt.
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(MANDATE_ADDED_RESULT, true)
                            navController.popBackStack()
                        }
                    }
                }
            }

            val state by vm.uiState.collectAsStateWithLifecycle()
            SetupAutopayScreen(
                state = state,
                handleEvent = vm::handleEvent
            )
        }

        composable<Route.PurchaseSuccess> { entry ->
            val route = entry.toRoute<Route.PurchaseSuccess>()

            PurchaseSuccessScreen(
                mode = PurchaseMode.fromName(route.mode),
                schemeName = route.schemeName,
                amount = route.amount,
                installmentDay = route.installmentDay.takeIf { it > 0 },
                startDate = route.startDate.takeIf { it.isNotBlank() },
                onViewHoldingsClick = { navController.popBackStack() },
                onDoneClick = { navController.popBackStack() }
            )
        }

        composable<Route.CartScreen> {
            val cartWebViewReturned by it.savedStateHandle
                .getStateFlow(CART_WEBVIEW_RESULT, false)
                .collectAsStateWithLifecycle()
            CartScreen(
                onBack = {
                    navController.popBackStack()
                },
                onLaunchWebView = { url ->
                    navController.navigate(
                        Route.WebViewScreen(
                            url = url,
                            exitUrlPatterns = emptyList(),
                            title = "Complete Payment",
                            completionRouteKey = "cart"
                        )
                    )
                },
                webViewReturned = cartWebViewReturned,
                onWebViewConsumed = {
                    it.savedStateHandle[CART_WEBVIEW_RESULT] = false
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
                onFundClick = { fund: BundledMutualFundItemDomain ->
                    navController.openFund(
                        mfProductId = fund.id,
                        isin = fund.isin,
                        fundName = fund.scheme_name,
                        fundSubtitle = listOf(fund.risk_name, fund.asset_type, fund.scheme_type)
                            .filter { it.isNotBlank() }
                            .distinct()
                            .joinToString(" \u00B7 ")
                    )
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
                navigateToSIPDetailsScreen = {
                    navController.navigate(Route.FolioFundScreen(it.folio, it.actualFolio)) {
                        launchSingleTop = true
                    }
                },
                navigateToFDDetailsScreen = { id ->
                    navController.navigate(Route.FixedDepositDetails(id)) {
                        launchSingleTop = true
                    }
                },
                navigateToCategoryMutualFundTypeScreen = {
                    navController.navigate(Route.CategoryMutualFund) {
                        launchSingleTop = true
                    }
                },
                navigateToFundsByAmountType = { amountType: String ->
                    navController.navigate(
                        Route.MutualFundSearchResult(amountType = amountType)
                    ) {
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
                navigateToFundPurchase = { fund: MutualFundDomain ->
                    navController.openFund(fund)
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

                },
                navigateToFD = {
                    navController.navigate(
                        Route.FixedDepositSearchResult()
                    ) {
                        launchSingleTop = true
                    }
                },
                navigateToKYC = {stage->
                    navigateToKYC(stage)
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
                navigateToTransactionHistory = {
                    navController.navigate(Route.TransactionHistory){
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
                        is SetInvestmentDetailsEffect.LaunchWebView -> {
                            navController.navigate(
                                Route.WebViewScreen(
                                    url = effect.url,
                                    exitUrlPatterns = emptyList(),
                                    title = "Complete Payment",
                                    completionRouteKey = "fd_purchase"
                                )
                            )
                        }
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
                        ProjectedImpactEffect.NavigateToInvest -> navController.navigate(
                            Route.MutualFundSearchResult()
                        )
                        ProjectedImpactEffect.OpenPortfolioBottomSheet -> { /* handle */ }
                        ProjectedImpactEffect.ClosePortfolioBottomSheet -> { /* handle */ }
                        is ProjectedImpactEffect.ShowError -> {
                            SnackBarController.showError(effect.message)
                        }

                        ProjectedImpactEffect.NavigateToMapScheme -> {
                            navController.navigate(Route.MapSchemes(route.id)) {
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }
            ProjectedImpactScreen(
                state = uiState, handleEvent = vm::handleEvent
            )
        }

        composable<Route.MapSchemes> {
            val route = it.toRoute<Route.MapSchemes>()
            val vm: ProjectionImpactViewModel = koinViewModel(parameters = { parametersOf(route.id) })
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            MapSchemesScreen(
                uiState = uiState,
                effectFlow = vm.effect,
                onEvent = vm::handleEvent,
                onBack = { navController.popBackStack() }
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
                        ProfileSettingEffect.NavigateToChangePin -> {
                            if (prefs.isMpinSetup()) {
                                navController.navigate(
                                    Route.EnterPin(EnterPinPurpose.CHANGE_PIN)
                                ) {
                                    launchSingleTop = true
                                }
                            } else {
                                navController.navigate(Route.ChangePin) {
                                    launchSingleTop = true
                                }
                            }
                        }
                        ProfileSettingEffect.NavigateToBiometricLogin -> {
                            navController.navigate(Route.BiometricLogin) {
                                launchSingleTop = true
                            }
                        }
                        ProfileSettingEffect.NavigateToNotification -> {
                            navController.navigate(
                                Route.Notifications
                            ){
                                launchSingleTop=true
                            }
                        }
                        ProfileSettingEffect.NavigateToPrivacyPolicy -> {
                            navController.navigate(Route.PrivacyPolicy){
                                launchSingleTop=true
                            }
                        }
                        ProfileSettingEffect.NavigateToTermsOfService -> {
                            navController.navigate(Route.TermsAndConditions){
                                launchSingleTop=true
                            }
                        }
                    }
                }
            }

            ProfileSettingScreen(
                state = state ,
                onEvent = vm::handleEvent
            )
        }

        composable<Route.PrivacyPolicy> {
            PrivacyPolicyScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable<Route.TermsAndConditions> {
            TermsAndConditionsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.Notifications> {
            NotificationScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.TransactionHistory> {
            val vm: TransactionHistoryViewModel = koinViewModel()
            val state by vm.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(vm.effect) {
                vm.effect.collect { effect ->
                    when (effect) {
                        TransactionHistoryEffect.NavigateBack -> navController.popBackStack()
                    }
                }
            }

            TransactionHistoryScreen(
                state = state,
                onEvent = vm::handleEvent
            )
        }

        //Portfolio

        composable<Route.ExistingFundScreen> {
            ExistingFundScreenRoot(
                onBack = { navController.popBackStack() },
                // The buy screen is keyed on an ISIN and has no folio, and the portfolio
                // endpoints carry neither — so a top-up has nowhere to go while the details
                // screen is off-limits.
                onFundClick = { _, _ -> },
            )
        }

        composable<Route.ExistingFundLumpSumScreen> {
            val lumpSumWebViewReturned by it.savedStateHandle
                .getStateFlow(EXISTING_FUND_LUMPSUM_WEBVIEW_RESULT, false)
                .collectAsStateWithLifecycle()
            ExistingFundLumpSumScreen(
                onBack = { navController.popBackStack() },
                onLaunchWebView = { url ->
                    navController.navigate(
                        Route.WebViewScreen(
                            url = url,
                            exitUrlPatterns = emptyList(),
                            title = "Complete Payment",
                            completionRouteKey = "existing_fund_lumpsum"
                        )
                    )
                },
                webViewReturned = lumpSumWebViewReturned,
                onWebViewConsumed = {
                    it.savedStateHandle[EXISTING_FUND_LUMPSUM_WEBVIEW_RESULT] = false
                },
            )
        }

        composable<Route.FDPortfolioDetailsScreen> {
            val id = it.toRoute<Route.FDPortfolioDetailsScreen>().id
            val fdWebViewReturned by it.savedStateHandle
                .getStateFlow(FD_DETAILS_WEBVIEW_RESULT, false)
                .collectAsStateWithLifecycle()
            FDPortfolioDetailsScreen(
                id = id,
                onBackClick = { navController.popBackStack() },
                onLaunchWebView = { url ->
                    navController.navigate(
                        Route.WebViewScreen(
                            url = url,
                            exitUrlPatterns = emptyList(),
                            title = "Fixed Deposit",
                            completionRouteKey = "fd_details"
                        )
                    )
                },
                webViewReturned = fdWebViewReturned,
                onWebViewConsumed = {
                    it.savedStateHandle[FD_DETAILS_WEBVIEW_RESULT] = false
                },
            )
        }

        composable<Route.FolioFundScreen> {
            val id = it.toRoute<Route.FolioFundScreen>().folioId
            val actualFolio = it.toRoute<Route.FolioFundScreen>().actualFolio
            FolioFundMFScreen(
                folioId = id,
                actualFolio=actualFolio,
                onBack = {
                    navController.popBackStack()
                },
                onFundClick = {
                    navController.navigate(Route.SIPPortfolioDetails(
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
                    )) {
                        launchSingleTop = true
                    }
                },
                // See ExistingFundScreen: a folio top-up has no destination for now.
                onTopUp = { _, _ -> },
            )
        }

        composable<Route.SIPPortfolioDetails> {
            val data = it.toRoute<Route.SIPPortfolioDetails>()
            val sipWebViewReturned by it.savedStateHandle
                .getStateFlow(SIP_DETAILS_WEBVIEW_RESULT, false)
                .collectAsStateWithLifecycle()
            MFPortfolioDetailsScreen(
                onBackClick = { navController.popBackStack() },
                data = data,
                onLaunchWebView = { url ->
                    navController.navigate(
                        Route.WebViewScreen(
                            url = url,
                            exitUrlPatterns = emptyList(),
                            title = "Withdraw Fund",
                            completionRouteKey = "sip_details"
                        )
                    )
                },
                webViewReturned = sipWebViewReturned,
                onWebViewConsumed = {
                    it.savedStateHandle[SIP_DETAILS_WEBVIEW_RESULT] = false
                },
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

        composable<Route.WebViewScreen> {
            val route = it.toRoute<Route.WebViewScreen>()

            val onWebViewDone: () -> Unit = {
                when (route.completionRouteKey) {
                    "sip_details" -> {
                        // Come back to the details screen so it can refresh the portfolio and close itself.
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(SIP_DETAILS_WEBVIEW_RESULT, true)
                        navController.popBackStack()
                    }

                    "existing_fund_lumpsum" -> {
                        // Come back to the fund list so it can reload the portfolio.
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(EXISTING_FUND_LUMPSUM_WEBVIEW_RESULT, true)
                        navController.popBackStack()
                    }

                    "cart" -> {
                        // Come back to the cart and run the queued payment follow-up.
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(CART_WEBVIEW_RESULT, true)
                        navController.popBackStack()
                    }

                    WEBVIEW_COMPLETION_PURCHASE_PAYMENT -> {
                        // Come back to the purchase screen so it can read the purchase back and
                        // decide whether the payment actually landed.
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(PURCHASE_PAYMENT_RESULT, true)
                        navController.popBackStack()
                    }

                    WEBVIEW_COMPLETION_ADD_MANDATE -> {
                        // Come back to the autopay screen so it can confirm with the bank.
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(ADD_MANDATE_WEBVIEW_RESULT, true)
                        navController.popBackStack()
                    }

                    "fd_purchase" -> {
                        // Payment is done; the purchase form used to close itself right after
                        // handing the URL off, so drop it along with the web view.
                        navController.popBackStack<Route.PurchaseFixedDeposit>(inclusive = true)
                    }

                    "fd_details" -> {
                        // Come back to the FD details so it can reload the deposit.
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(FD_DETAILS_WEBVIEW_RESULT, true)
                        navController.popBackStack()
                    }

                    else -> navController.popBackStack()
                }
            }

            WebViewScreen(
                config = WebViewConfig(
                    url = route.url,
                    exitUrlPatterns = route.exitUrlPatterns,
                    matchType = WebViewUrlMatchType.valueOf(route.matchType),
                    title = route.title
                ),
                onExitUrlReached = { onWebViewDone() },
                onBackClick = { onWebViewDone() }
            )
        }

    }
}

/** Tapping a fund anywhere in the app opens the buy screen. */
private fun NavHostController.openFund(fund: MutualFundDomain) {
    openFund(
        mfProductId = fund.id,
        isin = fund.isin.orEmpty(),
        fundName = fund.name,
        fundSubtitle = fund.purchaseSubtitle
    )
}

private fun NavHostController.openFund(
    mfProductId: String,
    isin: String,
    fundName: String,
    fundSubtitle: String
) {
    navigate(
        Route.FundPurchase(
            mfProductId = mfProductId,
            isin = isin,
            fundName = fundName,
            fundSubtitle = fundSubtitle
        )
    ) {
        launchSingleTop = true
    }
}

/**
 * "Very High · Equity: Large Cap" for the buy screen's header. The listing endpoint leaves these
 * fields blank for some funds, so anything empty is dropped and the screen falls back to the
 * fund house name from the scheme lookup.
 */
private val MutualFundDomain.purchaseSubtitle: String
    get() = listOf(riskText.orEmpty(), category, type)
        .filter { it.isNotBlank() }
        .distinct()
        .joinToString(" · ")
