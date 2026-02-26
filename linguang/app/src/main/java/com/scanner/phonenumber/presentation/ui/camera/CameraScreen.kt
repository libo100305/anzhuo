package com.scanner.phonenumber.presentation.ui.camera

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.scanner.phonenumber.R
import com.scanner.phonenumber.presentation.camera.CameraManager
import com.scanner.phonenumber.presentation.viewModel.CameraViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    navController: NavHostController,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    
    val cameraState by viewModel.cameraState.collectAsState()
    val recognitionResult by viewModel.recognitionResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val cameraManager = remember { CameraManager(context) }
    
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    
    // 权限请求启动器
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            previewView?.let { pv ->
                cameraManager.initializeCamera(
                    pv,
                    lifecycleOwner
                ) { state ->
                    viewModel.updateCameraState(
                        when (state) {
                            is CameraManager.CameraState.Idle -> CameraViewModel.CameraState.Idle
                            is CameraManager.CameraState.Initializing -> CameraViewModel.CameraState.Initializing
                            is CameraManager.CameraState.Ready -> CameraViewModel.CameraState.Ready
                            is CameraManager.CameraState.Error -> CameraViewModel.CameraState.Error(state.message)
                        }
                    )
                }
            }
        } else {
            Toast.makeText(context, "需要相机权限才能使用此功能", Toast.LENGTH_SHORT).show()
        }
    }
    
    LaunchedEffect(Unit) {
        if (cameraPermissionState.status.isGranted) {
            previewView?.let { pv ->
                cameraManager.initializeCamera(
                    pv,
                    lifecycleOwner
                ) { state ->
                    viewModel.updateCameraState(
                        when (state) {
                            is CameraManager.CameraState.Idle -> CameraViewModel.CameraState.Idle
                            is CameraManager.CameraState.Initializing -> CameraViewModel.CameraState.Initializing
                            is CameraManager.CameraState.Ready -> CameraViewModel.CameraState.Ready
                            is CameraManager.CameraState.Error -> CameraViewModel.CameraState.Error(state.message)
                        }
                    )
                }
            }
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // 相机预览
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    previewView = this
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // 加载指示器
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.ocr_processing),
                        color = Color.White,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
        
        // 错误信息
        if (cameraState is CameraViewModel.CameraState.Error) {
            val error = cameraState as CameraViewModel.CameraState.Error
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error.message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
        
        // 识别结果覆盖层
        recognitionResult?.let { result ->
            RecognitionResultOverlay(
                result = result,
                onDismiss = { viewModel.clearRecognitionResult() }
            )
        }
        
        // 拍照按钮
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            FloatingActionButton(
                onClick = {
                    if (!isLoading) {
                        viewModel.captureAndRecognize(cameraManager)
                    }
                },
                enabled = cameraState is CameraViewModel.CameraState.Ready && !isLoading
            ) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = stringResource(R.string.scan_phone_number)
                )
            }
        }
    }
}

@Composable
fun RecognitionResultOverlay(
    result: CameraViewModel.RecognitionResult,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable { onDismiss() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "识别结果",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "关闭")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (result.phoneNumbers.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_phone_numbers_found),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    result.phoneNumbers.forEachIndexed { index, phoneNumber ->
                        val formattedNumber = result.formattedPhoneNumbers.getOrNull(index) ?: phoneNumber
                        val matchedContact = result.matchedContacts[phoneNumber]
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = formattedNumber,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                
                                matchedContact?.let {
                                    Text(
                                        text = "匹配联系人: $it",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}