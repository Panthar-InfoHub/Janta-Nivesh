package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class VerifyCartSipCheckoutOtpUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(batchId: String, otp: String): NetworkResponse<Unit, ErrorDomain> {
        return repository.verifySipCheckoutOtp(batchId = batchId, otp = otp)
    }
}
