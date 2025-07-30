package com.leng.jingzhekt.Entity

import java.time.LocalDate

data class DailyBill(
    val date: LocalDate,
    val billList: List<Bill>,
    val expand: Float,
    val income: Float,
) {
    val dailyAmount: Float get() = expand + income

    companion object{
        fun create(billList: List<Bill>):DailyBill{
            var currentMonth: LocalDate? = billList[0].time.toLocalDate()
            billList.forEachIndexed { index, bill ->
                if(currentMonth)

                currentMonth = bill.time.toLocalDate()
            }

            return DailyBill()
        }
    }
}
