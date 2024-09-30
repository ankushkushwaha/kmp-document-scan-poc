package com.solera.poc
import platform.VisionKit.*
import platform.Foundation.NSError
import platform.UIKit.*
import platform.darwin.NSObject

actual class DocumentScanner  {
    private val controller = DocumentScannerDelegate()

    actual fun startScanning(resultCallback: (List<String>) -> Unit) {
        controller.openDocumentScanner { scannedDocuments ->
            resultCallback(scannedDocuments)
        }
    }
}

class DocumentScannerDelegate : NSObject(), VNDocumentCameraViewControllerDelegateProtocol {
    private var viewController: VNDocumentCameraViewController? = null

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
        viewController?.dismissModalViewControllerAnimated(true)
    }

    override fun documentCameraViewControllerDidCancel(controller: VNDocumentCameraViewController) {
        println("Document scan cancelled")
        viewController?.dismissModalViewControllerAnimated(true)
    }

    override fun documentCameraViewController(
        controller: VNDocumentCameraViewController,
        didFailWithError: NSError
    ) {
        println("Document scan failed: ${didFailWithError.localizedDescription}")
        viewController?.dismissModalViewControllerAnimated(true)
    }

    fun openDocumentScanner(resultCallback: (List<String>) -> Unit) {

        viewController = VNDocumentCameraViewController()
        viewController?.delegate = this

        this.resultCallback = resultCallback

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(viewController!!, animated = true, completion = null)
    }
}