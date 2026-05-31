package org.velvetinvesting.jantanivesh.app.features.fd.domain.repository

import org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto.PurchaseFDBodyDto
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FixedDepositDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.PaginatedData
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse

interface FixedDepositRepository {

    suspend fun getFDSearchResult(
        maxDeposit: Double? = null,
        minDeposit: Double? = null,
        payoutFrequency: String? = null,
        tenure: String? = null,
        limit: Int? = null,
        page: Int? = null,
        search: String? = null,
    ): NetworkResponse<PaginatedData<FixedDepositDomain>, ErrorDomain>

    suspend fun getFDDetails(id: String): NetworkResponse<FDDetailsDomain, ErrorDomain>

    suspend fun purchaseFD(data: PurchaseFDBodyDto): NetworkResponse<String, ErrorDomain>
}
