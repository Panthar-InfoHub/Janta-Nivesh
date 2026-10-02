package org.velvetinvesting.jantanivesh.app.features.bundles.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.repository.BundlesRepository

class GetBundleDetailsUseCase(
    private val repository: BundlesRepository
) {
    suspend operator fun invoke(bundleId: String): NetworkResponse<BundleDetailsDomain, ErrorDomain> =
        repository.getBundleDetails(bundleId)
}
