package com.leng.jingzhekt.data.repository

import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.BillType
import com.leng.jingzhekt.data.local.dao.BillDao
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.*
import java.time.LocalDateTime

/**
 * BillRepositoryImpl 单元测试
 * 测试账单仓库实现类的所有方法
 */
class BillRepositoryImplTest {

    private lateinit var billDao: BillDao
    private lateinit var billRepository: BillRepositoryImpl

    @Before
    fun setup() {
        billDao = mock()
        billRepository = BillRepositoryImpl(billDao)
    }

    @Test
    fun `getAllBills should return flow from dao`() = runTest {
        // Given: DAO返回账单列表
        val bills = listOf(
            createTestBill(1, BillType.EXPEND, 100.0f),
            createTestBill(2, BillType.INCOME, 200.0f)
        )
        whenever(billDao.getAllBills()).thenReturn(flowOf(bills))

        // When: 调用getAllBills
        val result = billRepository.getAllBills()

        // Then: 应该返回正确的账单列表
        assertEquals(bills, result.first())
        verify(billDao).getAllBills()
    }

    @Test
    fun `getBillsByDateRange should return filtered bills`() = runTest {
        // Given: 日期范围和账单
        val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        val endDate = LocalDateTime.of(2024, 1, 31, 23, 59)
        val bills = listOf(
            createTestBill(1, BillType.EXPEND, 100.0f, startDate.plusDays(5))
        )
        whenever(billDao.getBillsByDateRange(startDate, endDate)).thenReturn(flowOf(bills))

        // When: 调用getBillsByDateRange
        val result = billRepository.getBillsByDateRange(startDate, endDate)

        // Then: 应该返回过滤后的账单
        assertEquals(bills, result.first())
        verify(billDao).getBillsByDateRange(startDate, endDate)
    }

    @Test
    fun `getBillById should return bill when exists`() = runTest {
        // Given: 存在指定ID的账单
        val bill = createTestBill(1, BillType.EXPEND, 100.0f)
        whenever(billDao.getBillById(1)).thenReturn(bill)

        // When: 调用getBillById
        val result = billRepository.getBillById(1)

        // Then: 应该返回正确的账单
        assertEquals(bill, result)
        verify(billDao).getBillById(1)
    }

    @Test
    fun `getBillById should return null when not exists`() = runTest {
        // Given: 不存在指定ID的账单
        whenever(billDao.getBillById(999)).thenReturn(null)

        // When: 调用getBillById
        val result = billRepository.getBillById(999)

        // Then: 应该返回null
        assertNull(result)
        verify(billDao).getBillById(999)
    }

    @Test
    fun `insertBill should delegate to dao`() = runTest {
        // Given: 一个账单对象
        val bill = createTestBill(1, BillType.EXPEND, 100.0f)
        doNothing().whenever(billDao).insertBill(bill)

        // When: 调用insertBill
        billRepository.insertBill(bill)

        // Then: 应该调用DAO的insertBill方法
        verify(billDao).insertBill(bill)
    }

    @Test
    fun `deleteBill should delegate to dao`() = runTest {
        // Given: 一个账单对象
        val bill = createTestBill(1, BillType.EXPEND, 100.0f)
        doNothing().whenever(billDao).deleteBill(bill)

        // When: 调用deleteBill
        billRepository.deleteBill(bill)

        // Then: 应该调用DAO的deleteBill方法
        verify(billDao).deleteBill(bill)
    }

    @Test
    fun `updateBill should delegate to dao`() = runTest {
        // Given: 一个账单对象
        val bill = createTestBill(1, BillType.EXPEND, 100.0f)
        doNothing().whenever(billDao).updateBill(bill)

        // When: 调用updateBill
        billRepository.updateBill(bill)

        // Then: 应该调用DAO的updateBill方法
        verify(billDao).updateBill(bill)
    }

    @Test
    fun `deleteBillById should delegate to dao`() = runTest {
        // Given: 一个账单ID
        val billId = 1
        doNothing().whenever(billDao).deleteBillById(billId)

        // When: 调用deleteBillById
        billRepository.deleteBillById(billId)

        // Then: 应该调用DAO的deleteBillById方法
        verify(billDao).deleteBillById(billId)
    }

    // 辅助方法：创建测试账单
    private fun createTestBill(
        id: Int = 0,
        type: BillType = BillType.EXPEND,
        amount: Float = 100.0f,
        time: LocalDateTime = LocalDateTime.now()
    ): Bill {
        return Bill.create(
            classifyId = 1,
            type = type,
            time = time,
            amount = amount,
            remarks = "测试备注"
        ).copy(id = id)
    }
}

