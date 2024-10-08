package com.solera.poc

actual class DocumentScanner {
    actual fun startScanning(resultCallback: (ScanResult) -> Unit) {
        println("Android missing this feature: startScanning")
        resultCallback(ScanResult())
    }
}

actual class FileSystem {
    actual fun saveImage(filename : String,bytes : ByteArray) : String {
        println("Android missing this feature: saveImage")
        return ""
    }
}
