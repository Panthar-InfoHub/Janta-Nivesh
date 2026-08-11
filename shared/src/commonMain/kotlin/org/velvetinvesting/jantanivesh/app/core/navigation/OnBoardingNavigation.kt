package org.velvetinvesting.jantanivesh.app.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewConfig
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewScreen
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewUrlMatchType
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose.AddNomineeScreen
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose.ConfirmYourDetailsScreen
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose.EmailIdScreen
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose.KycSplashScreen
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose.ReviewProfileScreen
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose.SetupAutopayScreen
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose.UploadSignatureScreen
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose.VerifyBankAccountScreen
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.compose.VerifyWithDigilockerScreen
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.AddNomineeEffect
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.AddNomineeViewModel
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.ConfirmYourDetailsEffect
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.ConfirmYourDetailsViewModel
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.EmailIdEffect
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.EmailIdViewModel
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.KycSplashEffect
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.KycSplashViewModel
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.ReviewProfileEffect
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.ReviewProfileEvent
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.ReviewProfileViewModel
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.SetupAutopayEffect
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.SetupAutopayEvent
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.SetupAutopayViewModel
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.UploadSignatureEffect
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.UploadSignatureViewModel
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.VerifyBankAccountEffect
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.VerifyBankAccountViewModel
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.VerifyWithDigilockerEffect
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.VerifyWithDigilockerEvent
import org.velvetinvesting.jantanivesh.app.features.kycnew.ui.viewmodels.VerifyWithDigilockerViewModel

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
    val startDestination = when (OnboardingStage.fromIdOrDefault(stage)) {
        OnboardingStage.Nominee -> Route.OnboardingNominee
        OnboardingStage.PennyDrop -> Route.OnboardingBankVerification
        OnboardingStage.Profile -> Route.OnboardingEmail
        OnboardingStage.Readiness -> Route.OnboardingKYCSplash
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

            // Always precedes the investor profile, which prefills the address it collects.
            composable<Route.OnboardingEmail> {
                val vm: EmailIdViewModel = koinViewModel()
                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            is EmailIdEffect.EmailSubmitted -> {
                                navController.navigate(Route.OnboardingProfile(effect.email)) {
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

            composable<Route.OnboardingPANVerification> {
                val vm: ConfirmYourDetailsViewModel = koinViewModel()
                LaunchedEffect(Unit) {
                    vm.effect.collect { effect ->
                        when (effect) {
                            ConfirmYourDetailsEffect.PanVerified -> {
                                navController.navigate(Route.OnboardingBankVerification){
                                    launchSingleTop=true
                                }
                            }
                            ConfirmYourDetailsEffect.NavigateToKycInitiate -> {
                                navController.navigate(Route.OnboardingKYCInitiation){
                                    launchSingleTop= true
                                }
                            }
                            ConfirmYourDetailsEffect.OpenPrivacyUrl -> {}
                            ConfirmYourDetailsEffect.OpenReadMoreUrl -> {}
                            ConfirmYourDetailsEffect.OpenTermsUrl -> {}
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
                VerifyWithDigilockerScreen(
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
                            // The email screen sits in between and hands the address on.
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
                val route = entry.toRoute<Route.OnboardingProfile>()
                val vm: ReviewProfileViewModel = koinViewModel(
                    parameters = { parametersOf(route.email) }
                )

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
                    handleEvent = vm::handleEvent
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
