package com.leng.jingzhekt.domain.usecase

import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.domain.repository.ClassifyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClassifyUseCase @Inject constructor(
    private val classifyRepository: ClassifyRepository
){
    operator fun invoke(): Flow<List<Classify>> = classifyRepository.getAllClassifies()
}