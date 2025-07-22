package com.leng.jingzhekt.ui.components

import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leng.jingzhekt.R

@Composable
fun MiniCard(
    title: String,
    data: Float,
    icon: Int = R.drawable.chart
) {
    Card(
        modifier = Modifier.size(180.dp, 100.dp).padding(horizontal = 8.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "统计",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = title, color = Color.Black, fontSize = 14.sp)
            }
            Text(
                text = "￥${"%.2f".format(data)}",
                color = Color(0xFF7AC0F6),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Preview
@Composable
fun MiniCardPreview() {
    Column {
        MiniCard(title = "支出金额", data = 928.55f, R.drawable.chart)
    }
}


@Preview
@Composable
fun Keyboard(){
    // 数字键盘
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            //.weight(1f, fill = false),
        //verticalArrangement = Arrangement.Bottom
    ) {
        val keys = listOf(
            listOf("1", "2", "3", "⌫"),
            listOf("4", "5", "6", "+"),
            listOf("7", "8", "9", "-"),
            listOf(".", "0", "再记", "完成")
        )
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { key ->
                    Button(
                        onClick = { /* TODO: 处理键盘输入 */ },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (key == "完成") Color.Black else Color.White,
                            contentColor = if (key == "完成") Color.White else Color.Black
                        )
                    ) {
                        Text(
                            text = key,
                            fontSize = 14.sp,
                            fontWeight = if (key == "完成") FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}