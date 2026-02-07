package com.leng.jingzhekt.Entity

import com.leng.jingzhekt.R
import org.junit.Test
import org.junit.Assert.*

/**
 * Classify Entity 单元测试
 * 测试Classify实体的创建和属性
 */
class ClassifyTest {

    @Test
    fun `create should create classify with correct properties`() {
        // Given: 分类参数
        val name = "餐饮"
        val iconResId = R.drawable.icon_food
        val level = Level.Major
        val type = Type.Expend

        // When: 创建分类
        val classify = Classify.create(
            name = name,
            iconResId = iconResId,
            level = level,
            type = type
        )

        // Then: 应该设置正确的属性
        assertEquals(name, classify.name)
        assertEquals(iconResId, classify.iconResId)
        assertEquals(level, classify.level)
        assertEquals(type, classify.type)
        assertNull(classify.parentId) // 主分类没有parentId
        assertEquals(0, classify.id) // 默认id为0
    }

    @Test
    fun `create should create minor classify with parentId`() {
        // Given: 子分类参数
        val name = "早餐"
        val parentId = 1

        // When: 创建子分类
        val classify = Classify.create(
            name = name,
            iconResId = R.drawable.icon_food,
            level = Level.Minor,
            type = Type.Expend,
            parentId = parentId
        )

        // Then: 应该有parentId
        assertEquals(parentId, classify.parentId)
        assertEquals(Level.Minor, classify.level)
    }

    @Test
    fun `create should create major classify without parentId`() {
        // Given: 主分类参数
        val classify = Classify.create(
            name = "餐饮",
            iconResId = R.drawable.icon_food,
            level = Level.Major,
            type = Type.Expend
        )

        // Then: 不应该有parentId
        assertNull(classify.parentId)
        assertEquals(Level.Major, classify.level)
    }

    @Test
    fun `create should create income type classify`() {
        // Given: 收入类型分类
        val classify = Classify.create(
            name = "工资",
            iconResId = R.drawable.icon_salary,
            level = Level.Major,
            type = Type.Income
        )

        // Then: 应该是收入类型
        assertEquals(Type.Income, classify.type)
    }

    @Test
    fun `create should create expend type classify`() {
        // Given: 支出类型分类
        val classify = Classify.create(
            name = "餐饮",
            iconResId = R.drawable.icon_food,
            level = Level.Major,
            type = Type.Expend
        )

        // Then: 应该是支出类型
        assertEquals(Type.Expend, classify.type)
    }
}

