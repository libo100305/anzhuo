package com.scanner.phonenumber.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scanner.phonenumber.domain.usecase.ImportContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val importContactsUseCase: ImportContactsUseCase
) : ViewModel() {
    
    private val _importState = MutableStateFlow<ImportState>(ImportState.Idle)
    val importState: StateFlow<ImportState> = _importState.asStateFlow()
    
    sealed class ImportState {
        object Idle : ImportState()
        object Importing : ImportState()
        data class Success(val count: Int) : ImportState()
        data class Error(val message: String) : ImportState()
    }
    
    fun importContacts() {
        viewModelScope.launch {
            _importState.value = ImportState.Importing
            
            try {
                val result = importContactsUseCase()
                
                if (result.success) {
                    _importState.value = ImportState.Success(result.importedCount)
                } else {
                    _importState.value = ImportState.Error(result.errorMessage ?: "导入失败")
                }
            } catch (e: Exception) {
                _importState.value = ImportState.Error("导入异常: ${e.message}")
            }
        }
    }
    
    fun resetImportState() {
        _importState.value = ImportState.Idle
    }
}