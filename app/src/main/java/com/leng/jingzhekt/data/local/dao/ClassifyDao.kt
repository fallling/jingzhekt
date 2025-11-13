package com.leng.jingzhekt.data.local.dao

import androidx.room.*
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.Type
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassifyDao {
    @Query("SELECT * FROM classify ORDER BY name ASC")
    fun getAllClassifies(): Flow<List<Classify>>
    
    @Query("SELECT * FROM classify WHERE id = :id")
    suspend fun getClassifyById(id: Int): Classify?

    @Query("SELECT * FROM classify WHERE type = :type")
    fun getClassifyByType(type: Type): Flow<List<Classify>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassify(classify: Classify)
    
    @Delete
    suspend fun deleteClassify(classify: Classify)
    
    @Update
    suspend fun updateClassify(classify: Classify)
}
