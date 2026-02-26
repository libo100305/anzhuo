package com.scanner.phonenumber.presentation.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionManager(private val context: Context) {
    
    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun hasContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun hasAllRequiredPermissions(): Boolean {
        return hasCameraPermission() && hasContactsPermission()
    }
    
    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 1001
        const val CONTACTS_PERMISSION_REQUEST_CODE = 1002
        const val ALL_PERMISSIONS_REQUEST_CODE = 1003
        
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS
        )
    }
}