package com.solera.poc

expect class DocumentScanner {
    fun startScanning(resultCallback: (List<String>) -> Unit)
}