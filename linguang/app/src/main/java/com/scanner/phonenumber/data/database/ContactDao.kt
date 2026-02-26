package com.scanner.phonenumber.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scanner.phonenumber.data.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    
    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Flow<List<Contact>>
    
    @Query("SELECT * FROM contacts WHERE normalizedNumber = :normalizedNumber LIMIT 1")
    suspend fun getContactByNormalizedNumber(normalizedNumber: String): Contact?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<Contact>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)
    
    @Query("DELETE FROM contacts")
    suspend fun clearAllContacts()
    
    @Query("SELECT COUNT(*) FROM contacts")
    suspend fun getContactCount(): Int
}