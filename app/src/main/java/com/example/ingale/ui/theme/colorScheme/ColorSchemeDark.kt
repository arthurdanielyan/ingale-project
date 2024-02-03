package com.example.ingale.ui.theme.colorScheme

import androidx.compose.ui.graphics.Color

data class ColorSchemeDark(
    override val primary: Color = Color(0xFF00CA4D),
    override val primaryInverse: Color = Color(0xFF005520),
    override val background: Color = Color(0xFF001301),
    override val backgroundInverse: Color = Color(0xFF00B432),
    override val onBackground: Color = Color(0xFFA9F0B2),
    override val backgroundLight: Color = Color(0xFF022E12),
    override val onBackgroundLight: Color = Color(0xFF8ED89B),
    override val secondaryText: Color = Color(0x5EFFFFFF)
) : ColorScheme
