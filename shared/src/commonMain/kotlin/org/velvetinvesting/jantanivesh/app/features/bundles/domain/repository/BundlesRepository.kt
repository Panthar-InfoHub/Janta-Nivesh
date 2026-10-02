package org.velvetinvesting.jantanivesh.app.features.bundles.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleSummaryDomain

interface BundlesRepository {

    /** `GET /bundles`. */
    suspend fun getAllBundles(): NetworkResponse<List<BundleSummaryDomain>, ErrorDomain>

    /** `GET /bundles/{id}`. */
    suspend fun getBundleDetails(bundleId: String): NetworkResponse<BundleDetailsDomain, ErrorDomain>
}
