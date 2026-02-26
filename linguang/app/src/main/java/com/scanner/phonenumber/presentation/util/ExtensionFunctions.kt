package com.scanner.phonenumber.presentation.util

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun String.isValidPhoneNumber(): Boolean {
    val normalized = this.replace(Regex("[\\s\\-\\(\\)\\+]"), "")
    
    // 手机号码验证
    if (normalized.matches(Regex("1[3-9]\\d{9}"))) {
        return true
    }
    
    // 固定电话验证
    if (normalized.matches(Regex("0\\d{2,3}\\d{7,8}")) && 
        normalized.length in 10..12) {
        return true
    }
    
    // 国际号码验证
    if (normalized.startsWith("86") && normalized.length == 13) {
        val domesticNumber = normalized.substring(2)
        return domesticNumber.matches(Regex("1[3-9]\\d{9}"))
    }
    
    return false
}

fun String.formatPhoneNumber(): String {
    val normalized = this.replace(Regex("[\\s\\-\\(\\)\\+]"), "")
    
    return when {
        // 手机号码格式化
        normalized.matches(Regex("1[3-9]\\d{9}")) -> {
            "${normalized.substring(0, 3)}-${normalized.substring(3, 7)}-${normalized.substring(7)}"
        }
        
        // 固定电话格式化
        normalized.matches(Regex("0\\d{2,3}\\d{7,8}")) -> {
            if (normalized.length == 10) {
                // 010-12345678
                "0${normalized.substring(1, 3)}-${normalized.substring(3)}"
            } else {
                // 021-12345678
                "0${normalized.substring(1, 4)}-${normalized.substring(4)}"
            }
        }
        
        // 国际号码格式化
        normalized.startsWith("86") && normalized.length == 13 -> {
            val domesticNumber = normalized.substring(2)
            "+86 ${domesticNumber.substring(0, 3)}-${domesticNumber.substring(3, 7)}-${domesticNumber.substring(7)}"
        }
        
        else -> this
    }
}