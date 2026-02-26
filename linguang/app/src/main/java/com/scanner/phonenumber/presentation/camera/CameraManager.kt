package com.scanner.phonenumber.presentation.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.util.Size
import android.view.Surface
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.tasks.await
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CameraManager(private val context: Context) {
    
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var imageCapture: ImageCapture? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    
    sealed class CameraState {
        object Idle : CameraState()
        object Initializing : CameraState()
        data class Ready(val preview: Preview) : CameraState()
        data class Error(val message: String) : CameraState()
    }
    
    fun initializeCamera(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        onCameraStateChange: (CameraState) -> Unit
    ) {
        onCameraStateChange(CameraState.Initializing)
        
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        
        cameraProviderFuture?.addListener({
            try {
                val cameraProvider = cameraProviderFuture?.get() ?: return@addListener
                
                // 选择后置摄像头
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                
                imageCapture = ImageCapture.Builder()
                    .setTargetResolution(Size(1920, 1080))
                    .build()
                
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                    
                    onCameraStateChange(CameraState.Ready(preview))
                } catch (exc: Exception) {
                    Log.e("CameraManager", "相机绑定失败", exc)
                    onCameraStateChange(CameraState.Error("相机初始化失败: ${exc.message}"))
                }
            } catch (exc: Exception) {
                Log.e("CameraManager", "获取相机提供者失败", exc)
                onCameraStateChange(CameraState.Error("相机初始化失败: ${exc.message}"))
            }
        }, ContextCompat.getMainExecutor(context))
    }
    
    suspend fun capturePhoto(): Result<Bitmap> = suspendCoroutine { continuation ->
        val imageCapture = imageCapture ?: run {
            continuation.resume(Result.failure(Exception("相机未初始化")))
            return@suspendCoroutine
        }
        
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            android.content.ContentValues()
        ).build()
        
        imageCapture.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    try {
                        // 将图片转换为Bitmap
                        val bitmap = output.savedUri?.let { uri ->
                            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                                Bitmap.createScaledBitmap(
                                    android.graphics.BitmapFactory.decodeStream(inputStream),
                                    1920,
                                    1080,
                                    true
                                )
                            }
                        }
                        
                        if (bitmap != null) {
                            continuation.resume(Result.success(bitmap))
                        } else {
                            continuation.resume(Result.failure(Exception("图片转换失败")))
                        }
                    } catch (e: Exception) {
                        continuation.resume(Result.failure(e))
                    }
                }
                
                override fun onError(exc: ImageCaptureException) {
                    continuation.resume(Result.failure(exc))
                }
            }
        )
    }
    
    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun shutdown() {
        cameraProviderFuture?.get()?.unbindAll()
        cameraExecutor.shutdown()
    }
}