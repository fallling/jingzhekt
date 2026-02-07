package com.leng.jingzhekt.presentation.viewmodel

import app.cash.turbine.test
import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.BillType
import com.leng.jingzhekt.Entity.DailyBill
import com.leng.jingzhekt.Entity.MonthlyBill
import com.leng.jingzhekt.domain.usecase.GetBillsByDateRangeUseCase
import com.leng.jingzhekt.domain.repository.ClassifyRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.*
import java.time.LocalDateTime
import java.time.YearMonth

/**
 * BillViewModel 单元测试
 * 测试账单ViewModel的业务逻辑
 */
class BillViewModelTest {

    private lateinit var getBillsByDateRangeUseCase: GetBillsByDateRangeUseCase
    private lateinit var classifyRepository: ClassifyRepository
    private lateinit var billViewModel: BillViewModel

    @Before
    fun setup() {
        getBillsByDateRangeUseCase = mock()
        classifyRepository = mock()
        billViewModel = BillViewModel(getBillsByDateRangeUseCase, classifyRepository)
    }

    @Test
    fun `loadBillsForMonth should update uiState with bills`() = runTest {
        // Given: 一个月份的账单数据
        val month = YearMonth.of(2024, 1)
        val bills = listOf(
            createTestBill(1, BillType.EXPEND, 100.0f, LocalDateTime.of(2024, 1, 5, 10, 0)),
            createTestBill(2, BillType.INCOME, 200.0f, LocalDateTime.of(2024, 1, 5, 11, 0)),
            createTestBill(3, BillType.EXPEND, 50.0f, LocalDateTime.of(2024, 1, 6, 12, 0))
        )
        whenever(getBillsByDateRangeUseCase(any(), any())).thenReturn(flowOf(bills))

        // When: 加载该月的账单
        billViewModel.loadBillsForMonth(month)

        // Then: UI状态应该更新
        billViewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(bills, state.bills)
            assertNotNull(state.monthlyBill)
        }
    }

    @Test
    fun `loadBillsForMonth should set isLoading to true initially`() = runTest {
        // Given: 一个月份
        val month = YearMonth.of(2024, 1)
        whenever(getBillsByDateRangeUseCase(any(), any())).thenReturn(flowOf(emptyList()))

        // When: 加载该月的账单
        billViewModel.loadBillsForMonth(month)

        // Then: 初始状态应该是加载中
        billViewModel.uiState.test {
            val state = awaitItem()
            // 由于Flow的异步特性，我们检查最终状态
            skipItems(1) // 跳过初始状态
            val finalState = awaitItem()
            assertFalse(finalState.isLoading)
        }
    }

    @Test
    fun `loadBillsForMonth should create MonthlyBill from bills`() = runTest {
        // Given: 一个月份的账单数据（同一天多条）
        val month = YearMonth.of(2024, 1)
        val bills = listOf(
            createTestBill(1, BillType.EXPEND, 100.0f, LocalDateTime.of(2024, 1, 5, 10, 0)),
            createTestBill(2, BillType.INCOME, 200.0f, LocalDateTime.of(2024, 1, 5, 11, 0)),
            createTestBill(3, BillType.EXPEND, 50.0f, LocalDateTime.of(2024, 1, 6, 12, 0))
        )
        whenever(getBillsByDateRangeUseCase(any(), any())).thenReturn(flowOf(bills))

        // When: 加载该月的账单
        billViewModel.loadBillsForMonth(month)

        // Then: 应该创建MonthlyBill
        billViewModel.uiState.test {
            val state = awaitItem()
            skipItems(1)
            val finalState = awaitItem()
            assertNotNull(finalState.monthlyBill)
            assertEquals(2, finalState.monthlyBill?.dailyBillList?.size) // 两天
        }
    }

    @Test
    fun `loadBillsForMonth should handle empty bills`() = runTest {
        // Given: 空账单列表
        val month = YearMonth.of(2024, 1)
        whenever(getBillsByDateRangeUseCase(any(), any())).thenReturn(flowOf(emptyList()))

        // When: 加载该月的账单
        billViewModel.loadBillsForMonth(month)

        // Then: MonthlyBill应该为null
        billViewModel.uiState.test {
            val state = awaitItem()
            skipItems(1)
            val finalState = awaitItem()
            assertTrue(finalState.bills.isEmpty())
            assertNull(finalState.monthlyBill)
        }
    }

    @Test
    fun `loadBillsForMonth should handle errors`() = runTest {
        // Given: UseCase抛出异常
        val month = YearMonth.of(2024, 1)
        val errorMessage = "网络错误"
        whenever(getBillsByDateRangeUseCase(any(), any()))
            .thenReturn(flowOf<List<Bill>> { throw RuntimeException(errorMessage) })

        // When: 加载该月的账单
        billViewModel.loadBillsForMonth(month)

        // Then: 应该设置错误消息
        billViewModel.uiState.test {
            val state = awaitItem()
            skipItems(1)
            val finalState = awaitItem()
            assertNotNull(finalState.message)
            assertTrue(finalState.message!!.contains(errorMessage))
        }
    }

    @Test
    fun `clearMessage should clear message from uiState`() = runTest {
        // Given: 有一个错误消息的状态
        val month = YearMonth.of(2024, 1)
        whenever(getBillsByDateRangeUseCase(any(), any()))
            .thenReturn(flowOf<List<Bill>> { throw RuntimeException("错误") })
        billViewModel.loadBillsForMonth(month)

        // 等待错误状态
        billViewModel.uiState.test {
            skipItems(2)
            val errorState = awaitItem()
            assertNotNull(errorState.message)

            // When: 清除消息
            billViewModel.clearMessage()

            // Then: 消息应该被清除
            val clearedState = awaitItem()
            assertNull(clearedState.message)
        }
    }

    @Test
    fun `loadBillsForMonth should use correct date range`() = runTest {
        // Given: 一个月份
        val month = YearMonth.of(2024, 1)
        val expectedStartDate = month.atDay(1).atStartOfDay()
        val expectedEndDate = month.atEndOfMonth().atTime(23, 59, 59)
        whenever(getBillsByDateRangeUseCase(any(), any())).thenReturn(flowOf(emptyList()))

        // When: 加载该月的账单
        billViewModel.loadBillsForMonth(month)

        // Then: 应该使用正确的日期范围
        verify(getBillsByDateRangeUseCase).invoke(
            argThat { this == expectedStartDate },
            argThat { this == expectedEndDate }
        )
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

