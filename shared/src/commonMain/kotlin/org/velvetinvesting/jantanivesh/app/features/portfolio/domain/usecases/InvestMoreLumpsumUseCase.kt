package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import org.velvetinvesting.jantanivesh.app.features.portfolio.ui.viewmodel.LumpSumAdd
import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.portfolio.data.mapper.toInvestMoreDto
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo

class InvestMoreLumpsumUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(items: List<LumpSumAdd>): NetworkResponse<String, ErrorDomain> {
        return repository.investMoreLumpsum(items.toInvestMoreDto())
    }
}
