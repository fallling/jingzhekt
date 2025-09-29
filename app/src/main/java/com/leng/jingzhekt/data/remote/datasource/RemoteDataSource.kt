package com.leng.jingzhekt.data.remote.datasource

import com.leng.jingzhekt.data.remote.api.JingzheApiService
import com.leng.jingzhekt.data.remote.config.NetworkResult
import com.leng.jingzhekt.data.remote.config.NetworkUtils
import com.leng.jingzhekt.data.remote.dto.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 远程数据源
 * 负责与远程MySQL数据库的通信
 */
@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: JingzheApiService
) {
    
    // ==================== 交易记录相关 ====================
    
    /**
     * 获取交易记录列表
     */
    suspend fun getTransactions(
        ledgerId: Int? = null,
        startDate: String? = null,
        endDate: String? = null,
        page: Int = 1,
        pageSize: Int = 20
    ): NetworkResult<List<TransactionDto>> {
        return NetworkUtils.handleApiResponse {
            apiService.getTransactions(ledgerId, startDate, endDate, page, pageSize)
        }
    }
    
    /**
     * 根据ID获取交易记录
     */
    suspend fun getTransactionById(id: Int): NetworkResult<TransactionDto> {
        return NetworkUtils.handleApiResponse {
            apiService.getTransactionById(id)
        }
    }
    
    /**
     * 创建交易记录
     */
    suspend fun createTransaction(transaction: CreateTransactionDto): NetworkResult<TransactionDto> {
        return NetworkUtils.handleApiResponse {
            apiService.createTransaction(transaction)
        }
    }
    
    /**
     * 更新交易记录
     */
    suspend fun updateTransaction(id: Int, transaction: UpdateTransactionDto): NetworkResult<TransactionDto> {
        return NetworkUtils.handleApiResponse {
            apiService.updateTransaction(id, transaction)
        }
    }
    
    /**
     * 删除交易记录
     */
    suspend fun deleteTransaction(id: Int): NetworkResult<Unit> {
        return NetworkUtils.handleApiResponse {
            apiService.deleteTransaction(id)
        }
    }
    
    // ==================== 分类相关 ====================
    
    /**
     * 获取所有分类
     */
    suspend fun getCategories(
        type: String? = null,
        parentId: Int? = null
    ): NetworkResult<List<CategoryDto>> {
        return NetworkUtils.handleApiResponse {
            apiService.getCategories(type, parentId)
        }
    }
    
    /**
     * 创建分类
     */
    suspend fun createCategory(category: CreateCategoryDto): NetworkResult<CategoryDto> {
        return NetworkUtils.handleApiResponse {
            apiService.createCategory(category)
        }
    }
    
    /**
     * 更新分类
     */
    suspend fun updateCategory(id: Int, category: UpdateCategoryDto): NetworkResult<CategoryDto> {
        return NetworkUtils.handleApiResponse {
            apiService.updateCategory(id, category)
        }
    }
    
    /**
     * 删除分类
     */
    suspend fun deleteCategory(id: Int): NetworkResult<Unit> {
        return NetworkUtils.handleApiResponse {
            apiService.deleteCategory(id)
        }
    }
    
    // ==================== 账户相关 ====================
    
    /**
     * 获取所有账户
     */
    suspend fun getAccounts(): NetworkResult<List<AccountDto>> {
        return NetworkUtils.handleApiResponse {
            apiService.getAccounts()
        }
    }
    
    /**
     * 创建账户
     */
    suspend fun createAccount(account: CreateAccountDto): NetworkResult<AccountDto> {
        return NetworkUtils.handleApiResponse {
            apiService.createAccount(account)
        }
    }
    
    /**
     * 更新账户
     */
    suspend fun updateAccount(id: Int, account: UpdateAccountDto): NetworkResult<AccountDto> {
        return NetworkUtils.handleApiResponse {
            apiService.updateAccount(id, account)
        }
    }
    
    /**
     * 删除账户
     */
    suspend fun deleteAccount(id: Int): NetworkResult<Unit> {
        return NetworkUtils.handleApiResponse {
            apiService.deleteAccount(id)
        }
    }
    
    // ==================== 账本相关 ====================
    
    /**
     * 获取所有账本
     */
    suspend fun getLedgers(): NetworkResult<List<LedgerDto>> {
        return NetworkUtils.handleApiResponse {
            apiService.getLedgers()
        }
    }
    
    /**
     * 创建账本
     */
    suspend fun createLedger(ledger: CreateLedgerDto): NetworkResult<LedgerDto> {
        return NetworkUtils.handleApiResponse {
            apiService.createLedger(ledger)
        }
    }
    
    /**
     * 更新账本
     */
    suspend fun updateLedger(id: Int, ledger: UpdateLedgerDto): NetworkResult<LedgerDto> {
        return NetworkUtils.handleApiResponse {
            apiService.updateLedger(id, ledger)
        }
    }
    
    /**
     * 删除账本
     */
    suspend fun deleteLedger(id: Int): NetworkResult<Unit> {
        return NetworkUtils.handleApiResponse {
            apiService.deleteLedger(id)
        }
    }
    
    // ==================== 统计相关 ====================
    
    /**
     * 获取月度统计
     */
    suspend fun getMonthlyStatistics(
        ledgerId: Int,
        year: Int,
        month: Int
    ): NetworkResult<MonthlyStatisticsDto> {
        return NetworkUtils.handleApiResponse {
            apiService.getMonthlyStatistics(ledgerId, year, month)
        }
    }
    
    /**
     * 获取账户余额
     */
    suspend fun getAccountBalances(ledgerId: Int? = null): NetworkResult<List<AccountBalanceDto>> {
        return NetworkUtils.handleApiResponse {
            apiService.getAccountBalances(ledgerId)
        }
    }
    
    /**
     * 获取分类统计
     */
    suspend fun getCategorySummary(
        ledgerId: Int,
        startDate: String,
        endDate: String,
        type: String? = null
    ): NetworkResult<List<CategorySummaryDto>> {
        return NetworkUtils.handleApiResponse {
            apiService.getCategorySummary(ledgerId, startDate, endDate, type)
        }
    }
}

