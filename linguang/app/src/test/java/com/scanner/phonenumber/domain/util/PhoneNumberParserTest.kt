package com.scanner.phonenumber.domain.util

import org.junit.Assert.*
import org.junit.Test

class PhoneNumberParserTest {
    
    private val parser = PhoneNumberParser()
    
    @Test
    fun `extract phone numbers from text`() {
        val text = "联系电话：138-1234-5678 或 010-12345678"
        val result = parser.extractPhoneNumbers(text)
        
        assertTrue(result.contains("138-1234-5678"))
        assertTrue(result.contains("010-12345678"))
    }
    
    @Test
    fun `normalize phone number`() {
        val phone = "138-1234-5678"
        val normalized = parser.normalizePhoneNumber(phone)
        
        assertEquals("13812345678", normalized)
    }
    
    @Test
    fun `validate mobile phone number`() {
        assertTrue(parser.isValidPhoneNumber("13812345678"))
        assertTrue(parser.isValidPhoneNumber("138-1234-5678"))
        assertFalse(parser.isValidPhoneNumber("12812345678")) // 不是有效的手机号段
    }
    
    @Test
    fun `validate landline phone number`() {
        assertTrue(parser.isValidPhoneNumber("010-12345678"))
        assertTrue(parser.isValidPhoneNumber("021-12345678"))
        assertFalse(parser.isValidPhoneNumber("01-12345678")) // 区号太短
    }
    
    @Test
    fun `format phone number`() {
        assertEquals("138-1234-5678", parser.formatPhoneNumber("13812345678"))
        assertEquals("010-12345678", parser.formatPhoneNumber("01012345678"))
        assertEquals("+86 138-1234-5678", parser.formatPhoneNumber("+8613812345678"))
    }
}