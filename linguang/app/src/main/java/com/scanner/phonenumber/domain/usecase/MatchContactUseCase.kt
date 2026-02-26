package com.scanner.phonenumber.domain.usecase

import com.scanner.phonenumber.data.repository.ContactRepository
import com.scanner.phonenumber.domain.util.PhoneNumberParser
import javax.inject.Inject

class MatchContactUseCase @Inject constructor(
    private val contactRepository: ContactRepository,
    private val phoneNumberParser: PhoneNumberParser
) {
    
    suspend operator fun invoke(phoneNumber: String): String? {
        return try {
            val normalizedPhone = phoneNumberParser.normalizePhoneNumber(phoneNumber)
            val contact = contactRepository.getContactByPhoneNumber(normalizedPhone)
            contact?.name
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * 批量匹配多个电话号码
     */
    suspend fun matchMultiple(phoneNumbers: List<String>): Map<String, String?> {
        return phoneNumbers.associateWith { phoneNumber ->
            invoke(phoneNumber)
        }
    }
}