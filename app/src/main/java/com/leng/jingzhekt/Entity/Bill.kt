package com.leng.jingzhekt.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import java.time.LocalDateTime

enum class BillType {
    EXPEND, INCOME
}

@Entity(
    tableName = "bill",
    foreignKeys = [
        ForeignKey(
            entity = Classify::class,
            parentColumns = ["id"],
            childColumns = ["classifyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["classifyId"])]
)
data class Bill(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val classifyId: Int,
    val type: BillType,
    val time: LocalDateTime,
    val amount: Float,
    val remarks: String
) {
    companion object {
        fun create(
            classifyId: Int,
            type: BillType,
            time: LocalDateTime,
            amount: Float,
            remarks: String
        ): Bill {
            return Bill(
                classifyId = classifyId,
                type = type,
                time = time,
                amount = amount,
                remarks = remarks
            )
        }
    }
}
