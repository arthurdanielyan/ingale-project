package com.nightx.ingale.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DefaultDimensions(
    val extraSmall: Dp = 2.dp,
    val small: Dp = 4.dp,
    val normal: Dp = 8.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 32.dp,
    val squared: Dp = 64.dp
)

val LocalDimensions = compositionLocalOf<DefaultDimensions> {
    error("No Spacing provided here")
}

@Suppress("UnusedReceiverParameter")
val MaterialTheme.dimensions: DefaultDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current