package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo

class ExportSoaReportUseCase(
    private val repository: PortfolioRepo
) {
    suspend operator fun invoke(folio: String,): NetworkResponse<String, ErrorDomain> {
        return repository.exportReport(type = "soa", folio = folio)
    }
}