package com.leng.jingzhekt.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.DailyBill
import com.leng.jingzhekt.Entity.MonthlyBill
import com.leng.jingzhekt.domain.usecase.GetBillsByDateRangeUseCase
import com.leng.jingzhekt.domain.repository.ClassifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class BillViewModel @Inject constructor(
    private val getBillsByDateRangeUseCase: GetBillsByDateRangeUseCase,
    val classifyRepository: ClassifyRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(BillUiState())
    val uiState: StateFlow<BillUiState> = _uiState.asStateFlow()
    
    fun loadBillsForMonth(month: YearMonth) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val startDate = month.atDay(1).atStartOfDay()
                val endDate = month.atEndOfMonth().atTime(23, 59, 59)
                
                getBillsByDateRangeUseCase(startDate, endDate).collect { bills ->
                    val monthlyBill = createMonthlyBillFromBills(bills, month)
                    _uiState.value = _uiState.value.copy(
                        bills = bills,
                        monthlyBill = monthlyBill,
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
    }
    
    private fun createMonthlyBillFromBills(bills: List<Bill>, month: YearMonth): MonthlyBill? {
        if (bills.isEmpty()) return null
        
        // 按日期分组账单
        val billsByDate = bills.groupBy { it.time.toLocalDate() }
        
        // 创建日账单列表
        val dailyBills = billsByDate.map { (date, dayBills) ->
            DailyBill(dayBills)
        }.sortedBy { it.date }
        
        return MonthlyBill(dailyBills)
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}

data class BillUiState(
    val bills: List<Bill> = emptyList(),
    val monthlyBill: MonthlyBill? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)
