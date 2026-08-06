package org.velvetinvesting.jantanivesh.app.features.login.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.login.domain.model.LoginDomain
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.NotificationDomain

interface UserAuth {
    suspend fun loginWithNumber(number: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun verifyOTP(number: String, otp: String): NetworkResponse<LoginDomain, ErrorDomain>

    suspend fun getNotifications(): NetworkResponse<List<NotificationDomain>, ErrorDomain>

    suspend fun getUnreadStatus(): NetworkResponse<Boolean, ErrorDomain>

    suspend fun markNotificationsAsRead(): NetworkResponse<Unit, ErrorDomain>
}
