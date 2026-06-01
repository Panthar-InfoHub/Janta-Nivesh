package org.velvetinvesting.jantanivesh.app.core.platform

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class PdfViewerAndroid(private val context: Context): PdfViewer{
    override fun openPdf(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(url.toUri(), "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

}