package com.leng.jingzhekt.data.local.dao

import androidx.room.*
import com.leng.jingzhekt.Entity.Bill
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface BillDao {
    @Query("SELECT * FROM bill ORDER BY time DESC")
    fun getAllBills(): Flow<List<Bill>>
    
    @Query("SELECT * FROM bill WHERE time BETWEEN :startDate AND :endDate ORDER BY time DESC")
    fun getBillsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<Bill>>
    
    @Query("SELECT * FROM bill WHERE id = :id")
    suspend fun getBillById(id: Int): Bill?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: Bill)
    
    @Delete
    suspend fun deleteBill(bill: Bill)
    
    @Update
    suspend fun updateBill(bill: Bill)
    
    @Query("DELETE FROM bill WHERE id = :id")
    suspend fun deleteBillById(id: Int)
}
