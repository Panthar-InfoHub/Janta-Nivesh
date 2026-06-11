package org.velvetinvesting.jantanivesh.app.features.fd.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.fd.domain.model.FDDetailsDomain
import org.velvetinvesting.jantanivesh.app.features.fd.domain.repository.FixedDepositRepository
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse

class GetFDDetailsUseCase(
    private val repository: FixedDepositRepository
) {
    suspend operator fun invoke(
        id: String
    ): NetworkResponse<FDDetailsDomain, ErrorDomain> {
        return repository.getFDDetails(id)
    }
}