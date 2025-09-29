package com.leng.jingzhekt.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.BillType
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.DailyBill
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.Entity.MonthlyBill
import com.leng.jingzhekt.R
import com.leng.jingzhekt.presentation.viewmodel.BillViewModel
import com.leng.jingzhekt.ui.components.CalendarCard
import com.leng.jingzhekt.ui.components.CircularIcon
import com.leng.jingzhekt.ui.components.DateMonthPickerToolBar
import com.leng.jingzhekt.ui.components.DetailFlowCard
import com.leng.jingzhekt.ui.components.MonthPicker
import com.leng.jingzhekt.ui.components.NoBillsPlaceholder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

// 创建一个CompositionLocal来检测是否在预览模式
val LocalIsPreview = compositionLocalOf { false }

@Preview
@Composable
fun BillScreenPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalIsPreview provides true) {
            // 创建模拟的分类数据
            val mockClassifies = listOf(
                Classify.create("交通", R.drawable.directions_bus_24px, Level.Major),
                Classify.create("礼物", R.drawable.featured_seasonal_and_gifts_24px, Level.Major),
                Classify.create("餐饮", R.drawable.fork_spoon_24px, Level.Major),
                Classify.create("零食", R.drawable.icecream_24px, Level.Major),
                Classify.create("通讯", R.drawable.perm_phone_msg_24px, Level.Major)
            )
            
            // 创建模拟的月账单数据
            val mockMonthlyBill = createMockMonthlyBill()
            
            // 预览时不使用ViewModel，直接传入模拟数据
            BillScreenContent(
                monthlyBill = mockMonthlyBill,
                classifies = mockClassifies
            )
        }
    }
}

@Preview
@Composable
fun BillItemPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalIsPreview provides true) {
            // 创建模拟的分类数据
            val mockClassifies = listOf(
                Classify.create("交通", R.drawable.directions_bus_24px, Level.Major),
                Classify.create("餐饮", R.drawable.fork_spoon_24px, Level.Major)
            )
            
            // 创建模拟的账单数据
            val mockBill = Bill.create(1, BillType.EXPEND, LocalDateTime.now(), 150.0f, "测试账单")
            
            BillItem(
                bill = mockBill,
                classifies = mockClassifies
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillScreen(
    modifier: Modifier = Modifier,
    viewModel: BillViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // 监听月份变化，加载对应月份的数据
    LaunchedEffect(Unit) {
        viewModel.loadBillsForMonth(YearMonth.now())
    }
    
    BillScreenContent(
        modifier = modifier,
        monthlyBill = uiState.monthlyBill,
        classifies = null, // 生产环境不传入分类数据，让组件自己查询
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillScreenContent(
    modifier: Modifier = Modifier,
    monthlyBill: MonthlyBill? = null,
    classifies: List<Classify>? = null,
    uiState: com.leng.jingzhekt.presentation.viewmodel.BillUiState? = null
) {
    val navController = rememberNavController()
    val startDestination = BillScreenDestination.STATEMENT
    var tabSelectedIndex by remember { mutableIntStateOf(startDestination.ordinal) }
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }

    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTabRow(
                    selectedTabIndex = tabSelectedIndex,
                    onTabSelected = { index ->
                        tabSelectedIndex = index
                        val destination = BillScreenDestination.entries[index]
                        navController.navigate(route = destination.route)
                    }
                )
                
                DateMonthPickerToolBar(
                    yearMonth = selectedMonth,
                    onLeftClick = {
                        selectedMonth = selectedMonth.minusMonths(1)
                    },
                    onDateClick = {
                        showSheet = true
                    },
                    onRightClick = {
                        selectedMonth = selectedMonth.plusMonths(1)
                    },
                )
            }
        }
    ) { innerPadding ->
        BillNavHost(
            navController, 
            startDestination, 
            Modifier.padding(innerPadding), 
            selectedMonth,
            monthlyBill = monthlyBill,
            classifies = classifies,
            uiState = uiState,
            onMonthChanged = { newMonth ->
                selectedMonth = newMonth
            }
        )
        val sheetState = rememberModalBottomSheetState()

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
                dragHandle = null
            ) {
                Card() {
                    MonthPicker()
                }
            }
        }
    }
}

@Composable
fun CustomTabRow(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.width(120.dp)
            .clip(RoundedCornerShape(50)),
        containerColor = Color(0xFFE3F2FD),
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .fillMaxSize()
                    .clip(RoundedCornerShape(50)),
                color = Color(0xFF81D4FA)
            )
        },
        divider = {},
        tabs = {
            BillScreenDestination.entries.forEachIndexed { index, destination ->
                Tab(
                    modifier = Modifier
                        .zIndex(2f)
                        .height(24.dp),
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            destination.label,
                            color = if (selectedTabIndex == index) Color.Black else Color.Gray,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun BillNavHost(
    navController: NavHostController,
    startDestination: BillScreenDestination,
    modifier: Modifier = Modifier,
    selectedMonth: YearMonth,
    monthlyBill: MonthlyBill? = null,
    classifies: List<Classify>? = null,
    uiState: com.leng.jingzhekt.presentation.viewmodel.BillUiState? = null,
    onMonthChanged: (YearMonth) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        BillScreenDestination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    BillScreenDestination.STATEMENT -> StatementView(modifier, monthlyBill, uiState)
                    BillScreenDestination.CALENDAR -> CalendarViewDetail(
                        modifier = modifier, 
                        selectedMonth = selectedMonth,
                        monthlyBill = monthlyBill,
                        classifies = classifies,
                        uiState = uiState,
                        onMonthChanged = onMonthChanged
                    )
                }
            }
        }
    }
}

