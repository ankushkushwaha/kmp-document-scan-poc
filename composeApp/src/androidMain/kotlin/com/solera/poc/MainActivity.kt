package com.solera.poc

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.lang.reflect.Modifier
import androidx.compose.foundation.layout.*
import java.net.URI


class MainActivity : ComponentActivity() {
    private lateinit var scannerLauncher: ActivityResultLauncher<IntentSenderRequest>
    private var scannedImageUri by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scannerLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                handleScanResult(result.data)
            } else {
                println("Scanning was canceled or failed.")
            }
        }

        setContent {
            ScanDocumentScreen(onScanClick = { startScanning() }, scannedImageUri)
        }
    }

    private fun startScanning() {

        val options = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(false)
            .setPageLimit(4)
            .setResultFormats(
                GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
                GmsDocumentScannerOptions.RESULT_FORMAT_PDF
            )
            .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
            .build()

        val scanner = GmsDocumentScanning.getClient(options)

        scanner.getStartScanIntent(this)
            .addOnSuccessListener { intentSender ->
                // Launch the scanning activity
                scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
            }
            .addOnFailureListener {
                println("Failed to get the scan intent: ${it.message}")
            }
    }

    private fun handleScanResult(data: Intent?) {
        val result = GmsDocumentScanningResult.fromActivityResultIntent(data)
        result?.getPages()?.let { pages ->
            if (pages.isNotEmpty()) {
                val imageUri = pages.first()?.getImageUri()
                scannedImageUri = imageUri.toString()
                println("Scanned Image URI: $imageUri")
            }
        }

        result?.getPdf()?.let { pdf ->
            val pdfUri = pdf.getUri()
            println("Scanned PDF URI: $pdfUri")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    ScanDocumentScreen(onScanClick = { /*TODO*/ }, scannedImageUri = "")
}