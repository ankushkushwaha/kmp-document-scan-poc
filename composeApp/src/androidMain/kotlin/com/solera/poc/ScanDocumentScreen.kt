package com.solera.poc

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import java.lang.reflect.Modifier

@Composable
fun ScanDocumentScreen(onScanClick: () -> Unit, scannedImageUri: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onScanClick) {
            Text(text = "Start Scanning")
        }

//        Spacer(modifier = Modifier.height(16.dp))

        scannedImageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(
                    data = scannedImageUri,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = null,
            )
        }
    }
}