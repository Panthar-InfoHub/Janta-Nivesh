package org.velvetinvesting.jantanivesh.app.features.bundles.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.mapper.toDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.AllBundlesDto
import org.velvetinvesting.jantanivesh.app.features.bundles.data.remote.model.BundleDetailsDto
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.models.BundleSummaryDomain
import org.velvetinvesting.jantanivesh.app.features.bundles.domain.repository.BundlesRepository

class BundlesRepo(
    private val client: HttpClient
) : BundlesRepository {

    override suspend fun getAllBundles(): NetworkResponse<List<BundleSummaryDomain>, ErrorDomain> {
        val response = safeRequest<AllBundlesDto> {
            client.get(getUrl("/bundles"))
        }

        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun getBundleDetails(
        bundleId: String
    ): NetworkResponse<BundleDetailsDomain, ErrorDomain> {
        val response = safeRequest<BundleDetailsDto> {
            client.get(getUrl("/bundles/$bundleId"))
        }

        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

}
