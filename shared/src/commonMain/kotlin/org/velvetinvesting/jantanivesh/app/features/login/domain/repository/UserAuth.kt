package org.velvetinvesting.jantanivesh.app.features.login.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.login.domain.model.LoginDomain

interface UserAuth {
    suspend fun loginWithNumber(number: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun verifyOTP(number: String, otp: String): NetworkResponse<LoginDomain, ErrorDomain>
}
