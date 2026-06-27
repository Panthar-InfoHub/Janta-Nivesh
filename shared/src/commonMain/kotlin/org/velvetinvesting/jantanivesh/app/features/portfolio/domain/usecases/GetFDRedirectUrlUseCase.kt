package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo

class GetFDRedirectUrlUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(id: String, event: String): NetworkResponse<String, ErrorDomain> {
        return repository.getFDRedirectUrl(id, event)
    }
}
