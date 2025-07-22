package com.leng.jingzhekt.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun BillClassification() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("支出", "收入", "转账", "借贷")
    val categories = listOf(
        "餐饮", "零食", "日用", "购物", "交通",
        "饮品", "水果", "服饰", "娱乐", "住房",
        "人情", "通讯", "其它", "分类管理"
    )
    val categoryIcons = listOf(
        "🍽️", "🧁", "🧻", "💄", "🚌",
        "🥤", "🍎", "👕", "🎬", "🏠",
        "🎁", "📞", "…", "🔧"
    )
    var selectedCategory by remember { mutableStateOf(0) }
    var remark by remember { mutableStateOf(TextFieldValue("")) }
    var amount by remember { mutableStateOf("0.00") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(top = 16.dp)
    ) {
        // 顶部Tab
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tabs.forEachIndexed { index, tab ->
                Text(
                    text = tab,
                    fontSize = 20.sp,
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedTab == index) Color.Black else Color.Gray,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable { selectedTab = index }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 分类网格
        val colCount = 5
        val gridItems = categories.size
        for (row in 0 until (gridItems + colCount - 1) / colCount) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (col in 0 until colCount) {
                    val index = row * colCount + col
                    if (index < categories.size) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable { selectedCategory = index }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        if (selectedCategory == index) Color(0xFFB2D7F5) else Color(0xFFF2F2F2),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = categoryIcons[index],
                                    fontSize = 28.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = categories[index],
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(56.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 备注和金额
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = remark,
                onValueChange = { remark = it },
                placeholder = { Text("点击填写备注…") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "￥$amount",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        // 日期、账户等标签
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("今天", "默认账本", "资产账户", "图片", "不报销").forEach {
                Text(
                    text = it,
                    color = Color(0xFF7BB6F7),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .background(Color(0x1A7BB6F7), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

    }
}