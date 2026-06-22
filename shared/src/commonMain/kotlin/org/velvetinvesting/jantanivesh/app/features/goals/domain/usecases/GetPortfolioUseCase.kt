package org.velvetinvesting.jantanivesh.app.features.goals.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.goals.domain.repository.UserFinance
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.goals.domain.models.PortfolioDomain


class GetPortfolioUseCase(
    private val repository: UserFinance
) {
    suspend operator fun invoke(): NetworkResponse<PortfolioDomain, ErrorDomain> {
        return repository.getPortfolio()
    }
}
