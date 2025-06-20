package com.leng.jingzhekt.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.leng.jingzhekt.R

@Preview
@Composable
fun BottomNavBar() {
        BottomAppBar {
            NavigationBarItem(
                selected = true, onClick = { },
                icon = { Icon(Icons.Filled.Home, contentDescription = "首页") },
                label = { Text("首页") }
            )
            NavigationBarItem(
                selected = false, onClick = { },
                icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "账单") },
                label = { Text("账单") }
            )

            NavigationBarItem(
                selected = false, onClick = { },
                icon = { },
                label = { }
            )

            NavigationBarItem(
                selected = false, onClick = { },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.chart),
                        contentDescription = "统计"
                    )
                },
                label = { Text("统计") }
            )
            NavigationBarItem(
                selected = false, onClick = { },
                icon = { Icon(Icons.Filled.Person, contentDescription = "我的") },
                label = { Text("我的") }
            )
        }

}