package org.velvetinvesting.jantanivesh.app.features.bundles.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.repository.BundlesRepository

class GetAllBundlesUseCase(
    private val repository: BundlesRepository
) {
    suspend operator fun invoke(): NetworkResponse<List<BundleSummaryDomain>, ErrorDomain> =
        repository.getAllBundles()
}
