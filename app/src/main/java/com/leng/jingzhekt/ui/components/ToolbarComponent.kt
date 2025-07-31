package com.leng.jingzhekt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@Composable
fun TabToolBar(
    tabs: List<String>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier
            .clip(RoundedCornerShape(50)),
        containerColor = Color(0xFFE3F2FD),
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedIndex])
                    .fillMaxSize()
                    .clip(RoundedCornerShape(50)),
                color = Color(0xFF81D4FA)
            )
        },
        divider = {},
        tabs = {
            tabs.forEachIndexed { index, text ->
                Tab(
                    modifier = Modifier
                        .zIndex(2f)
                        .height(24.dp),
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    text = {
                        Text(
                            text,
                            color = if (selectedIndex == index) Color.Black else Color.Gray,
                            fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
    )
}

@Preview
@Composable
fun TabToolBarPreview(){
    val tabs = listOf("流水","日历")
    TabToolBar(
        tabs,
        modifier = Modifier.width(120.dp)
    )
}
@Preview
@Composable
fun DateMonthPickerToolBarPreview(){
    val yearMonth = YearMonth.now()
    DateMonthPickerToolBar(
        yearMonth = yearMonth,
        onLeftClick = {

        },
        onDateClick = {

        },
        onRightClick = {

        },
    )
}

@Composable
fun DateMonthPickerToolBar(
    yearMonth : YearMonth,
    onLeftClick: () -> Unit,
    onDateClick: () -> Unit,
    onRightClick: () -> Unit
){
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFE3F2FD))
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White)
                .clickable { /* Previous month */ }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous month",
                tint = Color.Gray
            )
        }
        Text(
            text =  yearMonth.format(DateTimeFormatter.ofPattern("yyyy年M月", Locale.CHINA)),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White)
                .clickable{
                    onRightClick
                }
        ) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next month",
                tint = Color.Gray
            )
        }
    }
}
