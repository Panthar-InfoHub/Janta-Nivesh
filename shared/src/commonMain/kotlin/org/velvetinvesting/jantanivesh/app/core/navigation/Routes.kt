package org.velvetinvesting.jantanivesh.app.core.navigation

import kotlinx.serialization.Serializable

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
    data object OnboardingGraph : Route

    @Serializable
    data object EnterName : Route

    @Serializable
    data object EnterDob : Route

    @Serializable
    data object EnterEmail : Route


    // KYC Routes
    @Serializable
    data object KycGraph : Route

    @Serializable
    data object KycIntro : Route

    @Serializable
    data object KycForm : Route

    @Serializable
    data object KycImageUpload : Route

    @Serializable
    data object KycContract : Route

    @Serializable
    data object KycSuccess : Route

    @Serializable
    data object TradingAccountNavigation: Route

    @Serializable
    data object TradingAccountBasicDetails: Route
    @Serializable
    data object TradingAccountPANDetails: Route
    @Serializable
    data object TradingAccountFinancialDetails: Route
    @Serializable
    data object TradingAccountClientInfo: Route
    @Serializable
    data object TradingAccountBankDetails: Route
    @Serializable
    data object TradingAccountAddressDetails: Route
    @Serializable
    data object TradingAccountGuardianDetails: Route
    @Serializable
    data object TradingAccountGuardiansPANDetails: Route
    @Serializable
    data object TradingAccountSuccess: Route

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
    data class FolioFundScreen(val folioId: String) : Route

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
        val img_url: String? = ""
    ) : Route

    @Serializable
    data class SIPCancellationScreen(val id: String) : Route

    @Serializable
    data class CancelSIPReason(val id: String) : Route
}
