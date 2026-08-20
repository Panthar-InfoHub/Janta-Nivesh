package org.velvetinvesting.jantanivesh.app.core.navigation

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewUrlMatchType

@Serializable
sealed interface Route {

    @Serializable
    data object LoginGraph : Route

    @Serializable
    data object ChooseLanguage : Route

    @Serializable
    data object LoginWithPhone : Route

    @Serializable
    data class EnterOtp(val phoneNumber: String) : Route

    @Serializable
    data object SplashScreen : Route

    @Serializable
    data class OnboardingGraph(val stage: String) : Route


    // KYC Routes
    @Serializable
    data object KycGraph : Route

    @Serializable
    data object MainAppGraph : Route

    // Plans flow
    @Serializable
    data object PlansHome : Route

    @Serializable
    data object ChoosePlan : Route

    /** Everything shown on the success screen, carried from the confirm response. */
    @Serializable
    data class PurchaseSuccess(
        val schemeName: String,
        val amount: String,
        val installmentDay: Int,
        val startDate: String
    ) : Route

    @Serializable
    data object Redeem : Route

    @Serializable
    data object CategoryMutualFund : Route

    @Serializable
    data class MutualFundSearchResult(val search: String = "", val fundCategory: String? = null) : Route

    @Serializable
    data class MutualFundDetails(val id: String, val folioId: String? = null) : Route

    @Serializable
    data object CartScreen : Route

    @Serializable
    data object MutualFundTypeSelectionScreen : Route

    @Serializable
    data class BundleResultScreen(val bundleKey: String) : Route

    @Serializable
    data object AllBundleScreen : Route

    @Serializable
    data object ExistingFundScreen : Route

    @Serializable
    data object ExistingFundLumpSumScreen : Route

    @Serializable
    data class FolioFundScreen(val folioId: String, val actualFolio: String): Route

    @Serializable
    data class SIPPortfolioDetails(
        val id: Int,
        val title: String,
        val category: String,
        val amount: Double,
        val isSip: Boolean,
        val startDate: String,
        val returnPercentage: String,
        val returnAmount: Int,
        val xirr: String,
        val currentNav: Double,
        val avgNav: Double,
        val folio: String,
        val balanceUnits: Double,
        val img_url: String? = "",
        val orderId: String,
        val actualFolio: String
    ):Route

    @Serializable
    data class SIPCancellationScreen(val id: String) : Route

    @Serializable
    data class CancelSIPReason(val id: String) : Route


    @Serializable
    data object BottomNav
    @Serializable
    data object Home
    @Serializable
    data object FundScreener
    @Serializable
    data object PortFolio
    @Serializable
    data object Insurance
    @Serializable
    data object Profile

    @Serializable
    data object HealthInsurance
    @Serializable
    data object TermInsurance
    @Serializable
    data object OtherInsurance

    @Serializable
    data object RequestCallBack : Route

    @Serializable
    data class FixedDepositDetails(val id: String) : Route

    @Serializable
    data class FixedDepositSearchResult(
        val search: String? = null
    ) : Route

    @Serializable
    data class PurchaseFixedDeposit(val id:String)

    @Serializable
    data object GoalsScreen : Route

    @Serializable
    data object Notifications : Route

    @Serializable
    data object SingleGoalAdd : Route

    @Serializable
    data class GoalProjectionFlow(
        val id: String
    ) : Route

    @Serializable
    data class MapSchemes(
        val id: String
    ) : Route

    @Serializable
    data object PrivacyPolicy : Route

    @Serializable
    data object TermsAndConditions : Route

    @Serializable
    data object AboutUs : Route

    @Serializable
    data object AboutVelvet : Route

    @Serializable
    data object AboutFire : Route

    @Serializable
    data class FDPortfolioDetailsScreen(
        val id: String
    ) : Route

    @Serializable
    data object LanguageSelectionSettings : Route

    @Serializable
    data object ProfileSettingsScreen : Route

    @Serializable
    data class WebViewScreen(
        val url: String,
        val exitUrlPatterns: List<String> = emptyList(),
        val matchType: String = WebViewUrlMatchType.CONTAINS.name,
        val title: String? = null,
        val completionRouteKey: String? = null
    ): Route

    @Serializable
    data object OnboardingKYCSplash: Route
    @Serializable
    data object OnboardingEmail: Route

    /**
     * [email] rides along on the route so this screen can show the address back to the user and
     * resend to it, without re-reading it from the server.
     */
    @Serializable
    data class OnboardingEmailOtp(val email: String): Route
    @Serializable
    data object OnboardingBasicDetails: Route
    @Serializable
    data object OnboardingPANVerification: Route

    @Serializable
    data object OnboardingKYCInitiation: Route
    @Serializable
    data object OnboardingSignatureUpload: Route
    @Serializable
    data object OnboardingBankVerification: Route
    @Serializable
    data object OnboardingNominee: Route

    /** [email] is collected on [OnboardingEmail], which always runs immediately before this. */
    @Serializable
    data object OnboardingProfile: Route
    @Serializable
    data object OnboardingAutopay: Route
}
