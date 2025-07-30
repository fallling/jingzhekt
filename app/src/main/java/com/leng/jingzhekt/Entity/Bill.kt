package com.leng.jingzhekt.Entity

import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger

enum class BillType{
    EXPEND,INCOME
}

data class Bill(
    val id: Int,
    val classify: Classify,
    val type: BillType,
    val time:LocalDateTime,
    val amount: Float,
    val remarks:String,
){
    companion object{
        private val idGenerator = AtomicInteger(1)

        fun create(classify:Classify, type: BillType ,time: LocalDateTime,amount: Float, remarks: String):Bill{
            return Bill(idGenerator.getAndIncrement(), classify, type,time,amount,remarks)
        }
    }
}
