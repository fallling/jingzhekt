package com.leng.jingzhekt.Entity

import android.util.Log
import java.time.LocalDate

data class DailyBill(
    val billList: List<Bill>,
) {
    val dailyAmount: Float get() = expand + income
    val date: LocalDate get() = billList[0].time.toLocalDate()
    val expand: Float get() {
        var sum = 0f
        billList.forEachIndexed { index, bill ->
            if(bill.type == BillType.EXPEND){
                sum += bill.amount
            }
        }
        return sum
    }
    val income: Float get() {
        var sum = 0f
        billList.forEachIndexed { index, bill ->
            if(bill.type == BillType.INCOME){
                sum += bill.amount
            }
        }
        return sum
    }

    companion object{
        fun create(billList: List<Bill>):DailyBill{
            val currentMonth: LocalDate? = billList[0].time.toLocalDate()
            billList.forEachIndexed { index, bill ->
                if (currentMonth != bill.time.toLocalDate()) {
                    throw IllegalArgumentException("同一天的bill，日期必须保持相同")
                }
            }
            return DailyBill(billList)
        }
    }
}
