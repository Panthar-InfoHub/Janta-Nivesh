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

    @Serializable
    data object MainAppFlow : Route
}
