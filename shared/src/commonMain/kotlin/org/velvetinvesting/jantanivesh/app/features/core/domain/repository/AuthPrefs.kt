package org.velvetinvesting.jantanivesh.app.features.core.domain.repository

import org.velvetinvesting.jantanivesh.app.core.domain.model.OnboardingStage

interface AuthPrefs {

    fun setBearerToken(token: String)

    fun getBearerToken(): String?

    fun setRefreshToken(token: String)

    fun getRefreshToken(): String?

    fun setUserId(userId: String)

    fun getUserId(): String?

    fun setLoggedIn(isLoggedIn: Boolean)

    fun isLoggedIn(): Boolean

    fun setOnboardingCompleted(completed: Boolean)

    fun isOnboardingCompleted(): Boolean

    fun setOnboardingStage(stage: String)

    fun getOnboardingStage(): String?

    /**
     * Records progress made inside the onboarding flow, but never moves the stored stage back: a
     * status read can lag behind a step the user has already finished, and storing it would send a
     * relaunch to that finished screen. Login writes the server's stage with [setOnboardingStage]
     * instead, so the server still decides where a fresh session starts.
     */
    fun advanceOnboardingStage(stage: OnboardingStage)

    fun setPhoneNumber(phoneNumber: String)

    fun getPhoneNumber(): String?

    fun setFullName(fullName: String)

    fun getFullName(): String?

    fun setDob(dob: String)

    fun getDob(): String?

    fun setEmail(email: String)

    fun getEmail(): String?

    /** Set once the email OTP has been verified; the address is then no longer the user's to edit. */
    fun setEmailVerified(verified: Boolean)

    fun isEmailVerified(): Boolean

    /**
     * Whether the user has left the biometric shortcut switched on for the app lock. Defaults to
     * on so the prompt keeps appearing for anyone who had it before the setting existed; the
     * Biometric Login screen is the only thing that writes it.
     */
    fun setBiometricLoginEnabled(enabled: Boolean)

    fun isBiometricLoginEnabled(): Boolean

    fun setFirstLaunch(firstLaunch: Boolean)

    fun isFirstLaunch(): Boolean

    fun setMpinEnabled(enabled: Boolean)

    fun isMpinEnabled(): Boolean

    fun setMpinSetup(setup: Boolean)

    fun isMpinSetup(): Boolean

    fun clearAuth()
}