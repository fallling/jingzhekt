package com.leng.jingzhekt.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.Type
import com.leng.jingzhekt.domain.usecase.ClassifyGetByTypeUseCase
import com.leng.jingzhekt.domain.usecase.GetMinorClassifyUseCase
import com.leng.jingzhekt.domain.usecase.getMajorClassifyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassifyViewModel @Inject constructor(
    private val getClassifyUseCase: ClassifyGetByTypeUseCase,
    private val  getMajorClassify: getMajorClassifyUseCase,
    private val getMinorClassifyUseCase: GetMinorClassifyUseCase
): ViewModel(){
    private val _uiState = MutableStateFlow(ClassifyUiState())
    val uiState: StateFlow<ClassifyUiState> = _uiState.asStateFlow()

    // 提供给 Preview 或测试使用
    fun setUiState(state: ClassifyUiState) {
        _uiState.value = state
    }

    fun loadAllClassifyList(type: Type){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMajor = true)

            try {
                getMajorClassify(type).collect { classifies ->
                    _uiState.value = _uiState.value.copy(
                        classifies = classifies,
                        isLoadingMajor = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingMajor = false,
                    message = "加载失败: ${e.message}"
                )
            }
        }
    }

    fun loadMinorClassifyList(parentId: Int){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMinor = true)

            try {
                getMinorClassifyUseCase(parentId).collect { classifies ->
                    _uiState.value = _uiState.value.copy(
                        minorClassifies = classifies,
                        isLoadingMinor = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingMinor = false,
                    message = "加载失败: ${e.message}"
                )
            }
        }
    }
}

data class ClassifyUiState(
    val classifies: List<Classify> = emptyList(),
    val minorClassifies: List<Classify>? = emptyList(),
    val isLoadingMajor: Boolean = false,
    val isLoadingMinor: Boolean = false,
    val message: String? = null
)