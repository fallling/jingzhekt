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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.sp
import com.leng.jingzhekt.Entity.DailyBill
import com.leng.jingzhekt.Entity.MonthlyBill
import com.leng.jingzhekt.TestData
import com.leng.jingzhekt.ui.components.CircularIcon
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
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
            DailyBillCardPreview()
            Spacer(modifier = Modifier.height(16.dp))

            // 净资产卡片
            //NetWorthCard()
        }
    }
}

@Preview
@Composable
fun MonthlyBillCardPreview() {
    MonthlyBillCard(TestData.getTestDataMonthlyBill())
}

@Composable
fun MonthlyBillCard(monthlyBill: MonthlyBill) {
    val formatter = DateTimeFormatter.ofPattern("MM月")
    val month = monthlyBill.month.format(formatter)
    val monthString = month + "1日-" + month + monthlyBill.month.lengthOfMonth() + "日"

    val decimalFormat = DecimalFormat("#.##")
    val dailySpending = decimalFormat.format(monthlyBill.expend / monthlyBill.month.lengthOfMonth())
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

            Row(modifier = Modifier.align(Alignment.BottomStart)) {
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
    DailyBillCard(TestData.getTestDataDailyBill())
}

@Composable
fun DailyBillCard(
    dailyBill: DailyBill? = null

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(360.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("今日账单")
                Row {
                    Text(
                        "收 ${dailyBill?.income ?: "0.00"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "支 ${dailyBill?.expand ?: "0.00"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            //Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (dailyBill == null || dailyBill.billList.isEmpty()) {
                        // 占位图和提示
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "No data",
                            modifier = Modifier.size(72.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Text("当日没有账单数据", color = Color.LightGray)

                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {
                            dailyBill.billList.forEachIndexed { index, item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularIcon(
                                        painterResource(item.classify.iconResId),
                                        backGroundColor = Color(0xffe9f2ff),
                                        iconSize = 24.dp,
                                        size = 36.dp
                                    )

                                    Column(modifier = Modifier.padding(start = 16.dp)) {
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            Text(text = item.classify.name)
                                            Row(modifier = Modifier.align(Alignment.CenterEnd)) {

                                            }
                                            Text(
                                                text = "￥" + item.amount,
                                                modifier = Modifier.align(Alignment.CenterEnd)
                                            )
                                        }
                                        Text(
                                            item.remarks,
                                            modifier = Modifier.padding(start = 2.dp),
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
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