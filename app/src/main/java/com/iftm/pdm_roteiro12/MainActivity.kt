package com.iftm.pdm_roteiro12

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.iftm.pdm_roteiro12.presentation.navigation.BookApp
import com.iftm.pdm_roteiro12.ui.theme.BookTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookTheme {
                BookApp()
            }
        }
    }
}
