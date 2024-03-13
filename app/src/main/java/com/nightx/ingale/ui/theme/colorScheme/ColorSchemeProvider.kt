package com.nightx.ingale.ui.theme.colorScheme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

interface IngaleColors {
    val additional1: Color
    val additional2: Color
    val additional3: Color
}

val LocalIngaleColors: ProvidableCompositionLocal<IngaleColors> = compositionLocalOf {
    throw Exception("No theme provided")
}

@Suppress("UnusedReceiverParameter")
val MaterialTheme.ingaleColors: IngaleColors
    @Composable
    @ReadOnlyComposable
    get() = LocalIngaleColors.current
