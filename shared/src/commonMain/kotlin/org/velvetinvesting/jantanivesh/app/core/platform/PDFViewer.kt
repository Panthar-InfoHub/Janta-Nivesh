package org.velvetinvesting.jantanivesh.app.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface PdfViewer {
    fun openPdf(url: String)
}