package com.jcb.moviesapp.presentation.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val cardElevation: Dp = 4.dp,
    val roundedCornerSmall: Dp = 8.dp,
    val paddingSmall: Dp = 8.dp,
    val paddingMedium: Dp = 16.dp,
    val paddingLarge: Dp = 24.dp,
    val cardWidth: Dp = 120.dp,
    val cardHeight: Dp = 200.dp,
    val cardCornerRadius: Dp = 16.dp,
    val imageWidthSmall: Dp = 100.dp,
    val imageHeightSmall: Dp = 80.dp,
    val productionCompanySize: Dp = 50.dp,
    val iconSize: Dp = 24.dp
)

val LocalDimensions = compositionLocalOf { Dimensions() }
