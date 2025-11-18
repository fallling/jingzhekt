package com.leng.jingzhekt.domain.usecase

import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.Entity.Type
import com.leng.jingzhekt.domain.repository.ClassifyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClassifyUseCase @Inject constructor(
    private val classifyRepository: ClassifyRepository
){
    operator fun invoke(): Flow<List<Classify>> = classifyRepository.getAllClassifies()
}

class ClassifyGetByTypeUseCase @Inject constructor(
    private val classifyRepository: ClassifyRepository
){
    operator fun invoke(type: Type): Flow<List<Classify>> = classifyRepository.getClassifyByType(type)
}

class GetMinorClassifyUseCase @Inject constructor(
    private val classifyRepository: ClassifyRepository
){
    operator fun invoke(parentId: Int): Flow<List<Classify>> = classifyRepository.getMinorClassify(parentId)
}

class getMajorClassifyUseCase @Inject constructor(
    private val classifyRepository: ClassifyRepository
){
    operator fun invoke(type: Type): Flow<List<Classify>> = classifyRepository.getMajorClassify(type = type, Level.Major)
}