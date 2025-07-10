package com.leng.jingzhekt.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    data: List<Float>,
    labels: List<String>,
    colors: List<Color>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(16.dp)
    ) {
        // 交互：点击切换高亮
        var touchAngle by remember { mutableStateOf<Float?>(null) }
        Canvas(modifier = Modifier
            .width(600.dp)
            .height(480.dp)
            .pointerInput(data) {
                detectTapGestures { offset ->
                    val center = Offset((size.width / 2).toFloat(), (size.height / 2).toFloat())
                    val dx = offset.x - center.x
                    val dy = offset.y - center.y
                    val r = sqrt(dx * dx + dy * dy)
                    val radius = size.width / 2 * 0.85f
                    val strokeWidth = radius * 0.28f
                    if (r in (radius - strokeWidth / 2)..(radius + strokeWidth / 2)) {
                        // 计算角度
                        var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble()))
                        angle = (angle + 450) % 360 // 0度在正上方，顺时针
                        var start = 0.0
                        val total = data.sum()
                        data.forEachIndexed { i, v ->
                            val sweep = v / total * 360.0
                            if (angle in start..(start + sweep)) {
                                onSelect(i)
                                return@detectTapGestures
                            }
                            start += sweep
                        }
                    }
                }
            }
        ) {
            val total = data.sum()
            val sweepAngles = data.map { it / total * 360f }
            val side = minOf(size.width, size.height)
            val radius = side / 3 * 0.85f
            val strokeWidth = radius * 0.618f
            var startAngle = 0f
            val center = Offset(size.width / 2, size.height / 2)

            drawCircle(
                color = Color(0xfff4f4f6),
                radius = radius + strokeWidth * 1.15f/2
            )

            var selectedArcStartAngle = 0f
            var selectedArcSwapAngleAngle = 0f

            // 画每个环形扇区
            sweepAngles.forEachIndexed { i, sweep ->
                drawArc(
                    color = colors.getOrElse(i) { Color.Gray },
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(2 * radius, 2 * radius),
                    style = Stroke(
                        width =
                        if (i == selectedIndex)
                            strokeWidth * 1.15f
                        else
                            strokeWidth,
                    )
                )
                if (i == selectedIndex) {
                    selectedArcStartAngle = startAngle
                    selectedArcSwapAngleAngle = sweep
                }
                startAngle += sweep
            }
                    val path = Path().apply {
                        val point1 = getPointOnCircle(center, radius - (strokeWidth * 1.15f) / 2, selectedArcStartAngle)
                        val point2 = getPointOnCircle(center, (strokeWidth * 1.15f) / 2 + radius, selectedArcStartAngle)
                        val point3 =
                            getPointOnCircle(center, radius - (strokeWidth * 1.15f) / 2, selectedArcSwapAngleAngle + selectedArcStartAngle)
                        moveTo(point1.x, point1.y)
                        lineTo(point2.x, point2.y)
                        arcTo(
                            rect = Rect(
                                center = center,
                                radius = strokeWidth * 1.15f / 2 + radius
                            ),
                            startAngle,
                            selectedArcSwapAngleAngle,
                            true
                        )
                        lineTo(point3.x, point3.y)
                        arcTo(
                            rect = Rect(
                                center = center,
                                radius = -strokeWidth * 1.15f / 2 + radius
                            ),
                            startAngle + selectedArcSwapAngleAngle,
                            -selectedArcSwapAngleAngle,
                            false
                        )
                        close()
                    }
                    drawPath(
                        path = path,
                        color = Color.White,
                        style = Stroke(
                            width = 4.dp.toPx(),
                            cap = StrokeCap.Butt,
                            join = StrokeJoin.Round
                        ),
                    )

        }

        // 中心信息
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(labels[selectedIndex], style = MaterialTheme.typography.titleMedium, color = Color.Gray)
            Text("￥${data[selectedIndex]}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        }
    }
}


@Composable
fun CircularStatisticalCard(
    modifier: Modifier = Modifier,
    data: List<Float>,
    labels: List<String>,
    colors: List<Color>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .height(480.dp)
            .padding(16.dp)
    ) {
        DonutChart(
            data = data,
            labels = labels,
            colors = colors,
            selectedIndex = selectedIndex,
            onSelect = onSelect
        )
    }
}

@Preview
@Composable
fun CircularStatisticalCardPreview() {
    val data = listOf(5004.56f, 2809.04f, 496.55f, 200f, 100f)
    val labels = listOf("住房", "餐饮", "购物", "娱乐", "其他")
    val colors = listOf(
        Color(0xFFB2D7F5), Color(0xFF81D4FA), Color(0xFFB2F5E6), Color(0xFFF5E6B2), Color(0xFFF5B2B2)
    )
    var selectedIndex by remember { mutableStateOf(0) }

    CircularStatisticalCard(
        modifier = Modifier.width(490.dp),
        data = data,
        labels = labels,
        colors = colors,
        selectedIndex = selectedIndex,
        onSelect = { selectedIndex = it })
}

fun getPointOnCircle(
    o: Offset,
    radius: Float,
    sweepAngel: Float
    ): Offset{
    val angleRadians = Math.toRadians(sweepAngel.toDouble())
    val x = o.x + radius * cos(angleRadians).toFloat()
    val y = o.y + radius * sin(angleRadians).toFloat()
    return Offset(x,y)
}