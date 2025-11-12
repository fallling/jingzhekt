package com.leng.jingzhekt.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.R
import com.leng.jingzhekt.presentation.viewmodel.ClassifyViewModel
import com.leng.jingzhekt.presentation.viewmodel.ClassifyUiState
import com.leng.jingzhekt.ui.components.Keyboard
import java.time.YearMonth

// 为 Preview 提供示例数据
private fun getSampleClassifies(): List<Classify> {
    return listOf(
        // 主要支出分类
        Classify.create("餐饮", com.leng.jingzhekt.R.drawable.icon_food, Level.Major),
        Classify.create("购物", com.leng.jingzhekt.R.drawable.icon_shopping, Level.Major),
        Classify.create("交通", com.leng.jingzhekt.R.drawable.icon_traffic, Level.Major),
        Classify.create("娱乐", com.leng.jingzhekt.R.drawable.icon_entertainment, Level.Major),
        Classify.create("医疗", com.leng.jingzhekt.R.drawable.icon_medicine, Level.Major),
        Classify.create("教育", com.leng.jingzhekt.R.drawable.icon_study, Level.Major),
        Classify.create("住房", com.leng.jingzhekt.R.drawable.icon_houserent, Level.Major),

        // 主要收入分类
        Classify.create("工资", com.leng.jingzhekt.R.drawable.icon_salary, Level.Major),
        Classify.create("奖金", com.leng.jingzhekt.R.drawable.icon_winning, Level.Major),
        Classify.create("投资", com.leng.jingzhekt.R.drawable.icon_investment, Level.Major),

        // 次要分类
        Classify.create("早餐", com.leng.jingzhekt.R.drawable.icon_food, Level.Minor),
        Classify.create("午餐", com.leng.jingzhekt.R.drawable.icon_food, Level.Minor),
        Classify.create("晚餐", com.leng.jingzhekt.R.drawable.icon_food, Level.Minor),
        Classify.create("服装", com.leng.jingzhekt.R.drawable.icon_shopping, Level.Minor),
        Classify.create("日用品", com.leng.jingzhekt.R.drawable.icon_daily, Level.Minor),
        Classify.create("公交", com.leng.jingzhekt.R.drawable.icon_traffic, Level.Minor),
        Classify.create("打车", R.drawable.icon_traffic, Level.Minor)
    )
}

@Composable
@Preview(name = "正常状态")
fun BillClassificationPreview(){
    BillClassificationContent(
        uiState = ClassifyUiState(
            classifies = getSampleClassifies(),
            isLoading = false
        )
    )
}

@Composable
@Preview(name = "加载状态")
fun BillClassificationLoadingPreview(){
    BillClassificationContent(
        uiState = ClassifyUiState(
            classifies = emptyList(),
            isLoading = true
        )
    )
}

@Composable
@Preview(name = "空数据状态")
fun BillClassificationEmptyPreview(){
    BillClassificationContent(
        uiState = ClassifyUiState(
            classifies = emptyList(),
            isLoading = false
        )
    )
}

@Composable
fun BillClassification(
    classifyViewModel: ClassifyViewModel = hiltViewModel()
) {
    val uiState by classifyViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        classifyViewModel.loadAllClassifyList()
    }

    BillClassificationContent(uiState = uiState)
}

@Composable
fun BillClassificationContent(
    uiState: ClassifyUiState
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("支出", "收入")

    var mountInputState by remember { mutableStateOf(false)}
    val focusManager = LocalFocusManager.current

    var selectedCategory by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->
        Box(modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F4F4))
        ) {
            if (mountInputState) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.3f))
                        .zIndex(10f)
                        .clickable {
                            mountInputState = false
                            focusManager.clearFocus()
                        }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 顶部Tab
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
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
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 加载状态或分类网格
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.classifies.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "暂无分类数据",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    // 分类网格
                    val colCount = 5
                    val gridItems = uiState.classifies.size
                for (row in 0 until (gridItems + colCount - 1) / colCount) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0 until colCount) {
                            val index = row * colCount + col
                            if (index < uiState.classifies.size) {
                                val classify = uiState.classifies[index]
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
                                                if (selectedCategory == index) Color(0xFFB2D7F5) else Color(
                                                    0xFFF2F2F2
                                                ),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // 使用分类的图标资源ID或默认emoji
                                        Text(
                                            text = (classify.name),
                                            fontSize = 28.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = classify.name,
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
                } // 结束分类网格的 else 块

            }
            
            val density = LocalDensity.current
            val keyboardHeight = remember { mutableFloatStateOf(0f) }
            val imeInsets = WindowInsets.ime
            val imeHeight = with(density) { imeInsets.getBottom(density).toDp() }

            Keyboard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(100f)
                    .onGloballyPositioned { layoutCoordinates ->
                        keyboardHeight.value = layoutCoordinates.size.height.toFloat()
                    }
            )
            
            // KeyBoardInputView 应该在键盘上方
            // 当系统键盘弹出时，需要向上偏移：自定义键盘高度 + 系统键盘高度 + 一些间距
            // 当系统键盘未弹出时，只需要考虑自定义键盘高度
            KeyBoardInputView(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(
                        x = 0.dp,
                        y = with(density) { 
                            if (imeHeight > keyboardHeight.value.toDp()) {
                                // 系统键盘弹出时：向上偏移 = 自定义键盘高度 + 系统键盘高度 + 间距
                                -( imeHeight + 8.dp)
                            } else {
                                // 系统键盘未弹出时：向上偏移 = 自定义键盘高度 + 间距
                                -(keyboardHeight.value.toDp() + 8.dp)
                            }
                        }
                    )
                    .zIndex(101f),
                onMountStateChange = { state ->
                    mountInputState = state
                    if(!state) {
                        focusManager.clearFocus()
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyBoardInputView(
    modifier: Modifier,
    onMountStateChange:(Boolean) -> Unit
) {
    var remark by remember { mutableStateOf(TextFieldValue("")) }
    var amount by remember { mutableStateOf("0.00") }
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // 备注和金额
        Card(
            modifier = Modifier
                .padding(8.dp)
            ,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onTertiary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = remark,
                    onValueChange = { remark = it },
                    placeholder = { Text("点击填写备注…") },
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { state ->
                            onMountStateChange(state.isFocused)
                        },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    trailingIcon = {
                        Text(
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null){
                                onMountStateChange(false)
                            } ,
                            text = "￥$amount",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            // 日期、账户等标签
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("今天", "默认账本", "资产账户", "图片", "不报销").forEach {
                    Text(
                        text = it,
                        color = Color(0xFF7BB6F7),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .background(Color(0x1A7BB6F7), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}