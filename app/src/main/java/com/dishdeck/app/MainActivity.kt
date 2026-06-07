package com.dishdeck.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.dishdeck.app.navigation.NavGraph
import com.dishdeck.app.ui.theme.DishDeckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DishDeckTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
                }
            }
        }
    }
