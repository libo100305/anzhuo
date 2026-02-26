package com.scanner.phonenumber.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.scanner.phonenumber.data.model.Contact
import com.scanner.phonenumber.data.model.RecognitionHistory

@Database(
    entities = [Contact::class, RecognitionHistory::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun contactDao(): ContactDao
    abstract fun historyDao(): HistoryDao
    
    companion object {
        const val DATABASE_NAME = "phone_scanner_database"
    }
}