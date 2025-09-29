package com.leng.jingzhekt.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.BillType
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.presentation.ui.theme.AppTheme
import com.leng.jingzhekt.presentation.viewmodel.HomeUiState
import com.leng.jingzhekt.presentation.viewmodel.HomeViewModel
import com.leng.jingzhekt.ui.components.CircularIcon
import com.leng.jingzhekt.ui.components.MiniCard
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
fun MonthlyBillCard(bills: List<Bill>) {
    val currentMonth = LocalDate.now().month
    val currentYear = LocalDate.now().year

    val monthlyBills = bills.filter {
        it.time.year == currentYear && it.time.month == currentMonth
    }



    val totalExpend =
        monthlyBills.filter { it.type.name == "EXPEND" }.sumOf { it.amount.toDouble() }
    val totalIncome =
        monthlyBills.filter { it.type.name == "INCOME" }.sumOf { it.amount.toDouble() }

    val isLeapYear = LocalDate.now().isLeapYear

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(128.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFB2B7F5),
                            Color(0xFFB2E0F5)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "本月支出",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "${currentMonth.value}月1日-${currentMonth.length(isLeapYear)}日",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Text(
                    text = "￥${DecimalFormat("#.##").format(totalExpend)}",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "日均支出 ￥${
                            DecimalFormat("#.##").format(
                                totalExpend / currentMonth.length(
                                    false
                                )
                            )
                        }",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "本月收入 ￥${DecimalFormat("#.##").format(totalIncome)}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TodayBillCard(
    bills: List<Bill>,
    classifies: List<Classify>? = null,
    viewModel: HomeViewModel? = null
) {
    val today = LocalDateTime.now().toLocalDate()
    val todayBills = bills.filter { it.time.toLocalDate() == today }

    val todayExpend = todayBills.filter { it.type.name == "EXPEND" }.sumOf { it.amount.toDouble() }
    val todayIncome = todayBills.filter { it.type.name == "INCOME" }.sumOf { it.amount.toDouble() }

    // 如果提供了 classifies 参数（预览模式），直接使用；否则查询数据库
    val classifiesList = if (classifies != null) {
        classifies
    } else {
        // 生产模式：查询数据库
        val vm = viewModel ?: hiltViewModel<HomeViewModel>()
        vm.classifyRepository.getAllClassifies()
            .collectAsStateWithLifecycle(initialValue = emptyList()).value
    }

    // 辅助函数：根据 classifyId 查找对应的 Classify
    fun findClassifyById(classifyId: Int): Classify? {
        return classifiesList.find { it.id == classifyId }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(360.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "今日账单",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(
                            text = "支出 ${DecimalFormat("#.00").format(todayExpend)}",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            //color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    Row() {
                        Text(
                            text = "收入 ${DecimalFormat("#.00").format(todayIncome)}",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            //color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (bills == null || bills.isEmpty()) {
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
                            bills.forEachIndexed { index, item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val classify = findClassifyById(item.classifyId)
                                    CircularIcon(
                                        painterResource(
                                            classify?.iconResId
                                                ?: com.leng.jingzhekt.R.drawable.pending_24px
                                        ),
                                        backGroundColor = Color(0xffe9f2ff),
                                        iconSize = 24.dp,
                                        size = 36.dp
                                    )

                                    Column(modifier = Modifier.padding(start = 16.dp)) {
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            Text(text = item.remarks)
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

@Composable
fun StatisticsCards(bills: List<Bill>) {
    val currentMonth = LocalDateTime.now().month
    val currentYear = LocalDateTime.now().year

    val monthlyBills = bills.filter {
        it.time.year == currentYear && it.time.month == currentMonth
    }

    val totalExpend =
        monthlyBills.filter { it.type.name == "EXPEND" }.sumOf { it.amount.toDouble() }
    val totalIncome =
        monthlyBills.filter { it.type.name == "INCOME" }.sumOf { it.amount.toDouble() }
    val netWorth = totalIncome - totalExpend

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MiniCard(
            title = "本月支出",
            data = totalExpend.toFloat(),
            icon = com.leng.jingzhekt.R.drawable.chart
        )

        MiniCard(
            title = "净资产",
            data = netWorth.toFloat(),
            icon = com.leng.jingzhekt.R.drawable.chart
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        // 创建模拟的 UI 状态用于预览
        val mockUiState = HomeUiState(
            isLoading = false,
            bills = listOf(
                Bill(
                    id = 1,
                    classifyId = 1,
                    amount = 100.0f,
                    type = BillType.EXPEND,
                    time = LocalDateTime.now(),
                    remarks = "午餐"
                ),
                Bill(
                    id = 2,
                    classifyId = 2,
                    amount = 200.0f,
                    type = BillType.INCOME,
                    time = LocalDateTime.now(),
                    remarks = "工资"
                )
            ),
            message = null
        )

        // 创建模拟的分类数据
        val mockClassifies = listOf(
            Classify(
                id = 1,
                name = "餐饮",
                iconResId = com.leng.jingzhekt.R.drawable.fork_spoon_24px,
                level = Level.Major
            ),
            Classify(
                id = 2,
                name = "工资",
                iconResId = com.leng.jingzhekt.R.drawable.chart,
                level = Level.Major
            )
        )

        // 使用原有的组件，传入模拟的分类数据
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F7F7))
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // 本月支出卡片
                MonthlyBillCard(bills = mockUiState.bills)
                Spacer(modifier = Modifier.height(16.dp))

                // 今日账单 - 传入模拟分类数据，避免数据库查询
                TodayBillCard(bills = mockUiState.bills, classifies = mockClassifies)
                Spacer(modifier = Modifier.height(16.dp))

                // 统计卡片
                //StatisticsCards(bills = mockUiState.bills)
            }
        }
    }
}


@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFf4f4f4))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // 加载状态
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // 本月支出卡片
                MonthlyBillCard(bills = uiState.bills)
                Spacer(modifier = Modifier.height(16.dp))

                // 今日账单
                TodayBillCard(bills = uiState.bills)
                Spacer(modifier = Modifier.height(16.dp))

                // 统计卡片
                //StatisticsCards(bills = uiState.bills)
            }

            // 消息提示
            uiState.message?.let { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
