package com.leng.jingzhekt.domain.repository

import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.Classify
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface BillRepository {
    fun getAllBills(): Flow<List<Bill>>
    fun getBillsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Bill>>
    suspend fun getBillById(id: Int): Bill?
    suspend fun insertBill(bill: Bill)
    suspend fun deleteBill(bill: Bill)
    suspend fun updateBill(bill: Bill)
    suspend fun deleteBillById(id: Int)
}

interface ClassifyRepository {
    fun getAllClassifies(): Flow<List<Classify>>
    suspend fun getClassifyById(id: Int): Classify?
    suspend fun insertClassify(classify: Classify)
    suspend fun deleteClassify(classify: Classify)
    suspend fun updateClassify(classify: Classify)
}
