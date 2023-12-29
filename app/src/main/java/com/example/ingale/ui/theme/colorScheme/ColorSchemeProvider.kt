package com.example.ingale.ui.theme.colorScheme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

interface ColorScheme {
    val primaryInverse: Color
    val background: Color
    val backgroundInverse: Color
    val onBackground: Color
    val backgroundLight: Color
    val onBackgroundLight: Color
    val secondaryText: Color
}

val LocalColorScheme: ProvidableCompositionLocal<ColorScheme>
    = compositionLocalOf { ColorSchemeLight() }

@Suppress("UnusedReceiverParameter")
val MaterialTheme.colors: ColorScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalColorScheme.current
