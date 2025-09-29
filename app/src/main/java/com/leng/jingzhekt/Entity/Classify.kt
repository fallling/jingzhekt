package com.leng.jingzhekt.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Level {
    Major, Minor
}

@Entity(tableName = "classify")
data class Classify(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val iconResId: Int,
    val level: Level
) {
    companion object {
        fun create(name: String, iconResId: Int, level: Level): Classify {
            return Classify(
                name = name,
                iconResId = iconResId,
                level = level
            )
        }
    }
}
