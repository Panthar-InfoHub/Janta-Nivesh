package org.velvetinvesting.jantanivesh.app.features.fd.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.velvetinvesting.jantanivesh.app.features.fd.data.mapper.toDomain
import org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto.FixedDepositListDto
import org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto.FDDetailsDto
import org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto.PurchaseFDBodyDto
import org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto.PurchaseFDDto
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FixedDepositDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.PaginatedData
import org.velvetinvesting.jantanivesh.app.features.fd.domain.repository.FixedDepositRepository
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.networking.getUrl
import org.velvetinvesting.jantanivesh.app.core.networking.safeRequest
class FixedDepositRepo(
    private val client: HttpClient
): FixedDepositRepository {

    override suspend fun getFDSearchResult(
        maxDeposit: Double?,
        minDeposit: Double?,
        payoutFrequency: String?,
        tenure: String?,
        limit: Int?,
        page: Int?,
        search: String?
    ): NetworkResponse<PaginatedData<FixedDepositDomain>, ErrorDomain> {
        val response= safeRequest<FixedDepositListDto> {
            client.get(
                getUrl("/fd")
            ) {
                parameter("max_deposit", maxDeposit)
                parameter("min_deposit", minDeposit)
                parameter("payout_frequency", payoutFrequency)
                parameter("tenure", tenure)
                parameter("limit", limit)
                parameter("page", page)
                parameter("search", search)
            }
        }
        when(response){
            is NetworkResponse.Error -> {
                return response
            }
            is NetworkResponse.Success -> {
                val data=response.data
                return NetworkResponse.Success(data.toDomain())
            }
        }
    }

    override suspend fun getFDDetails(id: String): NetworkResponse<FDDetailsDomain, ErrorDomain> {
        val response= safeRequest<FDDetailsDto> {
            client.get(
                getUrl("/fd/$id")
            )
        }
        when (response) {
            is NetworkResponse.Error -> {
                return response
            }
            is NetworkResponse.Success -> {
                val data = response.data
                return NetworkResponse.Success(data.toDomain())
            }
        }
    }

    override suspend fun purchaseFD(data: PurchaseFDBodyDto): NetworkResponse<String, ErrorDomain> {
        val response= safeRequest<PurchaseFDDto> {
            client.post (
                getUrl("/fd/purchase-url")
            ){
                setBody(data)
            }
        }
        when (response) {
            is NetworkResponse.Error -> {
                return response
            }
            is NetworkResponse.Success -> {
                val data = response.data
                return NetworkResponse.Success(data.data)
            }
        }
    }
}