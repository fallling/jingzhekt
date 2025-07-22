package com.leng.jingzhekt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leng.jingzhekt.ui.components.BillToolbar
import com.leng.jingzhekt.ui.components.CircularStatisticalCard
import com.leng.jingzhekt.ui.components.DateMonthPickerToolBar
import com.leng.jingzhekt.ui.components.DetailFlowCard
import com.leng.jingzhekt.ui.components.LineChartCard
import com.leng.jingzhekt.ui.components.MiniCard
import com.leng.jingzhekt.ui.components.NoBillsPlaceholder
import com.leng.jingzhekt.ui.components.TabToolBar
import com.leng.jingzhekt.ui.navigation.AppTopBar
import com.leng.jingzhekt.ui.theme.AppTheme
import java.time.YearMonth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                //HomeScreen()
                BillScreen()
            }
        }
    }
}

@Preview
@Composable
fun MonthlyExpenditure(){
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
}

@Preview
@Composable
fun TodayBill(){
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

@Preview(device = "id:pixel_8_pro")
@Composable
fun MainTopBar(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        // 搜索按钮（靠右）
        IconButton(
            onClick = { /* 搜索 */ },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                Icons.Filled.Search,
                contentDescription = "搜索",
                modifier = Modifier.size(24.dp)
            )
        }

        // 标题和刷新按钮组（居中）
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "默认账本",
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(
                onClick = { /* 刷新 */ },
                modifier = Modifier.padding()
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "刷新", modifier = Modifier.size(20.dp))
            }
        }
    }
}

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
        ) {
            // 本月支出卡片
            MonthlyExpenditure()
            Spacer(modifier = Modifier.height(16.dp))

            // 今日账单
            TodayBill()
            Spacer(modifier = Modifier.height(16.dp))

            // 净资产卡片
            NetWorthCard()
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillScreen(){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Box(modifier = Modifier) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                AppTopBar()
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F7F7))
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BillToolbar()
                DetailFlowCard()
                NoBillsPlaceholder()
            }
        }
    }
}

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

                    TabToolBar(tabs = listOf("支出","收入","结余"),Modifier.width(180.dp))
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

@Preview
@Composable
fun MineScreen(){

}

enum class Destination(
    val route: String,
    val label: String,
    val icon: Any, // 支持 ImageVector 或 Int
    val contentDescription: String
) {
    HOME("home", "首页", Icons.Default.Home, "首页"),
    BILL("bill", "账单", Icons.AutoMirrored.Filled.List, "账单"),
    ADD("add", "", "", ""),
    STATISTIC("statistic", "统计", R.drawable.chart, "统计"), // 用自定义图标
    Mine("mine", "我的", Icons.Default.Person, "我的")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.HOME -> HomeScreen()
                    Destination.BILL -> BillScreen()
                    Destination.ADD -> {}
                    Destination.STATISTIC -> StatisticsScreen()
                    Destination.Mine -> MineScreen()
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeNavigationBar(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.HOME
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Scaffold 只负责内容和底部栏
        Scaffold(
            modifier = modifier,
            bottomBar = {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    Destination.entries.forEachIndexed { index, destination ->
                        val isAdd = destination == Destination.ADD
                        NavigationBarItem(
                            selected = !isAdd && selectedDestination == index,
                            onClick = {
                                if (!isAdd) {
                                    navController.navigate(route = destination.route)
                                    selectedDestination = index
                                }
                            },
                            enabled = !isAdd, // 禁用ADD按钮
                            icon = {
                                when (destination.icon) {
                                    is ImageVector -> Icon(destination.icon as ImageVector, contentDescription = destination.contentDescription)
                                    is Int -> Icon(painterResource(id = destination.icon as Int), contentDescription = destination.contentDescription)
                                    else -> {}
                                }
                            },
                            label = { if (destination.label.isNotEmpty()) Text(destination.label) }
                        )
                    }
                }
            }
        ) { contentPadding ->
            AppNavHost(
                navController,
                startDestination,
                modifier = Modifier.padding(contentPadding)
            )
        }

        // FAB 绝对定位在底部中间，覆盖在BottomBar上
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            FloatingActionButton(
                onClick = { /* TODO: 这里写你的点击事件 */ },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(bottom = 28.dp) // 这里的bottom值可根据BottomBar高度微调
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加")
            }
        }
    }
}