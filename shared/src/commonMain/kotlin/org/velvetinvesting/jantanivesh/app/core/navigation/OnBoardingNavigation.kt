package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.velvetinvesting.jantanivesh.app.core.domain.model.OnboardingStage
import org.velvetinvesting.jantanivesh.app.core.utils.SnackBarController
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewConfig
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewScreen
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewUrlMatchType
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.AddNomineeScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.BasicDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.ConfirmYourDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.EmailIdScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.EmailOtpScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.KycSplashScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.NomineeOptOutTermsScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.ReviewProfileScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.SetupAutopayScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.UploadSignatureScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.VerifyBankAccountScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.compose.DigiLockerSplashScreen
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddNomineeEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.AddNomineeViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.BasicDetailsEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.BasicDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.PanVerificationEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.PanVerificationViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EmailIdEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EmailIdViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EmailOtpEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.EmailOtpViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.KycSplashEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.KycSplashViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ReviewProfileEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ReviewProfileEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.ReviewProfileViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.SetupAutopayEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.SetupAutopayEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.SetupAutopayViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.UploadSignatureEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.UploadSignatureViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.VerifyBankAccountEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.VerifyBankAccountViewModel
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.VerifyWithDigilockerEffect
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.VerifyWithDigilockerEvent
import org.velvetinvesting.jantanivesh.app.features.onboarding.ui.viewmodels.VerifyWithDigilockerViewModel

/**
 * Set on whichever entry launched a web view once the user comes back, so that screen can
 * re-check the server and decide what happens next: DigiLocker (from KYC initiation), eSign
 * (from the investor profile) and mandate authorization (from autopay) all use it.
 */
private const val KYC_STEP_RESULT = "onboarding_kyc_step_completed"

/** Marks a web view that should pop back to its caller and flag [KYC_STEP_RESULT]. */
private const val WEBVIEW_COMPLETION_KYC_STEP = "onboarding_kyc_step"

