package org.velvetinvesting.jantanivesh.app.features.core.domain.repository


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

    fun setFirstLaunch(firstLaunch: Boolean)

    fun isFirstLaunch(): Boolean

    fun clearAuth()
}