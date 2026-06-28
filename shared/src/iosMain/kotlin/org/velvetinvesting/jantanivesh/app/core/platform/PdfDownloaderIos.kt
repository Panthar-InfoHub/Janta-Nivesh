package org.velvetinvesting.jantanivesh.app.core.platform

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.readRawBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIDocumentInteractionControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

class PdfDownloaderIos(
    private val client: HttpClient
) : PdfDownloadManager {

    @OptIn(ExperimentalForeignApi::class)
    override fun downloadPdf(
        pdfBytes: ByteArray,
        fileName: String,
        onSuccess: (String?) -> Unit,
        onFailed: (String) -> Unit
    ) {
        val safeFileName = fileName
            .replace("/", "-")
            .replace("\\", "-")
            .replace(":", "-")
        try {
            val data = pdfBytes.toNSData()

            val fileManager = NSFileManager.defaultManager

            val documentsURL = fileManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null
            ) ?: throw Exception("Cannot access documents directory")

            val fileURL = documentsURL.URLByAppendingPathComponent("$safeFileName.pdf")
            if (fileURL==null)
            {
                onFailed("Failed to save PDF")
                return
            }
            val success = data.writeToURL(fileURL, true)

            if (success) {
                onSuccess(documentsURL.path ?: "")
                openPdf(fileURL.path ?: "")

            } else {
                onFailed("Failed to save PDF")
            }

        } catch (e: Exception) {
            onFailed(e.message ?: "Unknown error")
        }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun downloadUrlPdf(
        url: String,
        fileName: String,
        onProgress: (progress: Int) -> Unit,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        try {

            val response = client.get(url) {
                headers {
                    append(HttpHeaders.Accept, "application/pdf")
                }
            }

            if (!response.status.isSuccess()) {
                onFailure()
                return
            }

            val bytes = response.readRawBytes()

            val documentsPath = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            ).firstOrNull() as? String

            if (documentsPath == null) {
                onFailure()
                return
            }

            val filePath = "$documentsPath/$fileName.pdf"
            val fileUrl = NSURL.fileURLWithPath(filePath)

            val data = bytes.usePinned { pinned ->
                NSData.create(
                    bytes = pinned.addressOf(0),
                    length = bytes.size.toULong()
                )
            }

            val writeSuccess = data.writeToURL(
                url = fileUrl,
                atomically = true
            ) ?: false

            if (!writeSuccess) {
                onFailure()
                return
            }

            withContext(Dispatchers.Main) {

                val rootViewController =
                    UIApplication.sharedApplication.keyWindow?.rootViewController

                if (rootViewController != null) {

                    val activityVC = UIActivityViewController(
                        activityItems = listOf(fileUrl),
                        applicationActivities = null
                    )

                    rootViewController.presentViewController(
                        viewControllerToPresent = activityVC,
                        animated = true,
                        completion = null
                    )
                }

                onSuccess()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            onFailure()
        }
    }


    private var documentController: UIDocumentInteractionController? = null

    fun openPdf(filePath: String) {

        val url = NSURL.fileURLWithPath(filePath)

        val controller = UIDocumentInteractionController.interactionControllerWithURL(url)

        val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController

            ?: return

        val delegate = object : NSObject(), UIDocumentInteractionControllerDelegateProtocol {

            override fun documentInteractionControllerViewControllerForPreview(

                controller: UIDocumentInteractionController

            ): UIViewController {

                return rootVC

            }

        }

        controller.delegate = delegate

        documentController = controller

        controller.presentPreviewAnimated(true)

    }
}


@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData {
    return this.usePinned { pinned ->
        NSData.dataWithBytes(
            bytes = pinned.addressOf(0),
            length = this.size.toULong()
        )
    }
}
