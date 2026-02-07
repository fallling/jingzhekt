package com.leng.jingzhekt.Entity

import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * DailyBill Entity 单元测试
 * 测试DailyBill的计算逻辑
 */
class DailyBillTest {

    @Test
    fun `dailyAmount should be sum of expand and income`() {
        // Given: 包含支出和收入的账单列表
        val date = LocalDateTime.of(2024, 1, 15, 10, 0)
        val bills = listOf(
            Bill.create(1, BillType.EXPEND, date, 100.0f, "早餐"),
            Bill.create(1, BillType.INCOME, date.plusHours(1), 200.0f, "工资"),
            Bill.create(1, BillType.EXPEND, date.plusHours(2), 50.0f, "午餐")
        )
        val dailyBill = DailyBill(bills)

        // When: 计算日总金额
        val dailyAmount = dailyBill.dailyAmount

        // Then: 应该是支出和收入的总和
        assertEquals(350.0f, dailyAmount, 0.01f)
    }

    @Test
    fun `expand should sum only expend bills`() {
        // Given: 包含支出和收入的账单列表
        val date = LocalDateTime.of(2024, 1, 15, 10, 0)
        val bills = listOf(
            Bill.create(1, BillType.EXPEND, date, 100.0f, "早餐"),
            Bill.create(1, BillType.INCOME, date.plusHours(1), 200.0f, "工资"),
            Bill.create(1, BillType.EXPEND, date.plusHours(2), 50.0f, "午餐")
        )
        val dailyBill = DailyBill(bills)

        // When: 计算支出总额
        val expand = dailyBill.expand

        // Then: 应该只计算支出
        assertEquals(150.0f, expand, 0.01f)
    }

    @Test
    fun `income should sum only income bills`() {
        // Given: 包含支出和收入的账单列表
        val date = LocalDateTime.of(2024, 1, 15, 10, 0)
        val bills = listOf(
            Bill.create(1, BillType.EXPEND, date, 100.0f, "早餐"),
            Bill.create(1, BillType.INCOME, date.plusHours(1), 200.0f, "工资"),
            Bill.create(1, BillType.EXPEND, date.plusHours(2), 50.0f, "午餐")
        )
        val dailyBill = DailyBill(bills)

        // When: 计算收入总额
        val income = dailyBill.income

        // Then: 应该只计算收入
        assertEquals(200.0f, income, 0.01f)
    }

    @Test
    fun `date should return first bill date`() {
        // Given: 账单列表
        val date = LocalDateTime.of(2024, 1, 15, 10, 0)
        val bills = listOf(
            Bill.create(1, BillType.EXPEND, date, 100.0f, "早餐"),
            Bill.create(1, BillType.EXPEND, date.plusHours(1), 50.0f, "午餐")
        )
        val dailyBill = DailyBill(bills)

        // When: 获取日期
        val billDate = dailyBill.date

        // Then: 应该是第一个账单的日期
        assertEquals(LocalDate.of(2024, 1, 15), billDate)
    }

    @Test
    fun `create should throw exception when bills have different dates`() {
        // Given: 不同日期的账单列表
        val bills = listOf(
            Bill.create(1, BillType.EXPEND, LocalDateTime.of(2024, 1, 15, 10, 0), 100.0f, "早餐"),
            Bill.create(1, BillType.EXPEND, LocalDateTime.of(2024, 1, 16, 10, 0), 50.0f, "午餐")
        )

        // When/Then: 创建DailyBill应该抛出异常
        try {
            DailyBill.create(bills)
            fail("应该抛出IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("同一天的bill"))
        }
    }
}

