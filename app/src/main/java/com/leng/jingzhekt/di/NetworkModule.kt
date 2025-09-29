package com.leng.jingzhekt.di

import com.leng.jingzhekt.data.remote.api.JingzheApiService
import com.leng.jingzhekt.data.remote.config.NetworkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * 网络模块
 * 提供网络相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    /**
     * 提供Retrofit实例
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return NetworkConfig.createRetrofit()
    }
    
    /**
     * 提供API服务
     */
    @Provides
    @Singleton
    fun provideJingzheApiService(retrofit: Retrofit): JingzheApiService {
        return retrofit.create(JingzheApiService::class.java)
    }
}

