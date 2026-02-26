package com.scanner.phonenumber.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.scanner.phonenumber.data.model.RecognitionHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    
    @Query("SELECT * FROM recognition_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<RecognitionHistory>>
    
    @Query("SELECT * FROM recognition_history WHERE phoneNumber = :phoneNumber ORDER BY timestamp DESC")
    fun getHistoryByPhoneNumber(phoneNumber: String): Flow<List<RecognitionHistory>>
    
    @Insert
    suspend fun insertHistory(history: RecognitionHistory)
    
    @Query("DELETE FROM recognition_history WHERE id = :id")
    suspend fun deleteHistory(id: String)
    
    @Query("DELETE FROM recognition_history")
    suspend fun clearAllHistory()
    
    @Query("SELECT COUNT(*) FROM recognition_history")
    suspend fun getHistoryCount(): Int
}