enum class BillScreenDestination(
    val route: String,
    val label: String,
    val contentDescription: String
) {
    STATEMENT("statement", "流水", "Statement"),
    CALENDAR("Calendar", "日历", "Calendar")
}

@Composable
fun StatementView(
    modifier: Modifier, 
    monthlyBill: MonthlyBill? = null,
    uiState: com.leng.jingzhekt.presentation.viewmodel.BillUiState? = null
) {
    Column(modifier) {
        DetailFlowCard()
        if (uiState?.isLoading == true) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            BillListCard(monthlyBill)
        }
    }
}

@Composable
fun CalendarViewDetail(
    modifier: Modifier = Modifier,
    selectedMonth: YearMonth,
    monthlyBill: MonthlyBill? = null,
    classifies: List<Classify>? = null,
    uiState: com.leng.jingzhekt.presentation.viewmodel.BillUiState? = null,
    onMonthChanged: (YearMonth) -> Unit = {}
) {
    Column(modifier.verticalScroll(rememberScrollState())) {
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        
        // 使用LaunchedEffect来监听selectedMonth的变化
        LaunchedEffect(selectedMonth) {
            // 当月份改变时，确保selectedDate在有效范围内
            val maxDay = selectedMonth.lengthOfMonth()
            if (selectedDate.dayOfMonth > maxDay) {
                selectedDate = selectedMonth.atDay(maxDay)
            } else if (selectedDate.month != selectedMonth.month) {
                selectedDate = selectedMonth.atDay(selectedDate.dayOfMonth.coerceAtMost(maxDay))
            }
        }

        if (uiState?.isLoading == true) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            CalendarCard(
                currentMonth = selectedMonth, // 直接使用selectedMonth，确保同步
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    val newMonth = YearMonth.from(date)
                    if (newMonth != selectedMonth) {
                        onMonthChanged(newMonth)
                    }
                },
                monthlyBill = monthlyBill ?: createMockMonthlyBill()
            )
            
            DailyBillDetailCard(
                getDailyBillByDate(selectedDate, monthlyBill), 
                selectedDate,
                classifies
            )
        }
    }
}

@Composable
fun BillListCard(monthlyBill: MonthlyBill? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            if (monthlyBill == null || monthlyBill.dailyBillList.isEmpty()) {
                NoBillsPlaceholder()
            } else {
                monthlyBill.dailyBillList.forEachIndexed { index, dailyBill ->
                    DailyListItem(dailyBill)
                }
            }
        }
    }
}

@Composable
fun DailyBillDetailCard(
    dailyBill: DailyBill?, 
    selectedDate: LocalDate,
    classifies: List<Classify>? = null
) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        DailyListItem(dailyBill, selectedDate, classifies)
    }
}

@Composable
fun DailyListItem(
    dailyBill: DailyBill?,
    selectedDate: LocalDate = LocalDate.now(),
    classifies: List<Classify>? = null
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("$selectedDate")
            Row {
                Text(
                    "收 ￥${dailyBill?.income ?: "0.00"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "支 ￥${dailyBill?.expand ?: "0.00"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp
        )
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (dailyBill == null) {
                    NoBillsPlaceholder()
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        dailyBill.billList.forEachIndexed { index, item ->
                            BillItem(item, classifies)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BillItem(
    bill: Bill, 
    classifies: List<Classify>? = null
) {
    val isPreview = LocalIsPreview.current
    
    // 如果提供了 classifies 参数（预览模式），直接使用；否则查询数据库
    val classifiesList = if (classifies != null || isPreview) {
        classifies ?: emptyList()
    } else {
        // 生产模式：查询数据库
        val vm = hiltViewModel<BillViewModel>()
        vm.classifyRepository.getAllClassifies()
            .collectAsStateWithLifecycle(initialValue = emptyList()).value
    }
    
    // 通过classifyId查找对应的Classify
    val classify = classifiesList.find { it.id == bill.classifyId }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularIcon(
            painterResource(classify?.iconResId ?: R.drawable.pending_24px),
            backGroundColor = Color(0xFFE9F2FF),
            iconSize = 24.dp,
            size = 36.dp
        )

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = classify?.name ?: "未知分类")
                Text(
                    text = "￥${bill.amount}",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            Text(
                bill.remarks,
                modifier = Modifier.padding(start = 2.dp),
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogCard(onDismissRequest: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Card {
            MonthPicker()
        }
    }
}

// 辅助函数：创建模拟月账单
private fun createMockMonthlyBill(): MonthlyBill {
    val mockBills = listOf(
        Bill.create(1, BillType.EXPEND, LocalDateTime.now().minusDays(3), 151.0f, "公交车"),
        Bill.create(2, BillType.EXPEND, LocalDateTime.now().minusDays(3), 123.0f, "红包"),
        Bill.create(3, BillType.EXPEND, LocalDateTime.now(), 150.0f, "午饭"),
        Bill.create(4, BillType.EXPEND, LocalDateTime.now(), 150.0f, "零食"),
        Bill.create(3, BillType.EXPEND, LocalDateTime.now(), 150.0f, "晚餐"),
        Bill.create(9, BillType.EXPEND, LocalDateTime.now(), 150.0f, "电话费")
    )
    
    val mockDailyBills = listOf(
        DailyBill(listOf(mockBills[0], mockBills[1])),
        DailyBill(listOf(mockBills[2], mockBills[3], mockBills[4], mockBills[5]))
    )
    
    return MonthlyBill(mockDailyBills)
}

// 辅助函数：根据日期获取日账单
private fun getDailyBillByDate(date: LocalDate, monthlyBill: MonthlyBill?): DailyBill? {
    return monthlyBill?.dailyBillList?.find { it.date == date }
}
