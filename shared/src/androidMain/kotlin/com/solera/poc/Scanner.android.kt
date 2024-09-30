package com.solera.poc

class AndroidScanner: ScannerInterface {
    override fun startScanning() {
        print("startScanning Android")
    }
}

actual fun getScanner(): ScannerInterface = AndroidScanner()

