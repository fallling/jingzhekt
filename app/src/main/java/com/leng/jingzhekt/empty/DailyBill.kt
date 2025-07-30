package com.leng.jingzhekt.empty

import java.time.LocalDate

data class DailyBill(
    val date: LocalDate,
    val billList: List<Bill>,
    val expand: Float,
    val income: Float,
) {
    val dailyAmount: Float get() = expand + income
}
