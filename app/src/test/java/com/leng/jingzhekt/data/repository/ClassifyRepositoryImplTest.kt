package com.leng.jingzhekt.data.repository

import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.Entity.Type
import com.leng.jingzhekt.R
import com.leng.jingzhekt.data.local.dao.ClassifyDao
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.*

/**
 * ClassifyRepositoryImpl 单元测试
 * 测试分类仓库实现类的所有方法
 */
class ClassifyRepositoryImplTest {

    private lateinit var classifyDao: ClassifyDao
    private lateinit var classifyRepository: ClassifyRepositoryImpl

    @Before
    fun setup() {
        classifyDao = mock()
        classifyRepository = ClassifyRepositoryImpl(classifyDao)
    }

    @Test
    fun `getAllClassifies should return flow from dao`() = runTest {
        // Given: DAO返回分类列表
        val classifies = listOf(
            createTestClassify(1, "餐饮", Level.Major, Type.Expend),
            createTestClassify(2, "购物", Level.Major, Type.Expend)
        )
        whenever(classifyDao.getAllClassifies()).thenReturn(flowOf(classifies))

        // When: 调用getAllClassifies
        val result = classifyRepository.getAllClassifies()

        // Then: 应该返回正确的分类列表
        assertEquals(classifies, result.first())
        verify(classifyDao).getAllClassifies()
    }

    @Test
    fun `getClassifyById should return classify when exists`() = runTest {
        // Given: 存在指定ID的分类
        val classify = createTestClassify(1, "餐饮", Level.Major, Type.Expend)
        whenever(classifyDao.getClassifyById(1)).thenReturn(classify)

        // When: 调用getClassifyById
        val result = classifyRepository.getClassifyById(1)

        // Then: 应该返回正确的分类
        assertEquals(classify, result)
        verify(classifyDao).getClassifyById(1)
    }

    @Test
    fun `getClassifyById should return null when not exists`() = runTest {
        // Given: 不存在指定ID的分类
        whenever(classifyDao.getClassifyById(999)).thenReturn(null)

        // When: 调用getClassifyById
        val result = classifyRepository.getClassifyById(999)

        // Then: 应该返回null
        assertNull(result)
        verify(classifyDao).getClassifyById(999)
    }

    @Test
    fun `getClassifyByType should return filtered classifies`() = runTest {
        // Given: 按类型过滤的分类
        val expendClassifies = listOf(
            createTestClassify(1, "餐饮", Level.Major, Type.Expend),
            createTestClassify(2, "购物", Level.Major, Type.Expend)
        )
        whenever(classifyDao.getClassifyByType(Type.Expend)).thenReturn(flowOf(expendClassifies))

        // When: 调用getClassifyByType
        val result = classifyRepository.getClassifyByType(Type.Expend)

        // Then: 应该返回正确类型的分类
        assertEquals(expendClassifies, result.first())
        verify(classifyDao).getClassifyByType(Type.Expend)
    }

    @Test
    fun `getMajorClassify should return major classifies by type`() = runTest {
        // Given: 主分类列表
        val majorClassifies = listOf(
            createTestClassify(1, "餐饮", Level.Major, Type.Expend),
            createTestClassify(2, "购物", Level.Major, Type.Expend)
        )
        whenever(classifyDao.getMajorClassify(Type.Expend, Level.Major))
            .thenReturn(flowOf(majorClassifies))

        // When: 调用getMajorClassify
        val result = classifyRepository.getMajorClassify(Type.Expend, Level.Major)

        // Then: 应该返回主分类
        assertEquals(majorClassifies, result.first())
        verify(classifyDao).getMajorClassify(Type.Expend, Level.Major)
    }

    @Test
    fun `getClassifyByLevel should return classifies by level`() = runTest {
        // Given: 按级别过滤的分类
        val majorClassifies = listOf(
            createTestClassify(1, "餐饮", Level.Major, Type.Expend),
            createTestClassify(2, "购物", Level.Major, Type.Expend)
        )
        whenever(classifyDao.getClassifyByLevel(Level.Major))
            .thenReturn(flowOf(majorClassifies))

        // When: 调用getClassifyByLevel
        val result = classifyRepository.getClassifyByLevel(Level.Major)

        // Then: 应该返回正确级别的分类
        assertEquals(majorClassifies, result.first())
        verify(classifyDao).getClassifyByLevel(Level.Major)
    }

    @Test
    fun `getMinorClassify should return minor classifies by parentId`() = runTest {
        // Given: 子分类列表
        val minorClassifies = listOf(
            createTestClassify(11, "早餐", Level.Minor, Type.Expend, parentId = 1),
            createTestClassify(12, "午餐", Level.Minor, Type.Expend, parentId = 1)
        )
        whenever(classifyDao.getMinorClassify(1)).thenReturn(flowOf(minorClassifies))

        // When: 调用getMinorClassify
        val result = classifyRepository.getMinorClassify(1)

        // Then: 应该返回子分类
        assertEquals(minorClassifies, result.first())
        verify(classifyDao).getMinorClassify(1)
    }

    @Test
    fun `insertClassify should delegate to dao`() = runTest {
        // Given: 一个分类对象
        val classify = createTestClassify(1, "餐饮", Level.Major, Type.Expend)
        doNothing().whenever(classifyDao).insertClassify(classify)

        // When: 调用insertClassify
        classifyRepository.insertClassify(classify)

        // Then: 应该调用DAO的insertClassify方法
        verify(classifyDao).insertClassify(classify)
    }

    @Test
    fun `deleteClassify should delegate to dao`() = runTest {
        // Given: 一个分类对象
        val classify = createTestClassify(1, "餐饮", Level.Major, Type.Expend)
        doNothing().whenever(classifyDao).deleteClassify(classify)

        // When: 调用deleteClassify
        classifyRepository.deleteClassify(classify)

        // Then: 应该调用DAO的deleteClassify方法
        verify(classifyDao).deleteClassify(classify)
    }

    @Test
    fun `updateClassify should delegate to dao`() = runTest {
        // Given: 一个分类对象
        val classify = createTestClassify(1, "餐饮", Level.Major, Type.Expend)
        doNothing().whenever(classifyDao).updateClassify(classify)

        // When: 调用updateClassify
        classifyRepository.updateClassify(classify)

        // Then: 应该调用DAO的updateClassify方法
        verify(classifyDao).updateClassify(classify)
    }

    // 辅助方法：创建测试分类
    private fun createTestClassify(
        id: Int = 0,
        name: String = "测试分类",
        level: Level = Level.Major,
        type: Type = Type.Expend,
        parentId: Int? = null
    ): Classify {
        return Classify.create(
            name = name,
            iconResId = R.drawable.icon_food,
            level = level,
            type = type,
            parentId = parentId
        ).copy(id = id)
    }
}

