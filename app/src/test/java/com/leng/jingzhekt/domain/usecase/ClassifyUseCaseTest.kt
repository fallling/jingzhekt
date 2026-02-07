package com.leng.jingzhekt.domain.usecase

import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.Entity.Type
import com.leng.jingzhekt.R
import com.leng.jingzhekt.domain.repository.ClassifyRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.*

/**
 * ClassifyUseCase 单元测试
 * 测试分类相关的所有UseCase
 */
class ClassifyUseCaseTest {

    private lateinit var classifyRepository: ClassifyRepository
    private lateinit var classifyUseCase: ClassifyUseCase
    private lateinit var classifyGetByTypeUseCase: ClassifyGetByTypeUseCase
    private lateinit var getMinorClassifyUseCase: GetMinorClassifyUseCase
    private lateinit var getMajorClassifyUseCase: getMajorClassifyUseCase

    @Before
    fun setup() {
        classifyRepository = mock()
        classifyUseCase = ClassifyUseCase(classifyRepository)
        classifyGetByTypeUseCase = ClassifyGetByTypeUseCase(classifyRepository)
        getMinorClassifyUseCase = GetMinorClassifyUseCase(classifyRepository)
        getMajorClassifyUseCase = getMajorClassifyUseCase(classifyRepository)
    }

    @Test
    fun `ClassifyUseCase should return all classifies from repository`() = runTest {
        // Given: Repository返回分类列表
        val classifies = listOf(
            createTestClassify(1, "餐饮", Level.Major, Type.Expend),
            createTestClassify(2, "购物", Level.Major, Type.Expend)
        )
        whenever(classifyRepository.getAllClassifies()).thenReturn(flowOf(classifies))

        // When: 调用UseCase
        val result = classifyUseCase()

        // Then: 应该返回正确的分类列表
        assertEquals(classifies, result.first())
        verify(classifyRepository).getAllClassifies()
    }

    @Test
    fun `ClassifyGetByTypeUseCase should return classifies by type`() = runTest {
        // Given: 按类型过滤的分类
        val expendClassifies = listOf(
            createTestClassify(1, "餐饮", Level.Major, Type.Expend),
            createTestClassify(2, "购物", Level.Major, Type.Expend)
        )
        whenever(classifyRepository.getClassifyByType(Type.Expend))
            .thenReturn(flowOf(expendClassifies))

        // When: 调用UseCase
        val result = classifyGetByTypeUseCase(Type.Expend)

        // Then: 应该返回正确类型的分类
        assertEquals(expendClassifies, result.first())
        verify(classifyRepository).getClassifyByType(Type.Expend)
    }

    @Test
    fun `GetMinorClassifyUseCase should return minor classifies by parentId`() = runTest {
        // Given: 子分类列表
        val minorClassifies = listOf(
            createTestClassify(11, "早餐", Level.Minor, Type.Expend, parentId = 1),
            createTestClassify(12, "午餐", Level.Minor, Type.Expend, parentId = 1)
        )
        whenever(classifyRepository.getMinorClassify(1))
            .thenReturn(flowOf(minorClassifies))

        // When: 调用UseCase
        val result = getMinorClassifyUseCase(1)

        // Then: 应该返回子分类
        assertEquals(minorClassifies, result.first())
        verify(classifyRepository).getMinorClassify(1)
    }

    @Test
    fun `getMajorClassifyUseCase should return major classifies by type`() = runTest {
        // Given: 主分类列表
        val majorClassifies = listOf(
            createTestClassify(1, "餐饮", Level.Major, Type.Expend),
            createTestClassify(2, "购物", Level.Major, Type.Expend)
        )
        whenever(classifyRepository.getMajorClassify(Type.Expend, Level.Major))
            .thenReturn(flowOf(majorClassifies))

        // When: 调用UseCase
        val result = getMajorClassifyUseCase(Type.Expend)

        // Then: 应该返回主分类
        assertEquals(majorClassifies, result.first())
        verify(classifyRepository).getMajorClassify(Type.Expend, Level.Major)
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

