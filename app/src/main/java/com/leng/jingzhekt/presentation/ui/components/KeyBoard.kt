package com.leng.jingzhekt.ui.components

import android.util.Log
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leng.jingzhekt.Entity.Classify
import org.w3c.dom.Text
import kotlin.math.log


@Composable
fun Keyboard(modifier: Modifier = Modifier,
             onKeyPressed: (String) -> Unit = {},
             onPlusPressed: () -> Unit = {},
             onMinusPressed: () -> Unit = {},
             onDelete: () -> Unit = {},
             onDone: () -> Unit = {},){
    val haptic = LocalHapticFeedback.current
    // 数字键盘
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
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
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            when (key) {
                                "⌫" -> {
                                    Log.d("", "Keyboard: delete")
                                    onDelete()
                                }
                                "完成" -> {
                                    Log.d("lengzq", "Keyboard: done")
                                    onDone()
                                }
                                "-" -> {
                                    onMinusPressed()
                                }
                                "+" -> {
                                    onPlusPressed()
                                }
                                "再记" -> {

                                }
                                else -> {
                                    Log.d("lengzq", "Keyboard: key = $key")
                                    onKeyPressed(key)
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (key == "完成") Color.Black else Color.White,
                            contentColor = if (key == "完成") Color.White else Color.Black
                        )
                    ) {
                        Text(
                            text = key,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
