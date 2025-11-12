package com.leng.jingzhekt.di

import android.content.Context
import androidx.room.Room
import com.leng.jingzhekt.data.local.AppDatabase
import com.leng.jingzhekt.data.local.dao.BillDao
import com.leng.jingzhekt.data.local.dao.ClassifyDao
import com.leng.jingzhekt.data.local.DatabaseInitializer
import com.leng.jingzhekt.data.local.DatabasePopulator
import com.leng.jingzhekt.data.repository.BillRepositoryImpl
import com.leng.jingzhekt.data.repository.ClassifyRepositoryImpl
import com.leng.jingzhekt.domain.repository.BillRepository
import com.leng.jingzhekt.domain.repository.ClassifyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "jingzhe_database"
        ).build()
    }
    
    @Provides
    fun provideBillDao(database: AppDatabase): BillDao = database.billDao()
    
    @Provides
    fun provideClassifyDao(database: AppDatabase): ClassifyDao = database.classifyDao()
    
    @Provides
    @Singleton
    fun provideBillRepository(billDao: BillDao): BillRepository = BillRepositoryImpl(billDao)
    
    @Provides
    @Singleton
    fun provideClassifyRepository(classifyDao: ClassifyDao): ClassifyRepository = ClassifyRepositoryImpl(classifyDao)
    
    @Provides
    @Singleton
    fun provideDatabasePopulator(
        classifyDao: ClassifyDao,
        billDao: BillDao
    ): DatabasePopulator = DatabasePopulator(classifyDao, billDao)
    
    @Provides
    @Singleton
    fun provideDatabaseInitializer(
        databasePopulator: DatabasePopulator
    ): DatabaseInitializer = DatabaseInitializer(databasePopulator)
}
