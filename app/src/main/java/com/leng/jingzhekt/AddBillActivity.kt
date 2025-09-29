package com.leng.jingzhekt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.leng.jingzhekt.presentation.ui.components.BillClassification
import com.leng.jingzhekt.presentation.ui.theme.AppTheme


class AddBillActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                BillClassification()
            }
        }
    }
}