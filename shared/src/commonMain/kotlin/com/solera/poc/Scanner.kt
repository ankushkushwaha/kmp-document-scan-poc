package com.solera.poc

interface ScannerInterface {
    fun startScanning()
}
expect fun getScanner(): ScannerInterface

class ScanManager {
    private val scanner = getScanner()
    fun startScanning() {
        scanner.startScanning()
    }
}