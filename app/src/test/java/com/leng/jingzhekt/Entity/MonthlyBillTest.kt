package com.leng.jingzhekt.Entity

import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDateTime
import java.time.YearMonth

/**
 * MonthlyBill Entity 单元测试
 * 测试MonthlyBill的计算逻辑
 */
class MonthlyBillTest {

    @Test
    fun `income should sum all daily income`() {
        // Given: 多天的账单
        val dailyBills = listOf(
            DailyBill(listOf(
                Bill.create(1, BillType.INCOME, LocalDateTime.of(2024, 1, 15, 10, 0), 1000.0f, "工资"),
                Bill.create(1, BillType.EXPEND, LocalDateTime.of(2024, 1, 15, 12, 0), 50.0f, "午餐")
            )),
            DailyBill(listOf(
                Bill.create(1, BillType.INCOME, LocalDateTime.of(2024, 1, 16, 10, 0), 500.0f, "奖金")
            ))
        )
        val monthlyBill = MonthlyBill(dailyBills)

        // When: 计算月收入
        val income = monthlyBill.income

        // Then: 应该是所有日收入的总和
        assertEquals(1500.0f, income, 0.01f)
    }

    @Test
    fun `expend should sum all daily expend`() {
        // Given: 多天的账单
        val dailyBills = listOf(
            DailyBill(listOf(
                Bill.create(1, BillType.INCOME, LocalDateTime.of(2024, 1, 15, 10, 0), 1000.0f, "工资"),
                Bill.create(1, BillType.EXPEND, LocalDateTime.of(2024, 1, 15, 12, 0), 50.0f, "午餐")
            )),
            DailyBill(listOf(
                Bill.create(1, BillType.EXPEND, LocalDateTime.of(2024, 1, 16, 10, 0), 100.0f, "购物")
            ))
        )
        val monthlyBill = MonthlyBill(dailyBills)

        // When: 计算月支出
        val expend = monthlyBill.expend

        // Then: 应该是所有日支出的总和
        assertEquals(150.0f, expend, 0.01f)
    }

    @Test
    fun `month should return correct YearMonth`() {
        // Given: 一月的账单
        val dailyBills = listOf(
            DailyBill(listOf(
                Bill.create(1, BillType.EXPEND, LocalDateTime.of(2024, 1, 15, 10, 0), 50.0f, "午餐")
            ))
        )
        val monthlyBill = MonthlyBill(dailyBills)

        // When: 获取月份
        val month = monthlyBill.month

        // Then: 应该是一月
        assertEquals(YearMonth.of(2024, 1), month)
    }

    @Test
    fun `create should throw exception when dailyBills have different months`() {
        // Given: 不同月份的日账单
        val dailyBills = listOf(
            DailyBill(listOf(
                Bill.create(1, BillType.EXPEND, LocalDateTime.of(2024, 1, 15, 10, 0), 50.0f, "午餐")
            )),
            DailyBill(listOf(
                Bill.create(1, BillType.EXPEND, LocalDateTime.of(2024, 2, 15, 10, 0), 50.0f, "午餐")
            ))
        )

        // When/Then: 创建MonthlyBill应该抛出异常
        try {
            MonthlyBill.create(dailyBills)
            fail("应该抛出IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("同一月的bill"))
        }
    }
}

