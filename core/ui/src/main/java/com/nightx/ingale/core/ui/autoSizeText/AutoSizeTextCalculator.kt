package com.nightx.ingale.core.ui.autoSizeText

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit

@Stable
internal interface AutoSizeTextCalculator {

    fun calculateTextSize(): TextUnit
}

@Composable
internal fun rememberAutoSizeTextCalculator(
    containerSize: IntSize,
    initialFontSize: TextUnit,
    forceFit: Boolean = true,
    text: String,
    style: TextStyle,
    maxLines: Int,
    softWrap: Boolean,
): AutoSizeTextCalculator {
    val textMeasurer = rememberTextMeasurer()

    return remember {
        LinearAutoSizeTextCalculator(
            containerSize = containerSize,
            initialFontSize = initialFontSize,
            forceFit = forceFit,
            textMeasurer = textMeasurer,
            text = text,
            style = style,
            maxLines = maxLines,
            softWrap = softWrap,
        )
    }
}
