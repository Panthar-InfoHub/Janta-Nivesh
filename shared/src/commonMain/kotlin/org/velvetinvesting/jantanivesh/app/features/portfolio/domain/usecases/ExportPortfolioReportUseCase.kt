package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.platform.PdfDownloadManager
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo

class ExportPortfolioReportUseCase(
    private val repository: PortfolioRepo,
    private val downloadManager: PdfDownloadManager
) {
    suspend operator fun invoke(
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ): NetworkResponse<Unit, ErrorDomain> {
        return when (val response = repository.getPortfolioReport()) {
            is NetworkResponse.Success -> {
                downloadManager.downloadPdf(
                    pdfBytes = response.data,
                    fileName = "Portfolio_Report",
                    onSuccess = {
                        onSuccess()
                    },
                    onFailed = { error ->
                        onFailed(error)
                    }
                )
                NetworkResponse.Success(Unit)
            }
            is NetworkResponse.Error -> {
                response
            }
        }
    }
}