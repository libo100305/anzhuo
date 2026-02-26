package com.scanner.phonenumber.presentation.ui.components

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.scanner.phonenumber.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequester(
    requiredPermissions: List<String> = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_CONTACTS
    ),
    onPermissionsGranted: () -> Unit,
    content: @Composable (Boolean) -> Unit
) {
    val context = LocalContext.current
    val permissionsState = rememberMultiplePermissionsState(requiredPermissions)
    var showRationale by remember { mutableStateOf(false) }
    
    LaunchedEffect(permissionsState) {
        val allGranted = permissionsState.allPermissionsGranted
        val shouldShowRationale = permissionsState.revokedPermissions.any {
            it.shouldShowRationale
        }
        
        when {
            allGranted -> {
                onPermissionsGranted()
            }
            shouldShowRationale -> {
                showRationale = true
            }
            else -> {
                permissionsState.launchMultiplePermissionRequest()
            }
        }
    }
    
    if (showRationale) {
        PermissionRationaleDialog(
            onConfirm = {
                showRationale = false
                permissionsState.launchMultiplePermissionRequest()
            },
            onDismiss = {
                showRationale = false
            },
            onOpenSettings = {
                showRationale = false
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
        )
    }
    
    content(permissionsState.allPermissionsGranted)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionRationaleDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = "需要权限",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "此应用需要相机和通讯录权限才能正常工作。\n\n" +
                            "• 相机权限：用于拍照识别电话号码\n" +
                            "• 通讯录权限：用于匹配联系人信息",
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("授予权限")
            }
        },
        dismissButton = {
            TextButton(onClick = onOpenSettings) {
                Text("去设置")
            }
        }
    )
}