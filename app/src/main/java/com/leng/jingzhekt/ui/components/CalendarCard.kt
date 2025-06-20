package com.leng.jingzhekt.ui.components;

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarCard(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth = YearMonth.now(),
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val dates = remember(yearMonth) {
        generateCalendarDates(yearMonth)
    }

    val weekDays = listOf("一", "二", "三", "四", "五", "六", "日")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(dates) { date ->
                    DayCell(
                        date = date,
                        isCurrentMonth = date.month == yearMonth.month,
                        isSelected = date == selectedDate,
                        onClick = { onDateSelected(it) }
                    )
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
    // A calendar grid is usually 6 weeks tall to accommodate all possible month layouts
    for (i in 0 until 42) {
        dates.add(currentDate)
        currentDate = currentDate.plusDays(1)
    }
    return dates
}

@Composable
private fun DayCell(
    date: LocalDate,
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
        isSelected -> Color.White
        isCurrentMonth -> Color.Black
        else -> Color.Gray.copy(alpha = 0.5f)
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) Color(0xFF81D4FA) else Color.Transparent)
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarCardPreview() {
    var selectedDate by remember { mutableStateOf(LocalDate.of(2025, 6, 23)) }
    CalendarCard(
        yearMonth = YearMonth.of(2025, 6),
        selectedDate = selectedDate,
        onDateSelected = { selectedDate = it }
    )
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
            .padding(),
        shape = RoundedCornerShape(20.dp, 20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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