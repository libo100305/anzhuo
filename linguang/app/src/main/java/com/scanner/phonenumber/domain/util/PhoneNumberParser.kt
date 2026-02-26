package com.scanner.phonenumber.domain.util

class PhoneNumberParser {
    
    // 支持的电话号码格式正则表达式
    private val phonePatterns = listOf(
        // 手机号码格式
        Regex("""1[3-9]\d{9}"""),                           // 13812345678
        Regex("""1[3-9]\d-\d{4}-\d{4}"""),                 // 138-1234-5678
        Regex("""1[3-9]\d\s\d{4}\s\d{4}"""),               // 138 1234 5678
        Regex("""\(1[3-9]\d\)\d{4}-\d{4}"""),              // (138)1234-5678
        
        // 固定电话格式
        Regex("""0\d{2,3}\d{7,8}"""),                      // 01012345678
        Regex("""0\d{2,3}-\d{7,8}"""),                     // 010-12345678
        Regex("""0\d{2,3}\s\d{7,8}"""),                    // 010 12345678
        Regex("""\(0\d{2,3}\)\d{7,8}"""),                  // (010)12345678
        
        // 国际号码格式
        Regex("""\+861[3-9]\d{9}"""),                      // +8613812345678
        Regex("""\+86\s1[3-9]\d{9}"""),                    // +86 13812345678
        Regex("""\+86-1[3-9]\d{9}"""),                     // +86-13812345678
    )
    
    /**
     * 从文本中提取所有可能的电话号码
     */
    fun extractPhoneNumbers(text: String): List<String> {
        val results = mutableSetOf<String>()
        
        phonePatterns.forEach { pattern ->
            val matches = pattern.findAll(text)
            results.addAll(matches.map { it.value })
        }
        
        return results.toList()
    }
    
    /**
     * 标准化电话号码，去除分隔符便于匹配
     */
    fun normalizePhoneNumber(phone: String): String {
        return phone.replace(Regex("[\\s\\-\\(\\)\\+]"), "")
    }
    
    /**
     * 验证电话号码格式是否有效
     */
    fun isValidPhoneNumber(phone: String): Boolean {
        val normalized = normalizePhoneNumber(phone)
        
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
    
    /**
     * 格式化电话号码显示
     */
    fun formatPhoneNumber(phone: String): String {
        val normalized = normalizePhoneNumber(phone)
        
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
            
            else -> phone
        }
    }
}