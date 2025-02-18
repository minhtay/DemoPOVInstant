package com.example.instant.start.dialog

import android.Manifest
import android.util.Log
import android.view.Surface
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.base.ui.base.BaseBottomSheetDialogFragment
import com.example.base.util.isPermissionGranted
import com.example.instant.databinding.FragmentDialogScanQrBinding
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class ScanQRFragmentDialog :
    BaseBottomSheetDialogFragment<FragmentDialogScanQrBinding>(FragmentDialogScanQrBinding::inflate) {

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (requireActivity().isPermissionGranted(Manifest.permission.CAMERA)) {
                 initCameraView()
            } else {
                handleShowPermissionDenied()
            }
        }

    @OptIn(ExperimentalGetImage::class)
    private fun initCameraView() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            try {
                // Set up the preview use case to display camera preview.
                val preview: Preview = Preview.Builder()
                    .setTargetRotation(Surface.ROTATION_0)
                    .build()
                    .also {
                        it.surfaceProvider = binding.cameraPreview.surfaceProvider
                    }

                // Set up the capture use case to allow users to take photos.
                val imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .setFlashMode(ImageCapture.FLASH_MODE_OFF)
                    .setTargetRotation(Surface.ROTATION_0)
                    .build()

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setTargetRotation(Surface.ROTATION_0)
                    .build()

                imageAnalyzer.setAnalyzer(ContextCompat.getMainExecutor(requireActivity())) { imageProxy ->
                    imageProxy.image?.let {image ->
                        val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
                        BarcodeScanning.getClient().process(inputImage)
                            .addOnSuccessListener { code->
                                Log.d("TAGQR success", "QRcod: $code")

                                for (barcode in code) {
                                    val bounds = barcode.boundingBox
                                    val corners = barcode.cornerPoints

                                    val rawValue = barcode.rawValue

                                    val valueType = barcode.valueType
                                    // See API reference for complete list of supported types
                                    when (valueType) {
                                        Barcode.TYPE_WIFI -> {
                                            val ssid = barcode.wifi!!.ssid
                                            val password = barcode.wifi!!.password
                                            val type = barcode.wifi!!.encryptionType
                                            Log.d("TAGQR success", "QRcod: TYPE_WIFI  $ssid")

                                        }
                                        Barcode.TYPE_URL -> {
                                            val title = barcode.url!!.title
                                            val url = barcode.url!!.url
                                            Log.d("TAGQR success", "QRcod: TYPE_URL  $url")
                                        }
                                    }
                                }

                                imageProxy.close()

                            }.addOnFailureListener { error ->

                                Log.d("TAGQR error", "QRerror: $error")
                                imageProxy.close()


                            }
                    }
                }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner = this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture,
                    imageAnalyzer
                )


            } catch (exc: Exception) {
                Log.e("TAG", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this.requireContext()))
    }

    private fun handleShowPermissionDenied() {

    }

    private fun requestCameraPermission() {
        cameraLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun afterViewCreated() {
        requestCameraPermission()
    }
}
