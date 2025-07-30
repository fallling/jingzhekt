package com.leng.jingzhekt

import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.BillType
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.DailyBill
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.Entity.MonthlyBill
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

class TestData {

    companion object{
        val classify1 = Classify.create("餐饮", R.drawable.add , Level.Major)
        val classify2 = Classify.create("零食", R.drawable.add , Level.Major)
        val classify3 = Classify.create("日用", R.drawable.add , Level.Major)

        val bill1 = Bill.create(classify1, BillType.EXPEND, LocalDateTime.now(), 150.0f,"购物测试1")
        val bill2 = Bill.create(classify2, BillType.EXPEND, LocalDateTime.now(), 150.0f,"购物测试1")
        val bill3 = Bill.create(classify3, BillType.EXPEND, LocalDateTime.now(), 150.0f,"购物测试1")

        val dailyBill1 = DailyBill(LocalDate.now(),listOf(bill1,bill2), 125.0f, 160.0f)
        val dailyBill2 = DailyBill(LocalDate.now(),listOf(bill1,bill2), 454.0f, 45.0f)

        val monthlyBill1 = MonthlyBill(YearMonth.now(), 25.0f, 123.0f, listOf(dailyBill1,dailyBill2))

        fun getTestDataClassify(): Classify {
            return classify1;
        }

        fun getTestDataBill(): Bill {
            return bill1;
        }

        fun getTestDataDailyBill(): DailyBill {
            return dailyBill1;
        }

        fun getTestDataMonthlyBill(): MonthlyBill {
            return monthlyBill1;
        }
    }
}