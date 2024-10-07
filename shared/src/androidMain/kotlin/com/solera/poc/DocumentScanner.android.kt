package com.solera.poc

actual class DocumentScanner {
    actual fun startScanning(resultCallback: (List<String>) -> Unit) {

        println("Android missing this feature")
        resultCallback(listOf())
    }
}