package com.leng.jingzhekt.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat


@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    originalData: List<Float>,
    lineColor: Color = Color(0xFF81D4FA),
    xLabels: List<String> = emptyList()
) {
    //补全30位
    val data = if (originalData.isEmpty() || originalData.size < 30) {
        originalData + FloatArray(30 - originalData.size).toList()
    } else {
        originalData
    }

    val selectIndex = mutableIntStateOf(2)
    // 图表区域
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 15.dp)
        ) {

            val maxY = data.maxOrNull() ?: 0f
            val minY = data.minOrNull()?.takeIf { it < 0 } ?: 0f
            val rangeY = (maxY - minY).takeIf { it > 0 } ?: 1f

            val chartWidth = size.width - 40.dp.toPx() // 留出左右边距
            val chartHeight = size.height - 20.dp.toPx()
            val startX = 20.dp.toPx()
            val startY = 10.dp.toPx()

            val stepX = chartWidth / 30

            // 计算数据点
            val points = data.mapIndexed { i, y ->
                Offset(
                    x = startX + i * stepX,
                    y = startY + chartHeight - ((y - minY) / rangeY) * chartHeight
                )
            }

            // 绘制背景网格条纹
            val gridCount = 30
            val gridWidth = (chartWidth / gridCount) * 4 / 5
            for (i in 0 until gridCount) {
                drawRect(
                    color = Color(0xFFF8F8F8),
                    size = Size(gridWidth, chartHeight),
                    topLeft = Offset(
                        startX + i * (chartWidth / gridCount) - gridWidth / 2,
                        startY
                    )
                )
            }

            //draw line
            if (points.size > 1) {
                val solidPath = Path().apply {
                    for (i in 0..points.size - 1) {
                        val point = points[i]
                        if (i == 0) {
                            moveTo(point.x, point.y)
                        } else {
                            lineTo(point.x, point.y)
                        }
                    }
                }

                drawPath(
                    path = solidPath,
                    color = lineColor,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // 绘制数据点（只绘制前几个实线部分的点）
            if (points.isNotEmpty()) {
                points.mapIndexed { i, point ->
                    if (i == selectIndex.value) {
                        // 外圈黑色
                        drawCircle(
                            color = Color.Black,
                            radius = 3.dp.toPx(),
                            center = point
                        )
                    } else {
                        // 外圈白色
                        drawCircle(
                            color = Color.White,
                            radius = 3.dp.toPx(),
                            center = point
                        )
                    }

                    // 内圈颜色不变
                    drawCircle(
                        color = lineColor,
                        radius = 1.dp.toPx(),
                        center = point
                    )
                }
            }
        }

        // X轴标签
        if (xLabels.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                xLabels.forEach { label ->
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LineChartPreview() {
    // 模拟参考图的数据分布：开始较高，然后下降，后续保持平稳
    val data = listOf(
        0f,     // 起始点
        180f,   // 高峰
        120f,   // 下降
        90f,    // 继续下降
        80f,    // 最低点
        75f    // 平稳
    )

    val xLabels = listOf("01", "05", "10", "15", "20", "25", "30")

    LineChartCard(
        originalData = data,
        title = "支出趋势",
        date = "2025-05-02",
        totalAmount = 1106.41f,
        xLabels = xLabels
    )
}

@Composable
fun LineChartCard(
    modifier: Modifier = Modifier,
    originalData: List<Float>,
    title: String = "支出趋势",
    date: String = "",
    totalAmount: Float = 0f,
    lineColor: Color = Color(0xFF81D4FA),
    xLabels: List<String> = emptyList()
) {
    val formatter = DecimalFormat("#.##")
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 蓝色指示条
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(16.dp)
                                .background(Color(0xFF2196F3))
                        )
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    if (date.isNotEmpty()) {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp, start = 12.dp)
                        )
                    }
                }

                if (totalAmount > 0) {
                    Text(
                        text = "累计支出：¥${formatter.format(totalAmount)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

            }
            LineChart(
                originalData = originalData,
                xLabels = xLabels,
                lineColor = lineColor
            )
        }
    }
}
