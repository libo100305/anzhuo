package com.scanner.phonenumber.data.repository

import com.scanner.phonenumber.data.database.HistoryDao
import com.scanner.phonenumber.data.model.RecognitionHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val historyDao: HistoryDao
) {
    
    fun getAllHistory(): Flow<List<RecognitionHistory>> {
        return historyDao.getAllHistory()
    }
    
    fun getHistoryByPhoneNumber(phoneNumber: String): Flow<List<RecognitionHistory>> {
        return historyDao.getHistoryByPhoneNumber(phoneNumber)
    }
    
    suspend fun insertHistory(history: RecognitionHistory) {
        historyDao.insertHistory(history)
    }
    
    suspend fun deleteHistory(id: String) {
        historyDao.deleteHistory(id)
    }
    
    suspend fun clearAllHistory() {
        historyDao.clearAllHistory()
    }
    
    suspend fun getHistoryCount(): Int {
        return historyDao.getHistoryCount()
    }
}