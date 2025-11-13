package com.leng.jingzhekt.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.MonthlyBill
import com.leng.jingzhekt.domain.repository.ClassifyRepository
import com.leng.jingzhekt.domain.usecase.ClassifyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class ClassifyViewModel @Inject constructor(
    private val getClassifyUseCase: ClassifyUseCase,
): ViewModel(){
    private val _uiState = MutableStateFlow(ClassifyUiState())
    val uiState: StateFlow<ClassifyUiState> = _uiState.asStateFlow()

    // 提供给 Preview 或测试使用
    fun setUiState(state: ClassifyUiState) {
        _uiState.value = state
    }
    fun loadAllClassifyList(){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                getClassifyUseCase().collect { classifies ->
                    _uiState.value = _uiState.value.copy(
                        classifies = classifies,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "加载失败: ${e.message}"
                )
            }
        }

        Log.d("lengzq", "123123123123" + _uiState.value.classifies)
    }
}

data class ClassifyUiState(
    val classifies: List<Classify> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)