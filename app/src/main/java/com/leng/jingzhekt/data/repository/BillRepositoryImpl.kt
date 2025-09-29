package com.leng.jingzhekt.data.repository

import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.data.local.dao.BillDao
import com.leng.jingzhekt.domain.repository.BillRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillRepositoryImpl @Inject constructor(
    private val billDao: BillDao
) : BillRepository {
    
    override fun getAllBills(): Flow<List<Bill>> = billDao.getAllBills()
    
    override fun getBillsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Bill>> =
        billDao.getBillsByDateRange(startDate, endDate)
    
    override suspend fun getBillById(id: Int): Bill? = billDao.getBillById(id)
    
    override suspend fun insertBill(bill: Bill) = billDao.insertBill(bill)
    
    override suspend fun deleteBill(bill: Bill) = billDao.deleteBill(bill)
    
    override suspend fun updateBill(bill: Bill) = billDao.updateBill(bill)
    
    override suspend fun deleteBillById(id: Int) = billDao.deleteBillById(id)
}
