package com.leng.jingzhekt.presentation.ui.navigation

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leng.jingzhekt.AddBillActivity
import com.leng.jingzhekt.R
import com.leng.jingzhekt.presentation.ui.screens.HomeScreen
import com.leng.jingzhekt.presentation.ui.screens.BillScreen
import com.leng.jingzhekt.presentation.ui.screens.StatisticsScreen
import com.leng.jingzhekt.presentation.ui.screens.MineScreen
import com.leng.jingzhekt.ui.navigation.AppTopBar

enum class Destination(
    val route: String,
    val label: String,
    val icon: Any, // 支持 ImageVector 或 Int
    val contentDescription: String
) {
    HOME("home", "首页", Icons.Default.Home, "首页"),
    BILL("bill", "账单", Icons.AutoMirrored.Filled.List, "账单"),
    ADD("add", "", "", ""),
    STATISTIC("statistic", "统计", R.drawable.chart, "统计"),
    MINE("mine", "我的", Icons.Default.Person, "我的")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.HOME -> HomeScreen(modifier = Modifier.fillMaxSize())
                    Destination.BILL -> BillScreen(modifier = Modifier.fillMaxSize())
                    Destination.ADD -> { /* 空实现，由FAB处理 */ }
                    Destination.STATISTIC -> StatisticsScreen(modifier = Modifier.fillMaxSize())
                    Destination.MINE -> MineScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainView(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.HOME
    var selectedDestination by remember { mutableIntStateOf(startDestination.ordinal) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Scaffold 只负责内容和底部栏
        Scaffold(
            modifier = modifier,
            topBar = {
                AppTopBar()
            },
            bottomBar = {
                Box(modifier = Modifier
                    .fillMaxWidth().background(Color.Transparent)
                    /* .pointerInput(Unit) {}*/,){
                    NavigationBar(
                        modifier = Modifier.align(alignment = Alignment.BottomCenter),
                        containerColor = MaterialTheme.colorScheme.onTertiary,
                        windowInsets = NavigationBarDefaults.windowInsets) {
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
                                        is ImageVector -> Icon(destination.icon, contentDescription = destination.contentDescription)
                                        is Int -> Icon(painterResource(id = destination.icon), contentDescription = destination.contentDescription)
                                        else -> {}
                                    }
                                },
                                label = { if (destination.label.isNotEmpty()) Text(destination.label) }
                            )
                        }
                    }
                    val context = LocalContext.current
                    Box(modifier =
                        Modifier
                            .align(Alignment.TopCenter)
                            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))){
                        FloatingActionButton(
                            onClick = {
                                val intent = Intent(context, AddBillActivity::class.java)
                                context.startActivity(intent)
                            },
                            shape = androidx.compose.foundation.shape.CircleShape,
                            containerColor = Color.Black,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                focusedElevation = 0.dp,
                                hoveredElevation = 0.dp
                            ),
                            modifier = Modifier
                                .padding(bottom = 30.dp)
                                .height(60.dp)
                                .width(60.dp)
                        ) {
                            Icon(modifier = Modifier.size(42.dp), imageVector = Icons.Default.Add, contentDescription = "添加")
                        }
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
    }
}



@Preview
@Composable
fun BottomBar(){
    val navController = rememberNavController()
    val startDestination = Destination.HOME
    var selectedDestination by remember { mutableIntStateOf(startDestination.ordinal) }

    Box(modifier = Modifier
            .fillMaxWidth().background(Color.Transparent)
           /* .pointerInput(Unit) {}*/,){
        NavigationBar(
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
            windowInsets = NavigationBarDefaults.windowInsets) {
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
                            is ImageVector -> Icon(destination.icon, contentDescription = destination.contentDescription)
                            is Int -> Icon(painterResource(id = destination.icon), contentDescription = destination.contentDescription)
                            else -> {}
                        }
                    },
                    label = { if (destination.label.isNotEmpty()) Text(destination.label) }
                )
            }
        }

        Box(modifier =
            Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(
                    WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))){
            FloatingActionButton(
                onClick = {},
                shape = androidx.compose.foundation.shape.CircleShape,
                containerColor = Color.Black,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    focusedElevation = 0.dp,
                    hoveredElevation = 0.dp
                ),
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .height(60.dp)
                    .width(60.dp)
            ) {
                Icon(modifier = Modifier.size(42.dp), imageVector = Icons.Default.Add, contentDescription = "添加")
            }
        }

    }
}