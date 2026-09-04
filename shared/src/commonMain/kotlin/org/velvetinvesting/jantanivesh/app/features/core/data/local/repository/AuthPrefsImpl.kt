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

        private const val KEY_USER_FULL_NAME = "user_full_name"
        private const val KEY_USER_DOB = "user_dob"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_EMAIL_VERIFIED = "user_email_verified"

        private const val KEY_BIOMETRIC_LOGIN_ENABLED = "biometric_login_enabled"
        private const val KEY_MPIN_ENABLED = "mpin_enabled"
        private const val KEY_MPIN_SETUP = "mpin_setup"
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

    override fun setFullName(fullName: String) {
        prefs.setString(KEY_USER_FULL_NAME, fullName)
    }

    override fun getFullName(): String? {
        return prefs.getString(KEY_USER_FULL_NAME)
    }

    override fun setDob(dob: String) {
        prefs.setString(KEY_USER_DOB, dob)
    }

    override fun getDob(): String? {
        return prefs.getString(KEY_USER_DOB)
    }

    override fun setEmail(email: String) {
        prefs.setString(KEY_USER_EMAIL, email)
    }

    override fun getEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL)
    }

    override fun setEmailVerified(verified: Boolean) {
        prefs.setBoolean(KEY_USER_EMAIL_VERIFIED, verified)
    }

    override fun isEmailVerified(): Boolean {
        return prefs.getBoolean(KEY_USER_EMAIL_VERIFIED) ?: false
    }

    override fun setBiometricLoginEnabled(enabled: Boolean) {
        prefs.setBoolean(KEY_BIOMETRIC_LOGIN_ENABLED, enabled)
    }

    override fun isBiometricLoginEnabled(): Boolean {
        // Absent means the user has never visited the setting — keep the pre-existing behaviour.
        return prefs.getBoolean(KEY_BIOMETRIC_LOGIN_ENABLED) ?: true
    }

    override fun setFirstLaunch(firstLaunch: Boolean) {
        prefs.setBoolean(KEY_FIRST_LAUNCH, firstLaunch)
    }

    override fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(KEY_FIRST_LAUNCH) ?: true
    }

    override fun setMpinEnabled(enabled: Boolean) {
        prefs.setBoolean(KEY_MPIN_ENABLED, enabled)
    }

    override fun isMpinEnabled(): Boolean {
        return prefs.getBoolean(KEY_MPIN_ENABLED) ?: false
    }

    override fun setMpinSetup(setup: Boolean) {
        prefs.setBoolean(KEY_MPIN_SETUP, setup)
    }

    override fun isMpinSetup(): Boolean {
        return prefs.getBoolean(KEY_MPIN_SETUP) ?: false
    }

    override fun clearAuth() {
        prefs.remove(KEY_BEARER_TOKEN)
        prefs.remove(KEY_REFRESH_TOKEN)
        prefs.remove(KEY_USER_ID)
        prefs.remove(KEY_LOGIN_STATUS)
        prefs.remove(KEY_ONBOARDING_COMPLETED)
        // Otherwise the next user to log in on this device resumes at the previous one's stage
        // for as long as it takes the OTP response to overwrite it.
        prefs.remove(KEY_ONBOARDING_STAGE)
        prefs.remove(KEY_USER_PHONE_NUMBER)
        prefs.remove(KEY_USER_FULL_NAME)
        prefs.remove(KEY_USER_DOB)
        prefs.remove(KEY_USER_EMAIL)
        prefs.remove(KEY_USER_EMAIL_VERIFIED)
        // The next person to sign in on this device makes their own choice about biometrics.
        prefs.remove(KEY_BIOMETRIC_LOGIN_ENABLED)
        prefs.remove(KEY_MPIN_ENABLED)
        prefs.remove(KEY_MPIN_SETUP)
    }
}