package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FixedDepositTransactionDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo

class GetFDPortfolioByIdUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(id: String): NetworkResponse<FixedDepositTransactionDomain, ErrorDomain> {
        return repository.getFDPortfolioById(id)
    }
}
