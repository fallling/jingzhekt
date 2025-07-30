package com.leng.jingzhekt.Entity

import java.time.YearMonth

data class MonthlyBill(
    val dailyBillList:List<DailyBill>
) {
    val month: YearMonth get() = YearMonth.from(dailyBillList[0].date)
    val income: Float get() {
        var sum = 0f
        dailyBillList.forEachIndexed { index, bill ->
            sum += bill.income
        }
        return sum
    }
    val expend: Float get() {
        var sum = 0f
        dailyBillList.forEachIndexed { index, bill ->
            sum += bill.expand
        }
        return sum
    }

    companion object{
        fun create(dailyBillList: List<DailyBill>): MonthlyBill{
            val current_month = YearMonth.from(dailyBillList[0].date)

            dailyBillList.forEachIndexed { index, bill ->
                if(current_month != YearMonth.from(bill.date)){
                    throw IllegalArgumentException("同一月的bill，月份必须保持相同")
                }
            }
            return MonthlyBill(dailyBillList)
        }
    }
}
