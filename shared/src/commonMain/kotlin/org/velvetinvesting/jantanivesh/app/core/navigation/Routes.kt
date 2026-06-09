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
}
