package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.models.ActiveSipDomain
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo

/** The running SIPs behind the portfolio's Active SIP tab: `GET /mf/purchase-plan`. */
class GetActiveSipsUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(): NetworkResponse<ActiveSipDomain, ErrorDomain> =
        repository.getActiveSips()
}
