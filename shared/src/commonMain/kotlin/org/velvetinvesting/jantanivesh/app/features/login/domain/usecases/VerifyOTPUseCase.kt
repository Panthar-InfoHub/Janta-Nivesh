package org.velvetinvesting.jantanivesh.app.features.login.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.login.domain.repository.UserAuth

class VerifyOTPUseCase(private val repository: UserAuth) {
    suspend operator fun invoke(number: String, otp: String) = repository.verifyOTP(number, otp)
}
