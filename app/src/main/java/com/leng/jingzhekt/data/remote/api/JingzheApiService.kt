package com.leng.jingzhekt.data.remote.api

import com.leng.jingzhekt.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * 记账系统远程API接口
 * 用于访问远程MySQL数据库
 */
interface JingzheApiService {
    
    // ==================== 交易记录相关 ====================
    
    /**
     * 获取交易记录列表
     * @param ledgerId 账本ID
     * @param startDate 开始日期 (格式: yyyy-MM-dd)
     * @param endDate 结束日期 (格式: yyyy-MM-dd)
     * @param page 页码
     * @param pageSize 每页大小
     */
    @GET("transactions")
    suspend fun getTransactions(
        @Query("ledger_id") ledgerId: Int? = null,
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<ApiResponse<List<TransactionDto>>>
    
    /**
     * 根据ID获取交易记录
     */
    @GET("transactions/{id}")
    suspend fun getTransactionById(@Path("id") id: Int): Response<ApiResponse<TransactionDto>>
    
    /**
     * 创建交易记录
     */
    @POST("transactions")
    suspend fun createTransaction(@Body transaction: CreateTransactionDto): Response<ApiResponse<TransactionDto>>
    
    /**
     * 更新交易记录
     */
    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Int,
        @Body transaction: UpdateTransactionDto
    ): Response<ApiResponse<TransactionDto>>
    
    /**
     * 删除交易记录
     */
    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // ==================== 分类相关 ====================
    
    /**
     * 获取所有分类
     */
    @GET("categories")
    suspend fun getCategories(
        @Query("type") type: String? = null,
        @Query("parent_id") parentId: Int? = null
    ): Response<ApiResponse<List<CategoryDto>>>
    
    /**
     * 创建分类
     */
    @POST("categories")
    suspend fun createCategory(@Body category: CreateCategoryDto): Response<ApiResponse<CategoryDto>>
    
    /**
     * 更新分类
     */
    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Int,
        @Body category: UpdateCategoryDto
    ): Response<ApiResponse<CategoryDto>>
    
    /**
     * 删除分类
     */
    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // ==================== 账户相关 ====================
    
    /**
     * 获取所有账户
     */
    @GET("accounts")
    suspend fun getAccounts(): Response<ApiResponse<List<AccountDto>>>
    
    /**
     * 创建账户
     */
    @POST("accounts")
    suspend fun createAccount(@Body account: CreateAccountDto): Response<ApiResponse<AccountDto>>
    
    /**
     * 更新账户
     */
    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Path("id") id: Int,
        @Body account: UpdateAccountDto
    ): Response<ApiResponse<AccountDto>>
    
    /**
     * 删除账户
     */
    @DELETE("accounts/{id}")
    suspend fun deleteAccount(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // ==================== 账本相关 ====================
    
    /**
     * 获取所有账本
     */
    @GET("ledgers")
    suspend fun getLedgers(): Response<ApiResponse<List<LedgerDto>>>
    
    /**
     * 创建账本
     */
    @POST("ledgers")
    suspend fun createLedger(@Body ledger: CreateLedgerDto): Response<ApiResponse<LedgerDto>>
    
    /**
     * 更新账本
     */
    @PUT("ledgers/{id}")
    suspend fun updateLedger(
        @Path("id") id: Int,
        @Body ledger: UpdateLedgerDto
    ): Response<ApiResponse<LedgerDto>>
    
    /**
     * 删除账本
     */
    @DELETE("ledgers/{id}")
    suspend fun deleteLedger(@Path("id") id: Int): Response<ApiResponse<Unit>>
    
    // ==================== 统计相关 ====================
    
    /**
     * 获取月度统计
     */
    @GET("statistics/monthly")
    suspend fun getMonthlyStatistics(
        @Query("ledger_id") ledgerId: Int,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<ApiResponse<MonthlyStatisticsDto>>
    
    /**
     * 获取账户余额
     */
    @GET("statistics/account-balances")
    suspend fun getAccountBalances(
        @Query("ledger_id") ledgerId: Int? = null
    ): Response<ApiResponse<List<AccountBalanceDto>>>
    
    /**
     * 获取分类统计
     */
    @GET("statistics/category-summary")
    suspend fun getCategorySummary(
        @Query("ledger_id") ledgerId: Int,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("type") type: String? = null
    ): Response<ApiResponse<List<CategorySummaryDto>>>
}

