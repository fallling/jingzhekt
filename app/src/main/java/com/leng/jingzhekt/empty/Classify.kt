package com.leng.jingzhekt.empty

import android.graphics.drawable.Icon
import java.util.concurrent.atomic.AtomicInteger

enum class Level {
    Major,Minor
}

data class Classify(
    val id : Int,
    val name: String,
    val iconResId: Int,
    val level: Level
){
    companion object{
        private val idGenerator = AtomicInteger(1)

        fun create(name:String, iconResId: Int, level: Level):Classify{
            return Classify(idGenerator.getAndIncrement(), name,iconResId, level)
        }
    }
}
