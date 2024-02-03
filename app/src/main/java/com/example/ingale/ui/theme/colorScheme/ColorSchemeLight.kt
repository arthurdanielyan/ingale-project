package com.example.ingale.ui.theme.colorScheme

import androidx.compose.ui.graphics.Color

data class ColorSchemeLight(
    override val primary: Color = Color(0xFF004914),
    override val backgroundInverse: Color = Color(0xFF00B432),
    override val primaryInverse: Color = Color(0xFFB5FDBD),
    override val background: Color = Color(0xFFDFFFE3),
    override val onBackground: Color = Color(0xFF001301),
    override val backgroundLight: Color = Color(0xFFF1FFF3),
    override val onBackgroundLight: Color = Color(0xFF036200),
    override val secondaryText: Color = Color(0x5EFFFFFF)
) : ColorScheme
