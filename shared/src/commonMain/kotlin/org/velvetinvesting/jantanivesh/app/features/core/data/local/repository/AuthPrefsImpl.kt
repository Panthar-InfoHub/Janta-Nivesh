package org.velvetinvesting.jantanivesh.app.features.core.data.local.repository

import org.velvetinvesting.jantanivesh.app.core.platform.SharedPreference
import org.velvetinvesting.jantanivesh.app.features.core.domain.repository.AuthPrefs


class AuthPrefsImpl(
    private val prefs: SharedPreference
) : AuthPrefs {

    companion object {
        private const val KEY_BEARER_TOKEN = "auth_bearer_token"
        private const val KEY_REFRESH_TOKEN = "auth_refresh_token"
        private const val KEY_USER_ID = "auth_user_id"
        private const val KEY_LOGIN_STATUS = "auth_login_status"

        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

        private const val KEY_USER_PHONE_NUMBER = "user_phone_number"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_ONBOARDING_STAGE = "onboarding_stage"
    }

    override fun setBearerToken(token: String) {
        prefs.setString(KEY_BEARER_TOKEN, token)
    }

    override fun getBearerToken(): String? {
        return prefs.getString(KEY_BEARER_TOKEN)
    }

    override fun setRefreshToken(token: String) {
        prefs.setString(KEY_REFRESH_TOKEN, token)
    }

    override fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH_TOKEN)
    }

    override fun setUserId(userId: String) {
        prefs.setString(KEY_USER_ID, userId)
    }

    override fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID)
    }

    override fun setLoggedIn(isLoggedIn: Boolean) {
        prefs.setBoolean(KEY_LOGIN_STATUS, isLoggedIn)
    }

    override fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_LOGIN_STATUS) ?: false
    }

    override fun setOnboardingCompleted(completed: Boolean) {
        prefs.setBoolean(KEY_ONBOARDING_COMPLETED, completed)
    }

    override fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED) ?: false
    }

    override fun setOnboardingStage(stage: String) {
        prefs.setString(KEY_ONBOARDING_STAGE, stage)
    }

    override fun getOnboardingStage(): String? {
        return prefs.getString(KEY_ONBOARDING_STAGE)
    }

    override fun setPhoneNumber(phoneNumber: String) {
        prefs.setString(KEY_USER_PHONE_NUMBER, phoneNumber)
    }

    override fun getPhoneNumber(): String? {
        return prefs.getString(KEY_USER_PHONE_NUMBER)
    }

    override fun setFirstLaunch(firstLaunch: Boolean) {
        prefs.setBoolean(KEY_FIRST_LAUNCH, firstLaunch)
    }

    override fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(KEY_FIRST_LAUNCH) ?: true
    }

    override fun clearAuth() {
        prefs.remove(KEY_BEARER_TOKEN)
        prefs.remove(KEY_REFRESH_TOKEN)
        prefs.remove(KEY_USER_ID)
        prefs.remove(KEY_LOGIN_STATUS)
        prefs.remove(KEY_ONBOARDING_COMPLETED)
        prefs.remove(KEY_USER_PHONE_NUMBER)
    }
}