package org.velvetinvesting.jantanivesh.app.core.platform

import platform.Foundation.NSURL
import platform.UIKit.UIDocumentInteractionController

class PdfViewerIos: PdfViewer {
    override fun openPdf(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        val controller = UIDocumentInteractionController.interactionControllerWithURL(nsUrl!!)
        controller.presentPreviewAnimated(true)
    }
}