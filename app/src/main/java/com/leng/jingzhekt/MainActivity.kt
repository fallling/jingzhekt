package com.leng.jingzhekt

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
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
import com.leng.jingzhekt.ui.view.BillScreenView
import com.leng.jingzhekt.ui.view.HomeScreen
import com.leng.jingzhekt.ui.view.MineScreen
import com.leng.jingzhekt.ui.view.StatisticsScreen
import java.time.YearMonth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                //HomeScreen()
                HomeNavigationBar()
            }
        }
    }
}

/*@Preview(device = "id:pixel_8_pro")
@Composable
fun MainTopBar(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        // 搜索按钮（靠右）
        IconButton(
            onClick = { *//* 搜索 *//* },
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
                onClick = { *//* 刷新 *//* },
                modifier = Modifier.padding()
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "刷新", modifier = Modifier.size(20.dp))
            }
        }
    }
}*/

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
                    Destination.HOME -> HomeScreen(modifier)
                    Destination.BILL -> BillScreenView(modifier)
                    Destination.ADD -> {}
                    Destination.STATISTIC -> StatisticsScreen(modifier)
                    Destination.Mine -> MineScreen(modifier)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeNavigationBar(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val navController = rememberNavController()
    val startDestination = Destination.HOME
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Scaffold 只负责内容和底部栏
        Scaffold(
            modifier = modifier,
            topBar = {
                AppTopBar()
            },
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
                onClick = {
                    val intent = Intent()
                    intent.setClass(context,AddBillActivity::class.java)
                    context.startActivity(intent)
                },
                shape = CircleShape,
                containerColor = Color.Black,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .height(78.dp)
                    .width(78.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加")
            }
        }
    }
}