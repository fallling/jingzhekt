package com.leng.jingzhekt.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.Entity.Type
import com.leng.jingzhekt.R
import com.leng.jingzhekt.presentation.viewmodel.ClassifyUiState
import com.leng.jingzhekt.presentation.viewmodel.ClassifyViewModel
import com.leng.jingzhekt.ui.components.Keyboard
import kotlin.math.log

// 为 Preview 提供示例数据
private fun getSampleClassifies(type: Type): List<Classify> {
    if(type == Type.Expend){
        return listOf(
            // 主要支出分类
            Classify.create("餐饮", R.drawable.icon_food, Level.Major, Type.Expend),
            Classify.create("购物", R.drawable.icon_shopping, Level.Major,Type.Expend),
            Classify.create("交通", R.drawable.icon_traffic, Level.Major,Type.Expend),
            Classify.create("娱乐", R.drawable.icon_entertainment, Level.Major,Type.Expend),
            Classify.create("医疗", R.drawable.icon_medicine, Level.Major,Type.Expend),
            Classify.create("教育", R.drawable.icon_study, Level.Major,Type.Expend),
            Classify.create("住房", R.drawable.icon_houserent, Level.Major,Type.Expend),

            // 次要分类
            Classify.create("早餐", R.drawable.icon_food, Level.Minor,Type.Expend),
            Classify.create("午餐", R.drawable.icon_food, Level.Minor,Type.Expend),
            Classify.create("晚餐", R.drawable.icon_food, Level.Minor,Type.Expend),
            Classify.create("服装", R.drawable.icon_shopping, Level.Minor,Type.Expend),
            Classify.create("日用品", R.drawable.icon_daily, Level.Minor,Type.Expend),
            Classify.create("公交", R.drawable.icon_traffic, Level.Minor,Type.Expend),
            Classify.create("打车", R.drawable.icon_traffic, Level.Minor,Type.Expend))
    }else{
        return listOf(
            // 主要收入分类
            Classify.create("工资", R.drawable.icon_salary, Level.Major,Type.Income),
            Classify.create("奖金", R.drawable.icon_winning, Level.Major,Type.Income),
            Classify.create("投资", R.drawable.icon_investment, Level.Major,Type.Income))
    }
}

@Composable
@Preview(name = "正常状态",locale = "zh-CN")
fun BillClassificationPreview(){
    var tab by remember { mutableStateOf(Type.Expend) }
    BillClassificationContent(
        uiState = ClassifyUiState(
            classifies = getSampleClassifies(type =tab ),
            isLoading = false,
        ),
        onTabChanged = { type ->
            tab = type
        }
    )
}

@Composable
@Preview(name = "加载状态", )
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
    var tab by remember { mutableStateOf(Type.Expend) }

    LaunchedEffect(tab) {
        classifyViewModel.loadAllClassifyList(tab)
    }

    BillClassificationContent(uiState = uiState, onTabChanged = {type ->
        tab = type
        Log.d("lengzq", "BillClassification:  ${type.name}")
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillClassificationContent(
    uiState: ClassifyUiState,
    onTabChanged: (Type) -> Unit = {}
) {

    //顶部tab选项
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(Type.Expend, Type.Income)
    val navController = rememberNavController()
    
    // Type 到中文名称的映射
    val typeToChineseName = mapOf(
        Type.Expend to "支出",
        Type.Income to "收入"
    )

    // 输入键盘焦点
    var mountInputState by remember { mutableStateOf(false)}
    val focusManager = LocalFocusManager.current

    //图标选项
    var selectedCategory by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))) {
            if (mountInputState) {
                Box(modifier = Modifier
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
                PrimaryTabRow(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null){
                    },
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    indicator = { TabRowDefaults.PrimaryIndicator(color=Color.Black) },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedTab == index,
                            selectedContentColor = Color.Black,
                            unselectedContentColor = Color.Gray,
                            onClick = {
                                selectedTab = index
                                navController.navigate(tab.name) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                onTabChanged(tab)
                            },
                            text = {
                                //Text(text = stringResource(if(tab == Type.Expend) R.string.title_expend else R.string.title_income),
                                Text(text = tab.name,
                                    fontSize = 20.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                NavHost(
                    navController = navController, 
                    startDestination = Type.Expend.name
                ) {
                    tabs.forEachIndexed { index, type ->
                        composable(type.name) {
                            //分类图标列表
                            Column {
                                ClassificationIcon(uiState)
                            }
                        }
                    }
                }
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
                        keyboardHeight.floatValue = layoutCoordinates.size.height.toFloat()
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
                            if (imeHeight > keyboardHeight.floatValue.toDp()) {
                                // 系统键盘弹出时：向上偏移 = 自定义键盘高度 + 系统键盘高度 + 间距
                                -(imeHeight + 8.dp)
                            } else {
                                // 系统键盘未弹出时：向上偏移 = 自定义键盘高度 + 间距
                                -(keyboardHeight.floatValue.toDp() + 8.dp)
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


@Composable
fun ClassificationIcon(uiState: ClassifyUiState){
    var selectedCategory by remember { mutableIntStateOf(0) }
    // 加载状态或分类网格
    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (uiState.classifies.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
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
        Column {
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

                            //显示图标
                            Box(
                                modifier = Modifier.size(56.dp)
                                    .background(if (selectedCategory == index) Color(0xFFB2D7F5) else Color(0xFFF2F2F2),
                                        shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = classify.iconResId),
                                    contentDescription = classify.name,
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.Unspecified // 保持原始颜色
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                            //图标标题
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