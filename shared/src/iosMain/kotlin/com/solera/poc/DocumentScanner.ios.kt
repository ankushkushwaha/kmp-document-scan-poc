package com.solera.poc
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.VisionKit.*
import platform.Foundation.NSError
import platform.Foundation.getBytes
import platform.UIKit.*
import platform.darwin.NSObject
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.writeToFile
import platform.posix.memcpy

actual class DocumentScanner  {
    private val controller = DocumentScannerDelegate()

    actual fun startScanning(resultCallback: (ScanResult) -> Unit) {
        controller.openDocumentScanner { scannedDocuments ->
            resultCallback(scannedDocuments)
        }
    }
}

class DocumentScannerDelegate : NSObject(), VNDocumentCameraViewControllerDelegateProtocol {
    private var viewController: VNDocumentCameraViewController? = null

    var resultCallback: ((ScanResult) -> Unit)? = null

    override fun documentCameraViewController(
        controller: VNDocumentCameraViewController,
        didFinishWithScan: VNDocumentCameraScan
    ) {
        // Handle the scanned document
        val imagePaths = mutableListOf<String>()
        val imageByteArrays = mutableListOf<ByteArray>()

        for (pageIndex in 0u until didFinishWithScan.pageCount.toUInt()) {

            val image = didFinishWithScan.imageOfPageAtIndex(0u)

            // Convert UIImage to NSData
            val imageData = image.PNGRepresentation() ?: continue

            // Convert NSData to ByteArray
            val byteArray = imageData.toByteArray()

            imageByteArrays.add(byteArray)

            val path = FileSystem().saveImage(filename = "imageScanned", bytes = byteArray)
            imagePaths.add(path)
        }
        val scanResult = ScanResult(byteArrayList = imageByteArrays,
            pathList = imagePaths)
        resultCallback?.invoke(scanResult)

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

    fun openDocumentScanner(resultCallback: (ScanResult) -> Unit) {

        viewController = VNDocumentCameraViewController()
        viewController?.delegate = this

        this.resultCallback = resultCallback

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(viewController!!, animated = true, completion = null)
    }

    private fun UIImage.PNGRepresentation(): NSData? {
        return UIImagePNGRepresentation(this)
    }

     @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
     public fun ByteArray.toData(): NSData = memScoped {
        NSData.create(bytes = allocArrayOf(this@toData),
            length = this@toData.size.toULong())
    }

    @OptIn(ExperimentalForeignApi::class)
    fun NSData.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
        }
    }
}

actual class FileSystem {
    private val fileManager = NSFileManager.defaultManager
    private val documentDirectory = NSSearchPathForDirectoriesInDomains(
        directory = NSDocumentDirectory,
        domainMask = NSUserDomainMask,
        expandTilde = true
    ).first() as NSString

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun saveImage(filename : String, bytes : ByteArray) : String {
        val path = documentDirectory.stringByAppendingPathComponent(filename)
        val data = bytes.usePinned {
            NSData.create(
                bytes = it.addressOf(0),
                length = bytes.size.toULong()
            )
        }
        data.writeToFile(path = path, atomically = true)

        return path
    }
}

/*
//Handler {
//
//    func scanAndUpload() {
//       startScan {
//
//        print(image)
//        // file path return
//    }
//    }
//

actual upload() {

}



 */
//
//}


