package com.leng.jingzhekt.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leng.jingzhekt.R
import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.BillType
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.DailyBill
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.Entity.MonthlyBill
import com.leng.jingzhekt.ui.navigation.AppTopBar
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { AppTopBar() },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // 本月支出卡片
            MonthlyBillCardPreview()
            Spacer(modifier = Modifier.height(16.dp))

            // 今日账单
            MonthlyBillCardPreview()
            Spacer(modifier = Modifier.height(16.dp))

            // 净资产卡片
            //NetWorthCard()
        }
    }
}

@Preview
@Composable
fun MonthlyBillCardPreview(){

    val classify1 = Classify.create("餐饮", R.drawable.add , Level.Major)
    val classify2 = Classify.create("零食", R.drawable.add , Level.Major)
    val classify3 = Classify.create("日用", R.drawable.add , Level.Major)

    val bill1 = Bill.create(classify1,BillType.EXPEND, LocalDateTime.now(),20.0f,"购物测试1")
    val bill2 = Bill.create(classify2,BillType.EXPEND, LocalDateTime.now(),18.0f,"零食测试1")
    val bill3 = Bill.create(classify3,BillType.EXPEND, LocalDateTime.now(),19.0f,"日用测试1")

    val dailyBill1 = DailyBill(LocalDate.now(), listOf(bill1,bill2), 20.9f, 21.0f)
    val dailyBill2 = DailyBill(LocalDate.now(), listOf(bill1,bill2), 20.9f, 21.0f)

    val monthlyBill = MonthlyBill(YearMonth.now(),20.02f, 100.01f, listOf(dailyBill1,dailyBill2) )
    MonthlyBillCard(monthlyBill)
}

@Composable
fun MonthlyBillCard(monthlyBill: MonthlyBill){
    val formatter = DateTimeFormatter.ofPattern("MM月")
    val month = monthlyBill.month.format(formatter)
    val monthString = month + "1日-" + month + monthlyBill.month.lengthOfMonth() + "日"

    val decimalFormat = DecimalFormat("#.##")
    val dailySpending =decimalFormat.format(monthlyBill.expend/monthlyBill.month.lengthOfMonth())
    // 本月支出卡片
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(128.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFB2B7F5), Color(0xFFB2E0F5))
                    )
                )
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("本月支出", style = MaterialTheme.typography.bodyMedium)
                    Card(
                        shape = RoundedCornerShape(50),
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(monthString, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                Text("￥ ${monthlyBill.expend}", style = MaterialTheme.typography.headlineLarge)
            }

            Row (modifier = Modifier.align(Alignment.BottomStart)){
                Text("本月收入 ￥ ${monthlyBill.income}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.width(16.dp))
                Text("日均支出 ￥ $dailySpending", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@Preview
@Composable
fun DailyBillCardPreview() {
    val classify1 = Classify.create("餐饮", R.drawable.add , Level.Major)
    val classify2 = Classify.create("零食", R.drawable.add , Level.Major)
    val classify3 = Classify.create("日用", R.drawable.add , Level.Major)

    val bill1 = Bill.create(classify1,BillType.EXPEND, LocalDateTime.now(),20.0f,"购物测试1")
    val bill2 = Bill.create(classify2,BillType.EXPEND, LocalDateTime.now(),18.0f,"零食测试1")
    val bill3 = Bill.create(classify3,BillType.EXPEND, LocalDateTime.now(),19.0f,"日用测试1")

    DailyBillCard(listOf(bill1,bill2,bill3))
}

@Composable
fun DailyBillCard(dataList: List<Bill>){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(220.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("今日账单", style = MaterialTheme.typography.bodyMedium)
                Row {
                    Text("收入 0.00", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("支出 0.00", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (dataList.isEmpty()) {
                        // 占位图和提示
                        Spacer(modifier = Modifier.height(32.dp))
                        Text("当日没有账单数据", color = Color.LightGray)
                    }

                    dataList.forEachIndexed { indext, item ->
                        Row {
                            Icon(
                                painterResource(item.classify.iconResId),
                                contentDescription = "统计",
                                modifier = Modifier.size(22.dp)
                            )
                            Text(item.classify.name)
                        }

                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun NetWorthCard() {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(80.dp)
            .width(180.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("净资产", style = MaterialTheme.typography.bodyMedium)
            Text("￥ 0.00", style = MaterialTheme.typography.headlineSmall)
        }
    }
}