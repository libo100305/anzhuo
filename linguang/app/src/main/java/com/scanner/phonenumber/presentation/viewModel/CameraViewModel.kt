package com.scanner.phonenumber.presentation.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanner.phonenumber.data.model.RecognitionHistory
import com.scanner.phonenumber.data.repository.HistoryRepository
import com.scanner.phonenumber.domain.usecase.MatchContactUseCase
import com.scanner.phonenumber.domain.usecase.RecognizePhoneNumberUseCase
import com.scanner.phonenumber.presentation.camera.CameraManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val recognizeUseCase: RecognizePhoneNumberUseCase,
    private val matchContactUseCase: MatchContactUseCase,
    private val historyRepository: HistoryRepository
) : ViewModel() {
    
    private val _cameraState = MutableStateFlow<CameraState>(CameraState.Idle)
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()
    
    private val _recognitionResult = MutableStateFlow<RecognitionResult?>(null)
    val recognitionResult: StateFlow<RecognitionResult?> = _recognitionResult.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    sealed class CameraState {
        object Idle : CameraState()
        object Initializing : CameraState()
        object Ready : CameraState()
        data class Error(val message: String) : CameraState()
    }
    
    data class RecognitionResult(
        val recognizedText: String,
        val phoneNumbers: List<String>,
        val formattedPhoneNumbers: List<String>,
        val matchedContacts: Map<String, String?>
    )
    
    fun updateCameraState(state: CameraState) {
        _cameraState.value = state
    }
    
    fun captureAndRecognize(cameraManager: CameraManager) {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                // 拍照
                val bitmapResult = cameraManager.capturePhoto()
                if (bitmapResult.isFailure) {
                    _cameraState.value = CameraState.Error("拍照失败: ${bitmapResult.exceptionOrNull()?.message}")
                    return@launch
                }
                
                val bitmap = bitmapResult.getOrNull() ?: return@launch
                
                // OCR识别
                val recognitionResult = recognizeUseCase(bitmap)
                if (recognitionResult.isFailure) {
                    _cameraState.value = CameraState.Error("识别失败: ${recognitionResult.exceptionOrNull()?.message}")
                    return@launch
                }
                
                val result = recognitionResult.getOrNull() ?: return@launch
                
                // 匹配联系人
                val matchedContacts = matchContactUseCase.matchMultiple(result.phoneNumbers)
                
                // 更新UI状态
                _recognitionResult.value = RecognitionResult(
                    recognizedText = result.recognizedText,
                    phoneNumbers = result.phoneNumbers,
                    formattedPhoneNumbers = result.formattedPhoneNumbers,
                    matchedContacts = matchedContacts
                )
                
                // 保存到历史记录
                saveToHistory(result, matchedContacts)
                
            } catch (e: Exception) {
                _cameraState.value = CameraState.Error("处理失败: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private suspend fun saveToHistory(
        result: RecognizePhoneNumberUseCase.RecognitionResult,
        matchedContacts: Map<String, String?>
    ) {
        result.phoneNumbers.forEach { phoneNumber ->
            val matchedName = matchedContacts[phoneNumber]
            val history = RecognitionHistory(
                recognizedText = result.recognizedText,
                phoneNumber = phoneNumber,
                matchedContactName = matchedName
            )
            historyRepository.insertHistory(history)
        }
    }
    
    fun clearRecognitionResult() {
        _recognitionResult.value = null
    }
    
    override fun onCleared() {
        super.onCleared()
    }
}