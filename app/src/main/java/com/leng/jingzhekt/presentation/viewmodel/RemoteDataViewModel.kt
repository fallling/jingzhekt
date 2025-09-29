package com.leng.jingzhekt.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leng.jingzhekt.data.remote.config.NetworkResult
import com.leng.jingzhekt.data.remote.dto.*
import com.leng.jingzhekt.data.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * 远程数据ViewModel示例
 * 展示如何使用远程MySQL数据库服务
 */
@HiltViewModel
class RemoteDataViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    
    // ==================== 交易记录状态 ====================
    
    private val _transactions = MutableStateFlow<NetworkResult<List<TransactionDto>>>(NetworkResult.Loading)
    val transactions: StateFlow<NetworkResult<List<TransactionDto>>> = _transactions.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // ==================== 分类状态 ====================
    
    private val _categories = MutableStateFlow<NetworkResult<List<CategoryDto>>>(NetworkResult.Loading)
    val categories: StateFlow<NetworkResult<List<CategoryDto>>> = _categories.asStateFlow()
    
    // ==================== 账户状态 ====================
    
    private val _accounts = MutableStateFlow<NetworkResult<List<AccountDto>>>(NetworkResult.Loading)
    val accounts: StateFlow<NetworkResult<List<AccountDto>>> = _accounts.asStateFlow()
    
    // ==================== 统计状态 ====================
    
    private val _monthlyStatistics = MutableStateFlow<NetworkResult<MonthlyStatisticsDto>?>(null)
    val monthlyStatistics: StateFlow<NetworkResult<MonthlyStatisticsDto>?> = _monthlyStatistics.asStateFlow()
    
    // ==================== 交易记录操作 ====================
    
    /**
     * 加载交易记录
     */
    fun loadTransactions(
        ledgerId: Int? = null,
        startDate: String? = null,
        endDate: String? = null
    ) {
        viewModelScope.launch {
            remoteRepository.getTransactions(ledgerId, startDate, endDate)
                .collect { result ->
                    _transactions.value = result
                    _isLoading.value = result is NetworkResult.Loading
                    if (result is NetworkResult.Error) {
                        _errorMessage.value = result.exception.message
                    }
                }
        }
    }
    
    /**
     * 创建交易记录
     */
    fun createTransaction(
        ledgerId: Int,
        categoryId: Int,
        accountId: Int,
        amount: Double,
        notes: String? = null
    ) {
        viewModelScope.launch {
            val transaction = CreateTransactionDto(
                transactionTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                ledgerId = ledgerId,
                typeId = if (amount > 0) 2 else 1, // 2=收入, 1=支出
                categoryId = categoryId,
                amount = amount,
                accountId = accountId,
                notes = notes
            )
            
            remoteRepository.createTransaction(transaction)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            // 创建成功后重新加载交易记录
                            loadTransactions(ledgerId)
                        }
                        is NetworkResult.Error -> {
                            _errorMessage.value = result.exception.message
                        }
                        is NetworkResult.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }
        }
    }
    
    /**
     * 更新交易记录
     */
    fun updateTransaction(
        id: Int,
        amount: Double? = null,
        notes: String? = null,
        categoryId: Int? = null
    ) {
        viewModelScope.launch {
            val updateData = UpdateTransactionDto(
                amount = amount,
                notes = notes,
                categoryId = categoryId
            )
            
            remoteRepository.updateTransaction(id, updateData)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            // 更新成功后重新加载交易记录
                            loadTransactions()
                        }
                        is NetworkResult.Error -> {
                            _errorMessage.value = result.exception.message
                        }
                        is NetworkResult.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }
        }
    }
    
    /**
     * 删除交易记录
     */
    fun deleteTransaction(id: Int) {
        viewModelScope.launch {
            remoteRepository.deleteTransaction(id)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            // 删除成功后重新加载交易记录
                            loadTransactions()
                        }
                        is NetworkResult.Error -> {
                            _errorMessage.value = result.exception.message
                        }
                        is NetworkResult.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }
        }
    }
    
    // ==================== 分类操作 ====================
    
    /**
     * 加载分类
     */
    fun loadCategories(type: String? = null) {
        viewModelScope.launch {
            remoteRepository.getCategories(type)
                .collect { result ->
                    _categories.value = result
                    if (result is NetworkResult.Error) {
                        _errorMessage.value = result.exception.message
                    }
                }
        }
    }
    
    /**
     * 创建分类
     */
    fun createCategory(
        name: String,
        type: String,
        parentId: Int? = null,
        iconName: String? = null
    ) {
        viewModelScope.launch {
            val category = CreateCategoryDto(
                categoryName = name,
                categoryType = type,
                parentId = parentId,
                iconName = iconName
            )
            
            remoteRepository.createCategory(category)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            // 创建成功后重新加载分类
                            loadCategories(type)
                        }
                        is NetworkResult.Error -> {
                            _errorMessage.value = result.exception.message
                        }
                        is NetworkResult.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }
        }
    }
    
    // ==================== 账户操作 ====================
    
    /**
     * 加载账户
     */
    fun loadAccounts() {
        viewModelScope.launch {
            remoteRepository.getAccounts()
                .collect { result ->
                    _accounts.value = result
                    if (result is NetworkResult.Error) {
                        _errorMessage.value = result.exception.message
                    }
                }
        }
    }
    
    /**
     * 创建账户
     */
    fun createAccount(
        name: String,
        type: String,
        initialBalance: Double = 0.0
    ) {
        viewModelScope.launch {
            val account = CreateAccountDto(
                accountName = name,
                accountType = type,
                initialBalance = initialBalance
            )
            
            remoteRepository.createAccount(account)
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            // 创建成功后重新加载账户
                            loadAccounts()
                        }
                        is NetworkResult.Error -> {
                            _errorMessage.value = result.exception.message
                        }
                        is NetworkResult.Loading -> {
                            _isLoading.value = true
                        }
                    }
                }
        }
    }
    
    // ==================== 统计操作 ====================
    
    /**
     * 加载月度统计
     */
    fun loadMonthlyStatistics(ledgerId: Int, year: Int, month: Int) {
        viewModelScope.launch {
            remoteRepository.getMonthlyStatistics(ledgerId, year, month)
                .collect { result ->
                    _monthlyStatistics.value = result
                    if (result is NetworkResult.Error) {
                        _errorMessage.value = result.exception.message
                    }
                }
        }
    }
    
    // ==================== 工具方法 ====================
    
    /**
     * 清除错误消息
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
    /**
     * 初始化数据
     */
    fun initializeData() {
        loadCategories()
        loadAccounts()
        loadTransactions()
    }
}

