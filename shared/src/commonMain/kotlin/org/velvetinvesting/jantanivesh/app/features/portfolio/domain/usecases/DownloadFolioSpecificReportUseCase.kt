package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse
import org.velvetinvesting.jantanivesh.app.core.platform.PdfDownloadManager
import org.velvetinvesting.jantanivesh.app.features.portfolio.domain.repository.PortfolioRepo

class DownloadFolioSpecificReportUseCase(
    private val repository: PortfolioRepo,
    private val downloadManager: PdfDownloadManager
) {
    suspend operator fun invoke(
        folio: String,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ): NetworkResponse<Unit, ErrorDomain> {
        return when (val response = repository.getFolioSpecificReport(folio)) {
            is NetworkResponse.Success -> {
                downloadManager.downloadPdf(
                    pdfBytes = response.data,
                    fileName = "Fund_Holding_Report_$folio.pdf",
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