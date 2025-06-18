package com.leng.jingzhekt.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavBar() {
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