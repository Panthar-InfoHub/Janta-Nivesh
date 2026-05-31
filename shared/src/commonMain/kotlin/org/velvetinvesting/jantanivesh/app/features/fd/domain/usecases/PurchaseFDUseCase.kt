package org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.fd.data.models.dto.PurchaseFDBodyDto
import org.velvetinvesting.jantanivesh.app.features.fd.domain.repository.FixedDepositRepository
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse

class PurchaseFDUseCase(
    private val repository: FixedDepositRepository
) {
    suspend operator fun invoke(data: PurchaseFDBodyDto): NetworkResponse<String, ErrorDomain> {
        return repository.purchaseFD(data)
    }
}
