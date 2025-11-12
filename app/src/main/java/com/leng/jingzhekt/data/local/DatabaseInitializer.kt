package com.leng.jingzhekt.data.local

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseInitializer @Inject constructor(
    private val databasePopulator: DatabasePopulator
) {
    
    fun initialize() {
        CoroutineScope(Dispatchers.IO).launch {
            databasePopulator.populateIfEmpty()
        }
    }
}
