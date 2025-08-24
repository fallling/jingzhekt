package com.leng.jingzhekt.ui.view

import android.util.Log
import androidx.appcompat.widget.Toolbar
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.DailyBill
import com.leng.jingzhekt.Entity.MonthlyBill
import com.leng.jingzhekt.TestData
import com.leng.jingzhekt.ui.components.CalendarCard
import com.leng.jingzhekt.ui.components.CircularIcon
import com.leng.jingzhekt.ui.components.DateMonthPickerToolBar
import com.leng.jingzhekt.ui.components.DetailFlowCard
import com.leng.jingzhekt.ui.components.MonthPicker
import com.leng.jingzhekt.ui.components.NoBillsPlaceholder
import java.time.LocalDate
import java.time.YearMonth
import kotlin.reflect.KClass

@Preview
@Composable
fun BillScreenView(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = BillScreenDestination.CALENDAR
    var tabSelectedIndex by remember { mutableIntStateOf(startDestination.ordinal) }

    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
    
    Scaffold(
        modifier = modifier,
        topBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                TabRow(
                    selectedTabIndex = tabSelectedIndex,
                    modifier = Modifier.width(120.dp)
                        .clip(RoundedCornerShape(50)),
                    containerColor = Color(0xFFE3F2FD),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier
                                .tabIndicatorOffset(tabPositions[tabSelectedIndex])
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
                                selected = tabSelectedIndex == index,
                                onClick = {
                                    navController.navigate(route = destination.route)
                                    tabSelectedIndex = index
                                },
                                text = {
                                    Text(
                                        destination.label,
                                        color = if (tabSelectedIndex == index) Color.Black else Color.Gray,
                                        fontWeight = if (tabSelectedIndex == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                )
                TabToolBar(onTabClick = { index ->
                    navController.navigate(route = destination.route)
                    tabSelectedIndex = index
                })
                DateMonthPickerToolBar(
                    yearMonth = selectedMonth,
                    onLeftClick = {
                        selectedMonth = selectedMonth.minusMonths(1)
                    },
                    onDateClick = {

                    },
                    onRightClick = {
                        selectedMonth = selectedMonth.plusMonths(1)
                    },
                )
            }
        }
    ) { innerPadding ->
        BillNavHost(navController, startDestination, Modifier.padding(innerPadding), selectedMonth)
    }
}

@Composable
fun TabToolBar(onTabClick: (index:Int, ) -> Unit){
    var tabSelectedIndex by remember { mutableIntStateOf(BillScreenDestination.CALENDAR.ordinal) }
    TabRow(
        selectedTabIndex = tabSelectedIndex,
        modifier = Modifier.width(120.dp)
            .clip(RoundedCornerShape(50)),
        containerColor = Color(0xFFE3F2FD),
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[tabSelectedIndex])
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
                    selected = tabSelectedIndex == index,
                    onClick = {
                        navController.navigate(route = destination.route)
                        tabSelectedIndex = index
                        onTabClick(index, destination)
                    },
                    text = {
                        Text(
                            destination.label,
                            color = if (tabSelectedIndex == index) Color.Black else Color.Gray,
                            fontWeight = if (tabSelectedIndex == index) FontWeight.Bold else FontWeight.Normal
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
    selectedMonth: YearMonth
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        BillScreenDestination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    BillScreenDestination.STATEMENT -> StatementView(modifier)
                    BillScreenDestination.CALENDAR -> CalendarViewDetail(modifier, selectedMonth)
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
fun StatementView(modifier: Modifier) {
    Column(modifier) {
        DetailFlowCard()
        BillListCardPreview()
    }
}

@Composable
fun CalendarViewDetail(
    modifier: Modifier = Modifier,
    selectedMonth: YearMonth) {

    Column(modifier) {
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }

        selectedDate = if(selectedDate.dayOfMonth !in 1..selectedMonth.lengthOfMonth()){
            selectedMonth.atDay(selectedMonth.lengthOfMonth())
        }else{
            selectedMonth.atDay(selectedDate.dayOfMonth)
        }

        var currentMonth by remember { mutableStateOf(selectedMonth) }

        CalendarCard(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            onDateSelected = { date ->
                selectedDate = date
                currentMonth = YearMonth.from(date)
            },
            monthlyBill = TestData.getTestDataMonthlyBill()
        )
        
        DailyBillDetailCard(TestData.getDailyBillByDate(selectedDate))
    }
}

@Composable
fun BillListCard(
    monthlyBill: MonthlyBill ?=null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            ,
        shape = RoundedCornerShape(20.dp)
    ) {

        val state = rememberScrollState()

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {

            if(monthlyBill == null || monthlyBill.dailyBillList.isEmpty()){
                NoBillsPlaceholder()
            }else {
                monthlyBill.dailyBillList.forEachIndexed { index, dailyBill ->
                    DailyListItem(dailyBill)
                }
            }
        }
    }
}

@Composable
fun DailyBillDetailCard(dailyBill: DailyBill?){

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DailyListItem(dailyBill)
    }
}

@Composable
fun DailyListItem(
    dailyBill: DailyBill?,
){
    Column (modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${dailyBill?.date}")
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
                if(dailyBill == null){
                    NoBillsPlaceholder()
                }else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        dailyBill.billList.forEachIndexed { index, item ->
                            BillItem(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BillItem(
    bill: Bill
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularIcon(
            painterResource(bill.classify.iconResId),
            backGroundColor = Color(0xffe9f2ff),
            iconSize = 24.dp,
            size = 36.dp
        )

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = bill.classify.name)
                Row(modifier = Modifier.align(Alignment.CenterEnd)) {

                }
                Text(
                    text = "￥" + bill.amount,
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


@Composable
fun DatePickerDialogCard(){
    Card {
        MonthPicker()
    }
}

@Preview
@Composable
fun DatePickerDialogCardPreview(){
    DatePickerDialogCard()
}

@Preview
@Composable
fun BillItemPreview(){
    val bill = TestData.getTestDataBill()
    BillItem(bill)
}

@Preview
@Composable
fun BillListCardPreview(){
    val monthlyBill = TestData.getTestDataMonthlyBill()
    BillListCard(monthlyBill)
}

@Preview
@Composable
fun DailyBillDetailCardPreview(){
    val dailyBill = TestData.getTestDataDailyBill()
    DailyBillDetailCard(dailyBill)
}