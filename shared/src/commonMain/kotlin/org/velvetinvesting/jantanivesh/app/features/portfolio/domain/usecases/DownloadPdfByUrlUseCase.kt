package org.velvetinvesting.jantanivesh.app.features.portfolio.domain.usecases

import org.velvetinvesting.jantanivesh.app.core.platform.PdfDownloadManager


class DownloadPdfByUrlUseCase(
    private val pdfDownloadManager: PdfDownloadManager
) {
    suspend operator fun invoke(
        url: String,
        fileName: String,
        onProgress: (Int) -> Unit = {},
        onSuccess: () -> Unit = {},
        onFailure: () -> Unit = {}
    ) {
        pdfDownloadManager.downloadUrlPdf(
            url = url,
            fileName = fileName,
            onProgress = onProgress,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}
