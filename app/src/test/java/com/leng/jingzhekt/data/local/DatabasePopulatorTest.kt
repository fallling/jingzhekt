package com.leng.jingzhekt.data.local

import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.BillType
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.Entity.Type
import com.leng.jingzhekt.R
import com.leng.jingzhekt.data.local.dao.BillDao
import com.leng.jingzhekt.data.local.dao.ClassifyDao
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.*

/**
 * DatabasePopulator 单元测试
 * 测试数据库初始化数据填充逻辑
 */
class DatabasePopulatorTest {

    private lateinit var classifyDao: ClassifyDao
    private lateinit var billDao: BillDao
    private lateinit var databasePopulator: DatabasePopulator

    @Before
    fun setup() {
        classifyDao = mock()
        billDao = mock()
        databasePopulator = DatabasePopulator(classifyDao, billDao)
    }

    @Test
    fun `populateIfEmpty should populate data when database is empty`() = runTest {
        // Given: 数据库为空
        whenever(classifyDao.getAllClassifies()).thenReturn(flowOf(emptyList()))

        // When: 调用填充方法
        databasePopulator.populateIfEmpty()

        // Then: 应该插入分类和账单数据
        verify(classifyDao, atLeastOnce()).insertClassify(any())
        verify(billDao, atLeastOnce()).insertBill(any())
    }

    @Test
    fun `populateIfEmpty should not populate data when database is not empty`() = runTest {
        // Given: 数据库已有数据
        val existingClassify = Classify.create("餐饮", R.drawable.icon_food, Level.Major, Type.Expend)
        whenever(classifyDao.getAllClassifies()).thenReturn(flowOf(listOf(existingClassify)))

        // When: 调用填充方法
        databasePopulator.populateIfEmpty()

        // Then: 不应该插入新数据
        verify(classifyDao, never()).insertClassify(any())
        verify(billDao, never()).insertBill(any())
    }

    @Test
    fun `populateIfEmpty should insert major classifies first`() = runTest {
        // Given: 数据库为空
        whenever(classifyDao.getAllClassifies())
            .thenReturn(flowOf(emptyList())) // 第一次调用返回空
            .thenReturn(flowOf(createMockMajorClassifies())) // 第二次调用返回主分类

        // When: 调用填充方法
        databasePopulator.populateIfEmpty()

        // Then: 应该先插入主分类
        verify(classifyDao, atLeast(7)).insertClassify(argThat {
            this.level == Level.Major
        })
    }

    @Test
    fun `populateIfEmpty should insert minor classifies with correct parentId`() = runTest {
        // Given: 数据库为空，然后返回主分类
        val majorClassifies = createMockMajorClassifies()
        whenever(classifyDao.getAllClassifies())
            .thenReturn(flowOf(emptyList()))
            .thenReturn(flowOf(majorClassifies))

        // When: 调用填充方法
        databasePopulator.populateIfEmpty()

        // Then: 应该插入子分类，并且有正确的parentId
        verify(classifyDao, atLeastOnce()).insertClassify(argThat {
            this.level == Level.Minor && this.parentId != null
        })
    }

    @Test
    fun `populateIfEmpty should insert default bills`() = runTest {
        // Given: 数据库为空，然后返回所有分类
        val allClassifies = createMockAllClassifies()
        whenever(classifyDao.getAllClassifies())
            .thenReturn(flowOf(emptyList()))
            .thenReturn(flowOf(allClassifies))

        // When: 调用填充方法
        databasePopulator.populateIfEmpty()

        // Then: 应该插入默认账单
        verify(billDao, atLeast(3)).insertBill(any())
    }

    @Test
    fun `populateIfEmpty should create bills with correct types`() = runTest {
        // Given: 数据库为空，然后返回所有分类
        val allClassifies = createMockAllClassifies()
        whenever(classifyDao.getAllClassifies())
            .thenReturn(flowOf(emptyList()))
            .thenReturn(flowOf(allClassifies))

        // When: 调用填充方法
        databasePopulator.populateIfEmpty()

        // Then: 应该插入支出和收入类型的账单
        verify(billDao, atLeastOnce()).insertBill(argThat {
            this.type == BillType.EXPEND
        })
        verify(billDao, atLeastOnce()).insertBill(argThat {
            this.type == BillType.INCOME
        })
    }

    // 辅助方法：创建模拟的主分类
    private fun createMockMajorClassifies(): List<Classify> {
        return listOf(
            Classify.create("餐饮", R.drawable.icon_food, Level.Major, Type.Expend).copy(id = 1),
            Classify.create("购物", R.drawable.icon_shopping, Level.Major, Type.Expend).copy(id = 2),
            Classify.create("交通", R.drawable.icon_traffic, Level.Major, Type.Expend).copy(id = 3),
            Classify.create("工资", R.drawable.icon_salary, Level.Major, Type.Income).copy(id = 8)
        )
    }

    // 辅助方法：创建模拟的所有分类（包括主分类和子分类）
    private fun createMockAllClassifies(): List<Classify> {
        val major = createMockMajorClassifies()
        val minor = listOf(
            Classify.create("早餐", R.drawable.icon_food, Level.Minor, Type.Expend, parentId = 1).copy(id = 11),
            Classify.create("午餐", R.drawable.icon_food, Level.Minor, Type.Expend, parentId = 1).copy(id = 12),
            Classify.create("公交", R.drawable.icon_traffic, Level.Minor, Type.Expend, parentId = 3).copy(id = 13)
        )
        return major + minor
    }
}

