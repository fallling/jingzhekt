package com.leng.jingzhekt.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class Level {
    Major, Minor
}

enum class Type {
    Income, Expend
}

@Entity(
    tableName = "classify",
    foreignKeys = [
        ForeignKey(
            entity = Classify::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.SET_NULL
        )
    ])
data class Classify(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val iconResId: Int,
    val level: Level,
    val type: Type,
    val parentId: Int?
) {
    companion object {
        fun create(name: String, iconResId: Int, level: Level, type: Type, parentId:Int?=null): Classify {
            return Classify(
                name = name,
                iconResId = iconResId,
                level = level,
                type = type,
                parentId = parentId
            )
        }
    }
}
