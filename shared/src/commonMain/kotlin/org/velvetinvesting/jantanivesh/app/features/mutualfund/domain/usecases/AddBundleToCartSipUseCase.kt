package org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.usecases
import org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundlecart.AddBundleSipRequest
import org.velvetinvesting.jantanivesh.app.features.mutualfund.domain.repository.MutualFundRepository

class AddBundleToCartSipUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        request: AddBundleSipRequest
    ) = repository.addBundleToCartSip(request)
}
