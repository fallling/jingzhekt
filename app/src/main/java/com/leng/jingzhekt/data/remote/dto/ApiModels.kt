package com.leng.jingzhekt.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * 通用API响应包装类
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("error_code")
    val errorCode: String? = null,
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * 分页响应
 */
data class PagedResponse<T>(
    @SerializedName("items")
    val items: List<T>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

// ==================== 交易记录相关DTO ====================

/**
 * 交易记录DTO
 */
data class TransactionDto(
    @SerializedName("transaction_id")
    val transactionId: Int,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("ledger_id")
    val ledgerId: Int,
    @SerializedName("type_id")
    val typeId: Int,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("account_id")
    val accountId: Int,
    @SerializedName("transfer_account_id")
    val transferAccountId: Int? = null,
    @SerializedName("reimbursement")
    val reimbursement: Double = 0.0,
    @SerializedName("is_reimbursed")
    val isReimbursed: Boolean = false,
    @SerializedName("notes")
    val notes: String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    // 关联数据
    @SerializedName("category")
    val category: CategoryDto? = null,
    @SerializedName("account")
    val account: AccountDto? = null,
    @SerializedName("transfer_account")
    val transferAccount: AccountDto? = null,
    @SerializedName("transaction_type")
    val transactionType: TransactionTypeDto? = null
)

/**
 * 创建交易记录DTO
 */
data class CreateTransactionDto(
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("ledger_id")
    val ledgerId: Int,
    @SerializedName("type_id")
    val typeId: Int,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("account_id")
    val accountId: Int,
    @SerializedName("transfer_account_id")
    val transferAccountId: Int? = null,
    @SerializedName("reimbursement")
    val reimbursement: Double = 0.0,
    @SerializedName("is_reimbursed")
    val isReimbursed: Boolean = false,
    @SerializedName("notes")
    val notes: String? = null
)

/**
 * 更新交易记录DTO
 */
data class UpdateTransactionDto(
    @SerializedName("transaction_time")
    val transactionTime: String? = null,
    @SerializedName("type_id")
    val typeId: Int? = null,
    @SerializedName("category_id")
    val categoryId: Int? = null,
    @SerializedName("amount")
    val amount: Double? = null,
    @SerializedName("account_id")
    val accountId: Int? = null,
    @SerializedName("transfer_account_id")
    val transferAccountId: Int? = null,
    @SerializedName("reimbursement")
    val reimbursement: Double? = null,
    @SerializedName("is_reimbursed")
    val isReimbursed: Boolean? = null,
    @SerializedName("notes")
    val notes: String? = null
)

/**
 * 交易类型DTO
 */
data class TransactionTypeDto(
    @SerializedName("type_id")
    val typeId: Int,
    @SerializedName("type_name")
    val typeName: String,
    @SerializedName("type_code")
    val typeCode: String,
    @SerializedName("description")
    val description: String? = null
)

// ==================== 分类相关DTO ====================

/**
 * 分类DTO
 */
data class CategoryDto(
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("parent_id")
    val parentId: Int? = null,
    @SerializedName("category_type")
    val categoryType: String,
    @SerializedName("icon_name")
    val iconName: String? = null,
    @SerializedName("sort_order")
    val sortOrder: Int = 0,
    @SerializedName("is_active")
    val isActive: Boolean = true,
    @SerializedName("created_at")
    val createdAt: String,
    // 关联数据
    @SerializedName("parent")
    val parent: CategoryDto? = null,
    @SerializedName("children")
    val children: List<CategoryDto>? = null
)

/**
 * 创建分类DTO
 */
data class CreateCategoryDto(
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("parent_id")
    val parentId: Int? = null,
    @SerializedName("category_type")
    val categoryType: String,
    @SerializedName("icon_name")
    val iconName: String? = null,
    @SerializedName("sort_order")
    val sortOrder: Int = 0
)

/**
 * 更新分类DTO
 */
data class UpdateCategoryDto(
    @SerializedName("category_name")
    val categoryName: String? = null,
    @SerializedName("parent_id")
    val parentId: Int? = null,
    @SerializedName("category_type")
    val categoryType: String? = null,
    @SerializedName("icon_name")
    val iconName: String? = null,
    @SerializedName("sort_order")
    val sortOrder: Int? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null
)

// ==================== 账户相关DTO ====================

/**
 * 账户DTO
 */
data class AccountDto(
    @SerializedName("account_id")
    val accountId: Int,
    @SerializedName("account_name")
    val accountName: String,
    @SerializedName("account_type")
    val accountType: String,
    @SerializedName("initial_balance")
    val initialBalance: Double,
    @SerializedName("current_balance")
    val currentBalance: Double,
    @SerializedName("currency")
    val currency: String = "CNY",
    @SerializedName("is_active")
    val isActive: Boolean = true,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

/**
 * 创建账户DTO
 */
data class CreateAccountDto(
    @SerializedName("account_name")
    val accountName: String,
    @SerializedName("account_type")
    val accountType: String,
    @SerializedName("initial_balance")
    val initialBalance: Double = 0.0,
    @SerializedName("currency")
    val currency: String = "CNY"
)

/**
 * 更新账户DTO
 */
data class UpdateAccountDto(
    @SerializedName("account_name")
    val accountName: String? = null,
    @SerializedName("account_type")
    val accountType: String? = null,
    @SerializedName("initial_balance")
    val initialBalance: Double? = null,
    @SerializedName("currency")
    val currency: String? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null
)

// ==================== 账本相关DTO ====================

/**
 * 账本DTO
 */
data class LedgerDto(
    @SerializedName("ledger_id")
    val ledgerId: Int,
    @SerializedName("ledger_name")
    val ledgerName: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("is_active")
    val isActive: Boolean = true,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

/**
 * 创建账本DTO
 */
data class CreateLedgerDto(
    @SerializedName("ledger_name")
    val ledgerName: String,
    @SerializedName("description")
    val description: String? = null
)

/**
 * 更新账本DTO
 */
data class UpdateLedgerDto(
    @SerializedName("ledger_name")
    val ledgerName: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null
)

// ==================== 统计相关DTO ====================

/**
 * 月度统计DTO
 */
data class MonthlyStatisticsDto(
    @SerializedName("year")
    val year: Int,
    @SerializedName("month")
    val month: Int,
    @SerializedName("total_income")
    val totalIncome: Double,
    @SerializedName("total_expense")
    val totalExpense: Double,
    @SerializedName("net_amount")
    val netAmount: Double,
    @SerializedName("transaction_count")
    val transactionCount: Int,
    @SerializedName("daily_statistics")
    val dailyStatistics: List<DailyStatisticsDto>
)

/**
 * 日统计DTO
 */
data class DailyStatisticsDto(
    @SerializedName("date")
    val date: String,
    @SerializedName("income")
    val income: Double,
    @SerializedName("expense")
    val expense: Double,
    @SerializedName("transaction_count")
    val transactionCount: Int
)

/**
 * 账户余额DTO
 */
data class AccountBalanceDto(
    @SerializedName("account_id")
    val accountId: Int,
    @SerializedName("account_name")
    val accountName: String,
    @SerializedName("account_type")
    val accountType: String,
    @SerializedName("initial_balance")
    val initialBalance: Double,
    @SerializedName("current_balance")
    val currentBalance: Double,
    @SerializedName("transaction_total")
    val transactionTotal: Double,
    @SerializedName("calculated_balance")
    val calculatedBalance: Double
)

/**
 * 分类统计DTO
 */
data class CategorySummaryDto(
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("category_type")
    val categoryType: String,
    @SerializedName("total_amount")
    val totalAmount: Double,
    @SerializedName("transaction_count")
    val transactionCount: Int,
    @SerializedName("percentage")
    val percentage: Double
)

