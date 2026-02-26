package com.scanner.phonenumber.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanner.phonenumber.data.model.RecognitionHistory
import com.scanner.phonenumber.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {
    
    private val _historyList = MutableStateFlow<List<RecognitionHistory>>(emptyList())
    val historyList: StateFlow<List<RecognitionHistory>> = _historyList.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadHistory()
    }
    
    private fun loadHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                historyRepository.getAllHistory().collect { history ->
                    _historyList.value = history
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteHistory(id: String) {
        viewModelScope.launch {
            try {
                historyRepository.deleteHistory(id)
            } catch (e: Exception) {
                // 处理删除失败
            }
        }
    }
    
    fun clearAllHistory() {
        viewModelScope.launch {
            try {
                historyRepository.clearAllHistory()
            } catch (e: Exception) {
                // 处理清空失败
            }
        }
    }
}