package com.leng.jingzhekt.ui.components;

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.leng.jingzhekt.Entity.MonthlyBill
import com.leng.jingzhekt.TestData
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarCard(
    modifier: Modifier = Modifier,
    currentMonth: YearMonth = YearMonth.now(),
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    monthlyBill: MonthlyBill
) {
    var dates = remember(currentMonth) {
        generateCalendarDates(currentMonth)
    }

    val weekDays = listOf("一", "二", "三", "四", "五", "六", "日")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        //colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Days of week header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weekDays.forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // 将42个日期分成6行，每行7列
                dates.chunked(7).forEach { rowDates ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        rowDates.forEach { date ->

                            var amount: Float? = null
                            monthlyBill.dailyBillList.forEach { dailyBill ->
                                if (dailyBill.date == date) {
                                    amount = dailyBill.dailyAmount
                                }
                            }
                            DayCell(
                                modifier = Modifier.weight(1f),
                                date = date,
                                text = if (amount != null)
                                    "-$amount" else "",
                                isCurrentMonth = date.month == currentMonth.month,
                                isSelected = date == selectedDate,
                                onClick = {
                                    onDateSelected(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun generateCalendarDates(yearMonth: YearMonth): List<LocalDate> {
    val firstDayOfMonth = yearMonth.atDay(1)
    val dayOfWeekOfFirst = firstDayOfMonth.dayOfWeek.value
    val firstVisibleDate = firstDayOfMonth.minusDays((dayOfWeekOfFirst - 1).toLong())

    val dates = mutableListOf<LocalDate>()
    var currentDate = firstVisibleDate
    for (i in 0 until 42) {
        dates.add(currentDate)
        currentDate = currentDate.plusDays(1)
    }
    return dates
}

@Composable
private fun DayCell(
    modifier: Modifier,
    date: LocalDate,
    text: String,
    isCurrentMonth: Boolean,
    isSelected: Boolean,
    onClick: (LocalDate) -> Unit
) {
    val monthNames = mapOf(
        1 to "一", 2 to "二", 3 to "三", 4 to "四", 5 to "五", 6 to "六",
        7 to "七", 8 to "八", 9 to "九", 10 to "十", 11 to "十一", 12 to "十二"
    )

    val displayText = if (date.dayOfMonth == 1) {
        monthNames[date.monthValue] + "月"
    } else {
        date.dayOfMonth.toString()
    }

    val textColor = when {
        isSelected -> Color.Black
        isCurrentMonth -> Color.Gray
        else -> Color.Transparent
    }

    val backgroundColor = when {
        isSelected -> Color(0xffaed8f6)
        text.isNotEmpty() -> Color(0xffe9f2ff)
        isCurrentMonth -> Color(0xfff5f5f5)
        else -> Color.Transparent
    }

    Box (
        modifier = modifier
            //.height(50.dp)
            .heightIn(48.dp)
            .clip(RoundedCornerShape(12.dp))  // 调整圆角大小
            .background(backgroundColor)
            .clickable {
                onClick(date)
                Log.d("lengzq", " selected Date $date")
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center  // 改回整体居中
        ) {
            // 月份标题
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    lineHeight = with(LocalDensity.current) { 14.dp.toSp() }
                ),
                text = displayText,
                color = if(isCurrentMonth) Color.Black else Color.Gray,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = with(LocalDensity.current) { 14.dp.toSp() }
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    lineHeight = with(LocalDensity.current) { 10.dp.toSp() }
                ),
                text = text,
                color = textColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = with(LocalDensity.current) { 10.dp.toSp() }  // 适当增大字体
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarCardPreview() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    CalendarCard(
        currentMonth = YearMonth.of(2025, selectedDate.month),
        selectedDate = selectedDate,
        onDateSelected = { date ->
            selectedDate = date
        },
        monthlyBill = TestData.getTestDataMonthlyBill()
    )
}

@Composable
private fun MonthCell(
    month: Int,
    monthName: String,
    isSelected: Boolean,
    onClick: (Int) -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF81D4FA) else Color.Transparent
    val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
    
    Box(
        modifier = Modifier
            .aspectRatio(2f)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick(month) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = monthName,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DatePickerDialog(){
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("按月查看", "按年查看")
    Column {
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex
        ){
            tabs.forEachIndexed{index, string ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = {
                        Text(string)
                    }
                )
            }
        }
        Row {
            when(selectedTabIndex){
                0 -> MonthPickerView()
                1 -> YearPickerView()
            }
        }
    }


}

@Composable
fun YearPickerView(
    modifier: Modifier = Modifier,
    currentYear: Int = 2025,
    selectedYear: Int = 6,
    onYearChanged: (Int) -> Unit = {},
    onMonthSelected: (Int) -> Unit = {}
){
    val yearList = listOf(2018,2019,2020,2021,2022,2023,2024,2025,2026,2027,2028,2029)

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row {
            // 年份选择行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "<",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onYearChanged(currentYear - 1) }
                )

                Text(
                    text = "年份",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = ">",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onYearChanged(currentYear + 1) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            // 月份网格
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(12) { index ->
                    val month = index + 1
                    MonthCell(
                        month = yearList[index],
                        monthName = yearList[index].toString(),
                        isSelected = month == selectedYear,
                        onClick = { onMonthSelected(month) }
                    )
                }
            }
        }
    }
}


@Composable
fun MonthPickerView(
    modifier: Modifier = Modifier,
    currentYear: Int = 2025,
    selectedMonth: Int = 6,
    onYearChanged: (Int) -> Unit = {},
    onMonthSelected: (Int) -> Unit = {}
){
    val monthNames = mapOf(
        1 to "1月", 2 to "2月", 3 to "3月", 4 to "4月", 5 to "5月", 6 to "6月",
        7 to "7月", 8 to "8月", 9 to "9月", 10 to "10月", 11 to "11月", 12 to "12月"
    )
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // 年份选择行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "<",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onYearChanged(currentYear - 1) }
            )

            Text(
                text = currentYear.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = ">",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onYearChanged(currentYear + 1) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 月份网格
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(12) { index ->
                val month = index + 1
                MonthCell(
                    month = month,
                    monthName = monthNames[month] ?: "",
                    isSelected = month == selectedMonth,
                    onClick = { onMonthSelected(month) }
                )
            }
        }
    }
}


@Composable
fun MonthPicker(
    modifier: Modifier = Modifier,
    currentYear: Int = 2025,
    selectedMonth: Int = 6,
    onYearChanged: (Int) -> Unit = {},
    onMonthSelected: (Int) -> Unit = {}
) {
    val monthNames = mapOf(
        1 to "1月", 2 to "2月", 3 to "3月", 4 to "4月", 5 to "5月", 6 to "6月",
        7 to "7月", 8 to "8月", 9 to "9月", 10 to "10月", 11 to "11月", 12 to "12月"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp, 20.dp),
        //colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 年份选择行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "<",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onYearChanged(currentYear - 1) }
                )

                Text(
                    text = currentYear.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = ">",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onYearChanged(currentYear + 1) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 月份网格
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(12) { index ->
                    val month = index + 1
                    MonthCell(
                        month = month,
                        monthName = monthNames[month] ?: "",
                        isSelected = month == selectedMonth,
                        onClick = { onMonthSelected(month) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthPickerPreview() {
    var selectedMonth by remember { mutableIntStateOf(6) }
    var currentYear by remember { mutableIntStateOf(2025) }
    
    MonthPicker(
        currentYear = currentYear,
        selectedMonth = selectedMonth,
        onYearChanged = { currentYear = it },
        onMonthSelected = { selectedMonth = it }
    )
}