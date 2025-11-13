package com.leng.jingzhekt.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Level {
    Major, Minor
}

enum class Type {
    Income, Expend
}

@Entity(tableName = "classify")
data class Classify(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val iconResId: Int,
    val level: Level,
    val type:Type
) {
    companion object {
        fun create(name: String, iconResId: Int, level: Level, type: Type): Classify {
            return Classify(
                name = name,
                iconResId = iconResId,
                level = level,
                type = type
            )
        }
    }
}
