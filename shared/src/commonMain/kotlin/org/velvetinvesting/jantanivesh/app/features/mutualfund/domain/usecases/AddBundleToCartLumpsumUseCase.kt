package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class AddBundleToCartLumpsumUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        bundleId: String,
        amount: Long
    ) = repository.addBundleToCartLumpsum(
        bundleId = bundleId,
        amount = amount
    )
}
