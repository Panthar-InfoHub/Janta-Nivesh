package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.PortfolioDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo

class GetPortfolioUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(): NetworkResponse<PortfolioDomain, ErrorDomain> {
        return repository.getPortfolio()
    }
}
