package com.leng.jingzhekt.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.domain.usecase.GetBillsUseCase
import com.leng.jingzhekt.domain.usecase.InsertBillUseCase
import com.leng.jingzhekt.domain.repository.ClassifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBillsUseCase: GetBillsUseCase,
    private val insertBillUseCase: InsertBillUseCase,
    val classifyRepository: ClassifyRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadBills()
    }
    
    private fun loadBills() {
        viewModelScope.launch {
            getBillsUseCase().collect { bills ->
                _uiState.value = _uiState.value.copy(
                    bills = bills,
                    isLoading = false
                )
            }
        }
    }
    
    fun addBill(bill: Bill) {
        viewModelScope.launch {
            try {
                insertBillUseCase(bill)
                _uiState.value = _uiState.value.copy(
                    message = "账单添加成功"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = "添加失败: ${e.message}"
                )
            }
        }
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}

data class HomeUiState(
    val bills: List<Bill> = emptyList(),
    val isLoading: Boolean = true,
    val message: String? = null
)
