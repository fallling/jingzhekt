package com.leng.jingzhekt

import android.app.Application
import com.leng.jingzhekt.data.local.DatabaseInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class JingZheApplication : Application() {
    
    @Inject
    lateinit var databaseInitializer: DatabaseInitializer
    
    override fun onCreate() {
        super.onCreate()
        // 初始化数据库，如果为空则填充默认数据
        databaseInitializer.initialize()
    }
}
