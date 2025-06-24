package com.leng.jingzhekt.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun LineChart(
    modifier: Modifier=Modifier,
    data: List<Float>,
    lineColor: Color = Color(0xFF81D4FA)
){
    if(data.isEmpty()) {
        return
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Canvas(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)){

                val maxY = data.maxOrNull() ?: 0f
                val minY = data.minOrNull() ?: 0f
                val rangeY = (maxY - minY).takeIf { it > 0 } ?:1f

                val stepX = size.width/(data.size-1).coerceAtLeast(1)
                val points = data.mapIndexed{ i , y ->
                    Offset(
                        x = i * stepX,
                        y = size.height - ((y-minY) / rangeY) * size.height
                    )
                }

                val path = Path().apply{
                    points.forEachIndexed{ i, point ->
                        if( i == 0) {
                            moveTo(point.x, point.y)
                        }else{
                            lineTo(point.x, point.y)
                        }
                    }
                }

                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round)
                )
            }
        }
    }
}

@Preview
@Composable
fun LineChartPreview(){
    val data = listOf(10f, 20f, 15f, 30f, 25f, 40f, 35f)
    Text("账单趋势", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
    LineChart(data = data)
}
