package com.solera.poc
import platform.VisionKit.*
import platform.Foundation.NSError
import platform.UIKit.*
import  platform.Foundation.*
import platform.darwin.NSObject
import platform.UIKit.UIImage
import platform.darwin.*

actual class DocumentScanner  {
    private val controller = DocumentScannerDelegate()

    actual fun startScanning(resultCallback: (List<String>) -> Unit) {
        controller.openDocumentScanner { scannedDocuments ->
            resultCallback(scannedDocuments)
        }
    }
}

class DocumentScannerDelegate : NSObject(), VNDocumentCameraViewControllerDelegateProtocol {
    private val viewController = VNDocumentCameraViewController()
    var resultCallback: ((List<String>) -> Unit)? = null

    override fun documentCameraViewController(
        controller: VNDocumentCameraViewController,
        didFinishWithScan: VNDocumentCameraScan
    ) {
        // Handle the scanned document

        val imagePaths = mutableListOf<String>()

        for (pageIndex in 0u until didFinishWithScan.pageCount.toUInt()) {

            val image = didFinishWithScan.imageOfPageAtIndex(0u)
            imagePaths.add(image.toString())
        }

        // Call the result callback with the scanned image paths
        resultCallback?.invoke(imagePaths)

        println("Document scan completed with ${didFinishWithScan.pageCount} pages")
        viewController.dismissModalViewControllerAnimated(true)
    }

    override fun documentCameraViewControllerDidCancel(controller: VNDocumentCameraViewController) {
        // Handle cancellation
        println("Document scan cancelled")
    }

    override fun documentCameraViewController(
        controller: VNDocumentCameraViewController,
        didFailWithError: NSError
    ) {
        // Handle error
        println("Document scan failed: ${didFailWithError.localizedDescription}")
    }

    fun openDocumentScanner(resultCallback: (List<String>) -> Unit) {
        viewController.delegate = this
        this.resultCallback = resultCallback

        // Get the root view controller and present the document scanner

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(viewController, animated = true, completion = null)
    }
}