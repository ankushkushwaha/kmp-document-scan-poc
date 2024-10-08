package com.solera.poc

data class ScanResult(
    val byteArrayList: List<ByteArray>? = null, // Nullable list of ByteArray
    val pathList: List<String>? = null // Non-nullable list of String
)

expect class DocumentScanner {
    fun startScanning(resultCallback: (ScanResult) -> Unit)
}

expect class FileSystem {
    fun saveImage(filename : String,bytes : ByteArray) : String
}