@Composable
fun OnboardingNavigation(
    onCompleted: () -> Unit,
    stage: String
) {

    val navController = rememberNavController()
    // `resumePoint` has already folded away the stages with no screen of their own, so every
    // remaining branch maps to exactly one destination.
    val startDestination = when (OnboardingStage.resumePoint(stage)) {
        OnboardingStage.BasicDetails -> Route.OnboardingBasicDetails
        OnboardingStage.PanVerification -> Route.OnboardingPANVerification
        OnboardingStage.PennyDropVerification -> Route.OnboardingBankVerification
        OnboardingStage.EmailVerification -> Route.OnboardingEmail
        OnboardingStage.InvestorProfile -> Route.OnboardingProfile
        OnboardingStage.NomineeAddition -> Route.OnboardingNominee
        OnboardingStage.AutopaySetup -> Route.OnboardingAutopay
        else -> null
    }

    if (startDestination == null) {
        LaunchedEffect(Unit) {
            onCompleted()
        }
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { pv ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize()
        ) {
            composable<Route.OnboardingKYCSplash> {
                val vm: KycSplashViewModel = koinViewModel()
                LaunchedEffect(Unit){
                    vm.effect.collect { effect ->
                        when(effect){
                            KycSplashEffect.OnProceedClick -> {
                                navController.navigate(Route.OnboardingPANVerification){
                                    launchSingleTop=true
                                }
                            }
                        }
                    }
                }

                KycSplashScreen(
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.OnboardingEmail> {
                val vm: EmailIdViewModel = koinViewModel()
                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            is EmailIdEffect.OtpRequested -> {
                                navController.navigate(Route.OnboardingEmailOtp(effect.email)) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                EmailIdScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.OnboardingEmailOtp> { entry ->
                val route = entry.toRoute<Route.OnboardingEmailOtp>()
                val vm: EmailOtpViewModel = koinViewModel(
                    parameters = { parametersOf(route.email) }
                )

                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            EmailOtpEffect.EmailVerified -> {
                                navController.navigate(Route.OnboardingProfile) {
                                    // The address is confirmed — going back to re-enter the code
                                    // would only hit an already-consumed OTP.
                                    popUpTo(Route.OnboardingEmail) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }

                            EmailOtpEffect.NavigateBack -> navController.popBackStack()
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                EmailOtpScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.OnboardingBasicDetails> {
                val vm: BasicDetailsViewModel = koinViewModel()
                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            BasicDetailsEffect.NavigateToPanInitiate -> {
                                navController.navigate(Route.OnboardingPANVerification){
                                    launchSingleTop=true
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                BasicDetailsScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.OnboardingPANVerification> {
                val vm: PanVerificationViewModel = koinViewModel()
                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            PanVerificationEffect.PanVerified -> {
                                navController.navigate(Route.OnboardingBankVerification){
                                    launchSingleTop=true
                                }
                            }
                            PanVerificationEffect.NavigateToKycInitiate -> {
                                navController.navigate(Route.OnboardingKYCInitiation){
                                    launchSingleTop= true
                                }
                            }
                            PanVerificationEffect.OpenPrivacyUrl -> {}
                            PanVerificationEffect.OpenReadMoreUrl -> {}
                            PanVerificationEffect.OpenTermsUrl -> {}
                            PanVerificationEffect.SkipPanVerification -> {
                                SnackBarController.showSuccess("KYC Skipped. Please complete the remaining steps before making fund purchases.")
                                onCompleted()
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                ConfirmYourDetailsScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.OnboardingKYCInitiation> { entry ->
                val vm: VerifyWithDigilockerViewModel = koinViewModel()

                val stepReturned by entry.savedStateHandle
                    .getStateFlow(KYC_STEP_RESULT, false)
                    .collectAsStateWithLifecycle()

                LaunchedEffect(stepReturned) {
                    if (stepReturned) {
                        entry.savedStateHandle[KYC_STEP_RESULT] = false
                        vm.handleEvent(VerifyWithDigilockerEvent.OnStepReturned)
                    }
                }

                LaunchedEffect(Unit){
                    vm.effect.collect { effect ->
                        when(effect){
                            is VerifyWithDigilockerEffect.OpenWebView -> {
                                navController.navigate(
                                    Route.WebViewScreen(
                                        url = effect.url,
                                        title = effect.title,
                                        completionRouteKey = WEBVIEW_COMPLETION_KYC_STEP
                                    )
                                )
                            }

                            VerifyWithDigilockerEffect.ProofVerified -> {
                                navController.navigate(Route.OnboardingSignatureUpload) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                DigiLockerSplashScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.OnboardingSignatureUpload> {
                val vm: UploadSignatureViewModel = koinViewModel()

                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            UploadSignatureEffect.SignatureUploaded -> {
                                navController.navigate(Route.OnboardingBankVerification) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                UploadSignatureScreen(
                    state = state,
                    onEvent = vm::handleEvent,
                    onBack = { navController.popBackStack() }
                )
            }

            composable<Route.OnboardingBankVerification> {
                val vm: VerifyBankAccountViewModel = koinViewModel()

                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            VerifyBankAccountEffect.PennyDropCompleted -> {
                                navController.navigate(Route.OnboardingEmail) {
                                    launchSingleTop = true
                                }
                            }

                            VerifyBankAccountEffect.NavigateToChangeBankAccount -> {}
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                VerifyBankAccountScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.OnboardingProfile> { entry ->
                val vm: ReviewProfileViewModel = koinViewModel()

                val eSignReturned by entry.savedStateHandle
                    .getStateFlow(KYC_STEP_RESULT, false)
                    .collectAsStateWithLifecycle()

                LaunchedEffect(eSignReturned) {
                    if (eSignReturned) {
                        entry.savedStateHandle[KYC_STEP_RESULT] = false
                        vm.handleEvent(ReviewProfileEvent.OnESignReturned)
                    }
                }

                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            is ReviewProfileEffect.OpenESignWebView -> {
                                navController.navigate(
                                    Route.WebViewScreen(
                                        url = effect.url,
                                        title = "eSign Verification",
                                        completionRouteKey = WEBVIEW_COMPLETION_KYC_STEP
                                    )
                                )
                            }

                            ReviewProfileEffect.ProfileCompleted -> {
                                navController.navigate(Route.OnboardingNominee) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                ReviewProfileScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.OnboardingNominee> {
                val vm: AddNomineeViewModel = koinViewModel()

                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            AddNomineeEffect.NomineesSubmitted -> {
                                navController.navigate(Route.OnboardingAutopay) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                AddNomineeScreen(
                    state = state,
                    handleEvent = vm::handleEvent,
                    onOptOutTermsClick = {
                        navController.navigate(Route.NomineeOptOutTerms) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable<Route.NomineeOptOutTerms> {
                NomineeOptOutTermsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable<Route.OnboardingAutopay> { entry ->
                val vm: SetupAutopayViewModel = koinViewModel()

                val authorizationReturned by entry.savedStateHandle
                    .getStateFlow(KYC_STEP_RESULT, false)
                    .collectAsStateWithLifecycle()

                LaunchedEffect(authorizationReturned) {
                    if (authorizationReturned) {
                        entry.savedStateHandle[KYC_STEP_RESULT] = false
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
                                        completionRouteKey = WEBVIEW_COMPLETION_KYC_STEP
                                    )
                                )
                            }

                            SetupAutopayEffect.AutopayCompleted -> onCompleted()
                        }
                    }
                }

                val state by vm.uiState.collectAsStateWithLifecycle()
                SetupAutopayScreen(
                    state = state,
                    handleEvent = vm::handleEvent
                )
            }

            composable<Route.WebViewScreen> { entry ->
                val route = entry.toRoute<Route.WebViewScreen>()

                val onWebViewDone: () -> Unit = {
                    when (route.completionRouteKey) {
                        // Each hand-off returns to the screen that launched it, which then asks
                        // the server what actually changed.
                        WEBVIEW_COMPLETION_KYC_STEP -> navController.returnToCallerWithStepResult()
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
}

/** Pops back to whichever screen launched the web view and flags that its step is done. */
private fun NavHostController.returnToCallerWithStepResult() {
    previousBackStackEntry?.savedStateHandle?.set(KYC_STEP_RESULT, true)
    popBackStack()
}
