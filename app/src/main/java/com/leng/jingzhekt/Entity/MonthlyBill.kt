package com.leng.jingzhekt.Entity

import java.time.YearMonth

data class MonthlyBill(
    val month: YearMonth,
    val expend: Float,
    val income: Float,
    val dailyBillList:List<DailyBill>
)
