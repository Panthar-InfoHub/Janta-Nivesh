package org.velvetinvesting.jantanivesh.app.core.navigation

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.core.webview.WebViewUrlMatchType
import org.velvetinvesting.jantanivesh.app.features.auth.ui.viewmodels.EnterPinPurpose

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
    /**
     * Everything shown on the success screen, carried from the confirm response. [mode] is a
     * [org.velvetinvesting.jantanivesh.app.features.plans.domain.model.PurchaseMode] name, and
     * decides whether the screen reads as a SIP registration or a completed purchase.
     * [installmentDay] is zero when the mode has no debit day.
     */
    @Serializable
    data class PurchaseSuccess(
        val mode: String,
        val schemeName: String,
        val amount: String,
        val installmentDay: Int,
        val startDate: String
    ) : Route

    /**
     * The buy screen. [mfProductId] is what the purchase endpoints take, while [isin] is what
     * the scheme lookup — and so every threshold on the screen — is keyed on. The two name
     * fields ride along so the header reads correctly before that lookup lands.
     */
    @Serializable
    data class FundPurchase(
        val mfProductId: String,
        val isin: String,
        val fundName: String = "",
        val fundSubtitle: String = "",
        val amountType: String? = null
    ) : Route

    /**
     * Autopay setup reached from outside onboarding — from the purchase screen, when the user
     * has no approved mandate to debit a SIP against. Same screen, different exit.
     */
    @Serializable
    data object AddMandate : Route

    /**
     * The redeem screen. Everything it needs travels on the route — it is reached from the order
     * details, which already holds the figures, so it loads nothing of its own.
     */
    @Serializable
    data class Redeem(
        val holdingId: String,
        val scheme: String,
        val folioNumber: String,
        val availableUnits: Double,
        val currentValue: Double,
        val isSip: Boolean
    ) : Route

    /**
     * Confirms a redemption that is already placed with the gateway. [redemptionId] is the
     * `fp_id` from `POST /mf/redemption/`.
     */
    @Serializable
    data class RedeemOtp(val redemptionId: String) : Route

    @Serializable
    data object CategoryMutualFund : Route

    /**
     * The fund list. [tag], [category] and [amountType] are the `GET /mf/funds` filters the
     * screen opens with — set when it is entered from a category tile or one of the home
     * micro-SIP cards, and null when the user is browsing everything.
     */
    @Serializable
    data class MutualFundSearchResult(
        val search: String = "",
        val tag: String? = null,
        val category: String? = null,
        val amountType: String? = null
    ) : Route

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

    /**
     * The order-details screen, for a SIP holding and a lumpsum one alike. Everything it renders
     * rides on the route: it is opened from the portfolio, which already has the numbers.
     */
    @Serializable
    data class SIPPortfolioDetails(
        val id: Int,
        /** `mf_holding_id` — what the redemption endpoints are keyed on. */
        val holdingId: String = "",
        val title: String,
        val category: String,
        val amount: Double,
        val isSip: Boolean,
        val startDate: String,
        val returnPercentage: String,
        val returnAmount: Double,
        val xirr: String,
        val currentNav: Double,
        val avgNav: Double,
        val folio: String,
        val balanceUnits: Double,
        val img_url: String? = "",
        val orderId: String,
        val actualFolio: String,
        /** Falls back to invested + return when the caller has no separate figure. */
        val currentValue: Double = 0.0,
        /**
         * Today's movement. The portfolio payload reports it for the whole mutual-fund book but
         * not per holding, so these read zero until it does.
         */
        val dayReturn: Double = 0.0,
        val dayReturnPercent: Double = 0.0,
        /** The badge on the header card. */
        val status: String = "ACTIVE"
    ):Route

    @Serializable
    data class SIPCancellationScreen(val id: String) : Route

    @Serializable
    data class CancelSIPReason(val id: String) : Route


    /**
     * PIN entry. Shown once per cold start as the app lock, and again — with [purpose] set to
     * [EnterPinPurpose.CHANGE_PIN] — in front of [ChangePin], which is why it carries a purpose
     * rather than being a single fixed destination.
     */
    @Serializable
    data class EnterPin(val purpose: String = EnterPinPurpose.APP_LOCK) : Route

    /** Sets a new PIN. Only ever entered through [EnterPin] with the change-PIN purpose. */
    @Serializable
    data object ChangePin : Route

    /** The switch controlling whether the app lock offers the biometric shortcut. */
    @Serializable
    data object BiometricLogin : Route

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
    data object TransactionHistory : Route

    @Serializable
    data object MyOrders : Route

    /**
     * One order, opened from the listing.
     *
     * There is no per-order endpoint, so everything the screen renders rides on the route — the
     * listing already holds it. The figures are non-null with zero defaults because the
     * navigation argument types have no nullable primitives; zero therefore stands for "the
     * payload did not report this", which is exactly what the screen shows as "--".
     */
    @Serializable
    data class OrderDetails(
        /** An [org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderPlanType] name. */
        val planType: String = "",
        /** An [org.velvetinvesting.jantanivesh.app.features.orders.domain.model.OrderState] name. */
        val state: String = "",
        val stateLabel: String = "",
        val systematic: Boolean = false,
        val fundName: String = "",
        val fundIconUrl: String = "",
        val fundCategory: String = "",
        /** Product identity, so a failed order can be placed again from the details screen. */
        val mfProductId: String = "",
        val isin: String = "",
        val folioNumber: String = "",
        val orderId: String = "",
        val transactionId: String = "",
        val amount: Double = 0.0,
        val units: Double = 0.0,
        val allottedUnits: Double = 0.0,
        val purchasedAmount: Double = 0.0,
        val purchasedPrice: Double = 0.0,
        val latestNav: Double = 0.0,
        val paymentMethod: String = "",
        val frequency: String = "",
        val reason: String = "",
        val createdAt: String = "",
        val submittedAt: String = "",
        val succeededAt: String = "",
        val failedAt: String = "",
        val allottedNavDate: String = "",
        /**
         * The account a redemption pays out to. The order payload does not resolve it, so this
         * is blank today and the payout card is simply left off until it is filled.
         */
        val payoutAccount: String = ""
    ) : Route

    @Serializable
    data object ActiveMandates : Route

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

    /** The full opt-out declaration, opened from the link on the nominee screen. */
    @Serializable
    data object NomineeOptOutTerms: Route

    /** [email] is collected on [OnboardingEmail], which always runs immediately before this. */
    @Serializable
    data object OnboardingProfile: Route
    @Serializable
    data object OnboardingAutopay: Route
}
