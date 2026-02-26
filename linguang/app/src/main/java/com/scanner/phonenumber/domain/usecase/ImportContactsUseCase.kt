package com.scanner.phonenumber.domain.usecase

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import com.scanner.phonenumber.data.model.Contact
import com.scanner.phonenumber.data.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImportContactsUseCase @Inject constructor(
    private val context: Context,
    private val contactRepository: ContactRepository
) {
    
    data class ImportResult(
        val success: Boolean,
        val importedCount: Int,
        val errorMessage: String? = null
    )
    
    suspend operator fun invoke(): ImportResult {
        return withContext(Dispatchers.IO) {
            // 检查权限
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@withContext ImportResult(
                    success = false,
                    importedCount = 0,
                    errorMessage = "缺少读取通讯录权限"
                )
            }
            
            try {
                val contacts = readContactsFromSystem()
                contactRepository.clearAllContacts()
                contactRepository.insertContacts(contacts)
                
                ImportResult(
                    success = true,
                    importedCount = contacts.size
                )
            } catch (e: Exception) {
                ImportResult(
                    success = false,
                    importedCount = 0,
                    errorMessage = "导入失败: ${e.message}"
                )
            }
        }
    }
    
    private fun readContactsFromSystem(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentResolver: ContentResolver = context.contentResolver
        
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        
        cursor?.use { 
            val contactIdIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            
            while (it.moveToNext()) {
                val contactId = it.getString(contactIdIndex)
                val name = it.getString(nameIndex) ?: "未知联系人"
                val number = it.getString(numberIndex)?.trim() ?: continue
                
                // 过滤掉无效号码
                if (number.isNotEmpty() && number.any { char -> char.isDigit() }) {
                    contacts.add(Contact.fromSystemContact(contactId, name, number))
                }
            }
        }
        
        return contacts.distinctBy { it.normalizedNumber }
    }
}