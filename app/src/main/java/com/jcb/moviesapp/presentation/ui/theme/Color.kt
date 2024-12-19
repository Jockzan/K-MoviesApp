package com.jcb.moviesapp.presentation.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    background = Color.Black,
    onBackground = Color.White,
)

val LightColorScheme = lightColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
)


data class MovieColors(
    val background: Color = Color.Black,
    val textPrimary: Color = Color.White,
    val textSecondary: Color = Color.Gray,
)

val LocalColors = staticCompositionLocalOf { MovieColors() }