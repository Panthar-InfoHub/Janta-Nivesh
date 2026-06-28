package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.FolioFundDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo

class GetFolioFundsUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(folioId: String): NetworkResponse<List<FolioFundDomain>, ErrorDomain> {
        return repository.getFolioFunds(folioId)
    }
}
