package com.solera.poc
import platform.VisionKit.*
import platform.Foundation.NSError
import platform.UIKit.*
import  platform.Foundation.*
import platform.darwin.NSObject

class DocumentScannerDelegate : NSObject(), VNDocumentCameraViewControllerDelegateProtocol {
    override fun documentCameraViewController(
        controller: VNDocumentCameraViewController,
        didFinishWithScan: VNDocumentCameraScan
    ) {
        // Handle the scanned document
        println("Document scan completed with ${didFinishWithScan.pageCount} pages")
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
}



class IOSScanner: ScannerInterface {
    private val delegate = DocumentScannerDelegate()
    private val viewController = VNDocumentCameraViewController()

    override fun startScanning() {
        print("startScanning iOS")
        openDocumentScanner()
    }

    fun openDocumentScanner() {
        viewController.delegate = delegate
        // Get the root view controller and present the document scanner
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(viewController, animated = true, completion = null)
    }

}

actual fun getScanner(): ScannerInterface = IOSScanner()
