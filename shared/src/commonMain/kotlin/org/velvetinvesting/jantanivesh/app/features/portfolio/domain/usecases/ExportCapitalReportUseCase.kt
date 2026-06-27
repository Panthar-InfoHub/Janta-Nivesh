package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo

class ExportCapitalReportUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(): NetworkResponse<String, ErrorDomain> {
        return repository.exportReport(type = "capital")
    }
}