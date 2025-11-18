package com.leng.jingzhekt.data.repository

import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.Entity.Type
import com.leng.jingzhekt.data.local.dao.ClassifyDao
import com.leng.jingzhekt.domain.repository.ClassifyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassifyRepositoryImpl @Inject constructor(
    private val classifyDao: ClassifyDao
) : ClassifyRepository {
    
    override fun getAllClassifies(): Flow<List<Classify>> = classifyDao.getAllClassifies()
    
    override suspend fun getClassifyById(id: Int): Classify? = classifyDao.getClassifyById(id)

    override fun getClassifyByType(type: Type): Flow<List<Classify>> = classifyDao.getClassifyByType(type)

    override fun getClassifyByLevel(level: Level): Flow<List<Classify>> = classifyDao.getClassifyByLevel(level)
    override fun getMajorClassify(type: Type, level: Level): Flow<List<Classify>> = classifyDao.getMajorClassify(type,level)

    override fun getMinorClassify(parentId: Int): Flow<List<Classify>> = classifyDao.getMinorClassify(parentId)

    override suspend fun insertClassify(classify: Classify) = classifyDao.insertClassify(classify)
    
    override suspend fun deleteClassify(classify: Classify) = classifyDao.deleteClassify(classify)
    
    override suspend fun updateClassify(classify: Classify) = classifyDao.updateClassify(classify)
}

