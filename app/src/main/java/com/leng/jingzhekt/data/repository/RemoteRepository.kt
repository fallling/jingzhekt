package com.leng.jingzhekt.data.repository

import com.leng.jingzhekt.data.remote.config.NetworkResult
import com.leng.jingzhekt.data.remote.datasource.RemoteDataSource
import com.leng.jingzhekt.data.remote.dto.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 远程Repository实现
 * 负责管理远程数据源
 */
@Singleton
class RemoteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    
    // ==================== 交易记录相关 ====================
    
    /**
     * 获取交易记录列表
     */
    fun getTransactions(
        ledgerId: Int? = null,
        startDate: String? = null,
        endDate: String? = null,
        page: Int = 1,
        pageSize: Int = 20
    ): Flow<NetworkResult<List<TransactionDto>>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.getTransactions(ledgerId, startDate, endDate, page, pageSize))
    }
    
    /**
     * 根据ID获取交易记录
     */
    fun getTransactionById(id: Int): Flow<NetworkResult<TransactionDto>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.getTransactionById(id))
    }
    
    /**
     * 创建交易记录
     */
    fun createTransaction(transaction: CreateTransactionDto): Flow<NetworkResult<TransactionDto>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.createTransaction(transaction))
    }
    
    /**
     * 更新交易记录
     */
    fun updateTransaction(id: Int, transaction: UpdateTransactionDto): Flow<NetworkResult<TransactionDto>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.updateTransaction(id, transaction))
    }
    
    /**
     * 删除交易记录
     */
    fun deleteTransaction(id: Int): Flow<NetworkResult<Unit>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.deleteTransaction(id))
    }
    
    // ==================== 分类相关 ====================
    
    /**
     * 获取所有分类
     */
    fun getCategories(
        type: String? = null,
        parentId: Int? = null
    ): Flow<NetworkResult<List<CategoryDto>>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.getCategories(type, parentId))
    }
    
    /**
     * 创建分类
     */
    fun createCategory(category: CreateCategoryDto): Flow<NetworkResult<CategoryDto>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.createCategory(category))
    }
    
    /**
     * 更新分类
     */
    fun updateCategory(id: Int, category: UpdateCategoryDto): Flow<NetworkResult<CategoryDto>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.updateCategory(id, category))
    }
    
    /**
     * 删除分类
     */
    fun deleteCategory(id: Int): Flow<NetworkResult<Unit>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.deleteCategory(id))
    }
    
    // ==================== 账户相关 ====================
    
    /**
     * 获取所有账户
     */
    fun getAccounts(): Flow<NetworkResult<List<AccountDto>>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.getAccounts())
    }
    
    /**
     * 创建账户
     */
    fun createAccount(account: CreateAccountDto): Flow<NetworkResult<AccountDto>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.createAccount(account))
    }
    
    /**
     * 更新账户
     */
    fun updateAccount(id: Int, account: UpdateAccountDto): Flow<NetworkResult<AccountDto>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.updateAccount(id, account))
    }
    
    /**
     * 删除账户
     */
    fun deleteAccount(id: Int): Flow<NetworkResult<Unit>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.deleteAccount(id))
    }
    
    // ==================== 账本相关 ====================
    
    /**
     * 获取所有账本
     */
    fun getLedgers(): Flow<NetworkResult<List<LedgerDto>>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.getLedgers())
    }
    
    /**
     * 创建账本
     */
    fun createLedger(ledger: CreateLedgerDto): Flow<NetworkResult<LedgerDto>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.createLedger(ledger))
    }
    
    /**
     * 更新账本
     */
    fun updateLedger(id: Int, ledger: UpdateLedgerDto): Flow<NetworkResult<LedgerDto>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.updateLedger(id, ledger))
    }
    
    /**
     * 删除账本
     */
    fun deleteLedger(id: Int): Flow<NetworkResult<Unit>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.deleteLedger(id))
    }
    
    // ==================== 统计相关 ====================
    
    /**
     * 获取月度统计
     */
    fun getMonthlyStatistics(
        ledgerId: Int,
        year: Int,
        month: Int
    ): Flow<NetworkResult<MonthlyStatisticsDto>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.getMonthlyStatistics(ledgerId, year, month))
    }
    
    /**
     * 获取账户余额
     */
    fun getAccountBalances(ledgerId: Int? = null): Flow<NetworkResult<List<AccountBalanceDto>>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.getAccountBalances(ledgerId))
    }
    
    /**
     * 获取分类统计
     */
    fun getCategorySummary(
        ledgerId: Int,
        startDate: String,
        endDate: String,
        type: String? = null
    ): Flow<NetworkResult<List<CategorySummaryDto>>> = flow {
        emit(NetworkResult.Loading)
        emit(remoteDataSource.getCategorySummary(ledgerId, startDate, endDate, type))
    }
}

