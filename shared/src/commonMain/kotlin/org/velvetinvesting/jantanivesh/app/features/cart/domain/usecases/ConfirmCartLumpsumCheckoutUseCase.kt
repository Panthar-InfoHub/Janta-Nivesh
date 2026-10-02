package org.velvetinvesting.jantanivesh.app.features.cart.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.cart.domain.models.LumpsumCheckoutPaymentDomain
import org.velvetinvesting.jantanivesh.app.features.cart.domain.repository.CartRepository

class ConfirmCartLumpsumCheckoutUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(
        batchId: String,
        otp: String,
        paymentPostbackUrl: String
    ): NetworkResponse<LumpsumCheckoutPaymentDomain, ErrorDomain> {
        return repository.confirmLumpsumCheckout(
            batchId = batchId,
            otp = otp,
            paymentPostbackUrl = paymentPostbackUrl
        )
    }
}
