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
        val classify_transport = Classify.create("交通", R.drawable.directions_bus_24px , Level.Major)
        val classify_gift = Classify.create("礼物", R.drawable.featured_seasonal_and_gifts_24px , Level.Major)
        val classify_catering = Classify.create("餐饮", R.drawable.fork_spoon_24px , Level.Major)
        val classify_snacks = Classify.create("零食", R.drawable.icecream_24px , Level.Major)
        val classify_clothing = Classify.create("衣服", R.drawable.laundry_24px , Level.Major)
        val classify_beverage = Classify.create("饮料", R.drawable.local_bar_24px , Level.Major)
        val classify_Housing = Classify.create("住房", R.drawable.other_houses_24px , Level.Major)
        val classify_others = Classify.create("其他", R.drawable.pending_24px , Level.Major)
        val classify_communication = Classify.create("通讯", R.drawable.perm_phone_msg_24px , Level.Major)
        val classify_chart = Classify.create("购物", R.drawable.shopping_cart_24px , Level.Major)
        val classify_entertainment = Classify.create("娱乐", R.drawable.theaters_24px , Level.Major)


        val bill1 = Bill.create(classify_transport, BillType.EXPEND, LocalDateTime.now().minusDays(3), 151.0f,"公交车")
        val bill2 = Bill.create(classify_gift, BillType.EXPEND, LocalDateTime.now().minusDays(3), 123.0f,"红包")

        val bill3 = Bill.create(classify_catering, BillType.EXPEND, LocalDateTime.now(), 150.0f,"午饭")
        val bill4 = Bill.create(classify_snacks, BillType.EXPEND, LocalDateTime.now(), 150.0f,"零食")
        val bill5 = Bill.create(classify_catering, BillType.EXPEND, LocalDateTime.now(), 150.0f,"晚餐")
        val bill6 = Bill.create(classify_communication, BillType.EXPEND, LocalDateTime.now(), 150.0f,"电话费")
        val bill7 = Bill.create(classify_communication, BillType.EXPEND, LocalDateTime.now(), 160.0f,"测试2")
        val bill8 = Bill.create(classify_communication, BillType.EXPEND, LocalDateTime.now(), 160.0f,"测试3")

        val dailyBill1 = DailyBill(listOf(bill1, bill2))
        val dailyBill2 = DailyBill(listOf(bill3, bill4, bill5, bill6,bill7,bill8))

        val monthlyBill1 = MonthlyBill(listOf(dailyBill1, dailyBill2))

        fun getTestDataClassify(): Classify {
            return classify_beverage
        }

        fun getTestDataBill(): Bill {
            return bill1
        }

        fun getTestDataDailyBill(): DailyBill {
            return dailyBill1
        }

        fun getTestDataMonthlyBill(): MonthlyBill {
            return monthlyBill1
        }

        fun getDailyBillByDate(localDate: LocalDate):DailyBill? {
            monthlyBill1.dailyBillList.forEach { item ->
                if(item.date.equals(localDate)){
                    return item
                }
            }
            return null
        }
    }
}