package com.leng.jingzhekt.empty

import java.time.YearMonth

data class MonthlyBill(
    val month: YearMonth,
    val expend: Float,
    val income: Float
)
