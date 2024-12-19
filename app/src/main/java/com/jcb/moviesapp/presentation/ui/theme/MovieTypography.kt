package com.jcb.moviesapp.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

data class MovieTypography(
    val bodyLarge: TextStyle = TextStyle(fontSize = 18.sp),
    val bodyMedium: TextStyle = TextStyle(fontSize = 14.sp),
    val bodySmall: TextStyle = TextStyle(fontSize = 12.sp),
    val bodyXSmall: TextStyle = TextStyle(fontSize = 10.sp)
)

val LocalTypography = staticCompositionLocalOf { MovieTypography() }