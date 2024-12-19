package com.jcb.moviesapp.presentation.ui.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.jcb.moviesapp.presentation.ui.navigation.AppNavHost
import com.jcb.moviesapp.presentation.ui.theme.MoviesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoviesAppTheme {
                AppNavHost(navController = rememberNavController())
            }
        }
    }
}
