package com.leng.jingzhekt.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.data.local.dao.BillDao
import com.leng.jingzhekt.data.local.dao.ClassifyDao
import com.leng.jingzhekt.data.local.converter.LocalDateTimeConverter

@Database(
    entities = [Bill::class, Classify::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun billDao(): BillDao
    abstract fun classifyDao(): ClassifyDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jingzhe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
