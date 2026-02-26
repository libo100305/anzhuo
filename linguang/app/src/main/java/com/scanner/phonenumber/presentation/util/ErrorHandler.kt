package com.scanner.phonenumber.presentation.util

import android.util.Log
import com.scanner.phonenumber.BuildConfig

object ErrorHandler {
    
    fun logError(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
        // 在生产环境中可以发送到崩溃报告服务
    }
    
    fun logWarning(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, message)
        }
    }
    
    fun logInfo(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }
    
    sealed class AppError {
        data class CameraError(val message: String) : AppError()
        data class OCRError(val message: String) : AppError()
        data class ContactsError(val message: String) : AppError()
        data class DatabaseError(val message: String) : AppError()
        data class NetworkError(val message: String) : AppError()
        data class UnknownError(val message: String, val throwable: Throwable? = null) : AppError()
    }
    
    fun handleError(error: AppError): String {
        return when (error) {
            is AppError.CameraError -> "相机错误: ${error.message}"
            is AppError.OCRError -> "识别错误: ${error.message}"
            is AppError.ContactsError -> "通讯录错误: ${error.message}"
            is AppError.DatabaseError -> "数据库错误: ${error.message}"
            is AppError.NetworkError -> "网络错误: ${error.message}"
            is AppError.UnknownError -> "未知错误: ${error.message}"
        }
    }
}