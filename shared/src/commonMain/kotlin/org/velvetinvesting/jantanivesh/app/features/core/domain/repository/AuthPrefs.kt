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

    fun setPhoneNumber(phoneNumber: String)

    fun getPhoneNumber(): String?

    fun setFirstLaunch(firstLaunch: Boolean)

    fun isFirstLaunch(): Boolean

    fun clearAuth()
}