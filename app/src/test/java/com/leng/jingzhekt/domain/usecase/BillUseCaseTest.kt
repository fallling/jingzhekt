package com.leng.jingzhekt.domain.usecase

import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.BillType
import com.leng.jingzhekt.domain.repository.BillRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.*
import java.time.LocalDateTime

/**
 * BillUseCase 单元测试
 * 测试账单相关的所有UseCase
 */
class BillUseCaseTest {

    private lateinit var billRepository: BillRepository
    private lateinit var getBillsUseCase: GetBillsUseCase
    private lateinit var getBillsByDateRangeUseCase: GetBillsByDateRangeUseCase
    private lateinit var insertBillUseCase: InsertBillUseCase
    private lateinit var deleteBillUseCase: DeleteBillUseCase
    private lateinit var updateBillUseCase: UpdateBillUseCase

    @Before
    fun setup() {
        billRepository = mock()
        getBillsUseCase = GetBillsUseCase(billRepository)
        getBillsByDateRangeUseCase = GetBillsByDateRangeUseCase(billRepository)
        insertBillUseCase = InsertBillUseCase(billRepository)
        deleteBillUseCase = DeleteBillUseCase(billRepository)
        updateBillUseCase = UpdateBillUseCase(billRepository)
    }

    @Test
    fun `GetBillsUseCase should return all bills from repository`() = runTest {
        // Given: Repository返回账单列表
        val bills = listOf(
            createTestBill(1, BillType.EXPEND, 100.0f),
            createTestBill(2, BillType.INCOME, 200.0f)
        )
        whenever(billRepository.getAllBills()).thenReturn(flowOf(bills))

        // When: 调用UseCase
        val result = getBillsUseCase()

        // Then: 应该返回正确的账单列表
        assertEquals(bills, result.first())
        verify(billRepository).getAllBills()
    }

    @Test
    fun `GetBillsByDateRangeUseCase should return filtered bills`() = runTest {
        // Given: 日期范围和账单
        val startDate = LocalDateTime.of(2024, 1, 1, 0, 0)
        val endDate = LocalDateTime.of(2024, 1, 31, 23, 59)
        val bills = listOf(
            createTestBill(1, BillType.EXPEND, 100.0f, startDate.plusDays(5))
        )
        whenever(billRepository.getBillsByDateRange(startDate, endDate))
            .thenReturn(flowOf(bills))

        // When: 调用UseCase
        val result = getBillsByDateRangeUseCase(startDate, endDate)

        // Then: 应该返回过滤后的账单
        assertEquals(bills, result.first())
        verify(billRepository).getBillsByDateRange(startDate, endDate)
    }

    @Test
    fun `InsertBillUseCase should insert bill via repository`() = runTest {
        // Given: 一个账单对象
        val bill = createTestBill(1, BillType.EXPEND, 100.0f)
        doNothing().whenever(billRepository).insertBill(bill)

        // When: 调用UseCase
        insertBillUseCase(bill)

        // Then: 应该调用Repository的insertBill方法
        verify(billRepository).insertBill(bill)
    }

    @Test
    fun `DeleteBillUseCase should delete bill via repository`() = runTest {
        // Given: 一个账单对象
        val bill = createTestBill(1, BillType.EXPEND, 100.0f)
        doNothing().whenever(billRepository).deleteBill(bill)

        // When: 调用UseCase
        deleteBillUseCase(bill)

        // Then: 应该调用Repository的deleteBill方法
        verify(billRepository).deleteBill(bill)
    }

    @Test
    fun `UpdateBillUseCase should update bill via repository`() = runTest {
        // Given: 一个账单对象
        val bill = createTestBill(1, BillType.EXPEND, 100.0f)
        doNothing().whenever(billRepository).updateBill(bill)

        // When: 调用UseCase
        updateBillUseCase(bill)

        // Then: 应该调用Repository的updateBill方法
        verify(billRepository).updateBill(bill)
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

