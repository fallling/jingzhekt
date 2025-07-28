package com.leng.jingzhekt.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leng.jingzhekt.ui.components.CircularStatisticalCard
import com.leng.jingzhekt.ui.components.DateMonthPickerToolBar
import com.leng.jingzhekt.ui.components.LineChartCard
import com.leng.jingzhekt.ui.components.MiniCard
import com.leng.jingzhekt.ui.components.TabToolBar
import com.leng.jingzhekt.ui.navigation.AppTopBar
import java.time.YearMonth

@Preview
@Composable
fun StatisticsScreen(){
    val scrollState = rememberScrollState()
    Box(modifier = Modifier) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = { AppTopBar() },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F7F7))
                    .padding(innerPadding)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var selectedIndex by remember { mutableIntStateOf(0) }

                    TabToolBar(tabs = listOf("支出","收入","结余"), Modifier.width(180.dp))
                    DateMonthPickerToolBar(yearMonth = YearMonth.now())
                }

                Row {
                    MiniCard(title = "支出金额", data = 116.07f)
                    MiniCard(title = "日均支出", data = 928.55f)
                }
                Row {
                    MiniCard(title = "本月预算", data = 6300.00f)
                    MiniCard(title = "剩余预算", data = 5371.45f)
                }

                val data = listOf(0f, 180f, 120f, 90f, 80f, 75f)
                val xLabels = listOf("01", "05", "10", "15", "20", "25", "30")

                LineChartCard(
                    originalData = data,
                    title = "支出趋势",
                    date = "2025-05-02",
                    totalAmount = 1106.41f,
                    xLabels = xLabels
                )

                val mdata = listOf(5004.56f, 2809.04f, 496.55f, 200f, 100f)
                val labels = listOf("住房", "餐饮", "购物", "娱乐", "其他")
                val colors = listOf(
                    Color(0xFFB2D7F5), Color(0xFF81D4FA), Color(0xFFB2F5E6), Color(0xFFF5E6B2), Color(0xFFF5B2B2)
                )
                var selectedIndex by remember { mutableStateOf(0) }

                CircularStatisticalCard(
                    modifier = Modifier.fillMaxWidth(),
                    data = mdata,
                    labels = labels,
                    colors = colors,
                    selectedIndex = selectedIndex,
                    onSelect = { selectedIndex = it })
            }
        }
    }
}
