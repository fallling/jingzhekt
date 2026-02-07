package com.leng.jingzhekt.Entity

import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDateTime

/**
 * Bill Entity 单元测试
 * 测试Bill实体的创建和属性
 */
class BillTest {

    @Test
    fun `create should create bill with correct properties`() {
        // Given: 账单参数
        val classifyId = 1
        val type = BillType.EXPEND
        val time = LocalDateTime.of(2024, 1, 15, 10, 30)
        val amount = 100.5f
        val remarks = "测试备注"

        // When: 创建账单
        val bill = Bill.create(
            classifyId = classifyId,
            type = type,
            time = time,
            amount = amount,
            remarks = remarks
        )

        // Then: 应该设置正确的属性
        assertEquals(classifyId, bill.classifyId)
        assertEquals(type, bill.type)
        assertEquals(time, bill.time)
        assertEquals(amount, bill.amount, 0.01f)
        assertEquals(remarks, bill.remarks)
        assertEquals(0, bill.id) // 默认id为0
    }

    @Test
    fun `create should create income bill`() {
        // Given: 收入类型
        val bill = Bill.create(
            classifyId = 1,
            type = BillType.INCOME,
            time = LocalDateTime.now(),
            amount = 5000.0f,
            remarks = "工资"
        )

        // Then: 应该是收入类型
        assertEquals(BillType.INCOME, bill.type)
    }

    @Test
    fun `create should create expend bill`() {
        // Given: 支出类型
        val bill = Bill.create(
            classifyId = 1,
            type = BillType.EXPEND,
            time = LocalDateTime.now(),
            amount = 50.0f,
            remarks = "午餐"
        )

        // Then: 应该是支出类型
        assertEquals(BillType.EXPEND, bill.type)
    }
}

