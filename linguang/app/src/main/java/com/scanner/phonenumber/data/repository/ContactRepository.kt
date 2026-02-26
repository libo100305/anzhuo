package com.scanner.phonenumber.data.repository

import com.scanner.phonenumber.data.database.ContactDao
import com.scanner.phonenumber.data.model.Contact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactRepository @Inject constructor(
    private val contactDao: ContactDao
) {
    
    fun getAllContacts(): Flow<List<Contact>> {
        return contactDao.getAllContacts()
    }
    
    suspend fun getContactByPhoneNumber(phoneNumber: String): Contact? {
        return contactDao.getContactByNormalizedNumber(
            phoneNumber.replace(Regex("[\\s\\-\\(\\)]"), "")
        )
    }
    
    suspend fun insertContacts(contacts: List<Contact>) {
        contactDao.insertContacts(contacts)
    }
    
    suspend fun insertContact(contact: Contact) {
        contactDao.insertContact(contact)
    }
    
    suspend fun clearAllContacts() {
        contactDao.clearAllContacts()
    }
    
    suspend fun getContactCount(): Int {
        return contactDao.getContactCount()
    }
}