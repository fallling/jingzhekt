package com.leng.jingzhekt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leng.jingzhekt.ui.theme.AppTheme
import com.leng.jingzhekt.ui.theme.primaryLight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            AppTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true,
    device = "id:pixel_8_pro"
)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Android")
    }
}

@Preview
@Composable
fun MonthlyExpenditure(){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.size(width = 240.dp, height = 100.dp)
        ) {
            Text("本月支出")
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // 顶部栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("默认账本", style = MaterialTheme.typography.titleLarge)
                Row {
                    IconButton(onClick = { /* 刷新 */ }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "刷新")
                    }
                    IconButton(onClick = { /* 搜索 */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "搜索")
                    }
                }
            }
            // 本月支出卡片
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(120.dp),
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
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("本月支出", style = MaterialTheme.typography.bodyMedium)
                            Card(
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("6月1日-6月30日", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("￥ 0.00", style = MaterialTheme.typography.headlineLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text("本月收入 ￥ 0.00", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("日均支出 ￥ 0.00", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // 今日账单
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
                    // 占位图和提示
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // 这里可以放占位图片
                            Spacer(modifier = Modifier.height(32.dp))
                            Text("当日没有账单数据", color = Color.LightGray)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // 净资产卡片
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(80.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("净资产", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("￥ 0.00", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
        // 底部导航栏
        NavigationBar {
            NavigationBarItem(
                selected = true, onClick = { },
                icon = { Icon(Icons.Filled.Home, contentDescription = "首页") },
                label = { Text("首页") }
            )
            NavigationBarItem(
                selected = false, onClick = { },
                icon = { Icon(Icons.Filled.List, contentDescription = "账单") },
                label = { Text("账单") }
            )
            NavigationBarItem(
                selected = false, onClick = { },
                icon = { Icon(Icons.Filled.Add, contentDescription = "添加") },
                label = { Text("") }
            )
            NavigationBarItem(
                selected = false, onClick = { },
                icon = { Icon(Icons.Filled.Add, contentDescription = "统计") },
                label = { Text("统计") }
            )
            NavigationBarItem(
                selected = false, onClick = { },
                icon = { Icon(Icons.Filled.Add, contentDescription = "我的") },
                label = { Text("我的") }
            )
        }
    }
}