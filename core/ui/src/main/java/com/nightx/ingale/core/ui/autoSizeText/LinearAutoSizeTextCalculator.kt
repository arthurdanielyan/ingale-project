package com.nightx.ingale.core.ui.autoSizeText

import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import kotlin.math.abs

internal class LinearAutoSizeTextCalculator(
    private val containerSize: IntSize,
    private val initialFontSize: TextUnit,
    private val forceFit: Boolean = true,
    private val textMeasurer: TextMeasurer,
    private val text: String,
    private val style: TextStyle,
    private val maxLines: Int,
    private val softWrap: Boolean,
) : AutoSizeTextCalculator {

    private companion object {
        const val SizeMultiplier = 0.1f
        const val MinDiff = 2
    }

    override fun calculateTextSize(): TextUnit {
        var estimatedSize = if (initialFontSize != TextUnit.Unspecified) {
            initialFontSize
        } else {
            style.fontSize
        }

        while (true) {
            textMeasurer.measure(
                text = text,
                softWrap = softWrap,
                maxLines = maxLines,
                style = style.copy(
                    fontSize = estimatedSize
                ),
                constraints = Constraints(
                    maxWidth = containerSize.width,
                    maxHeight = containerSize.height,
                )
            ).apply {
                if (didOverflowWidth || didOverflowHeight) {
                    estimatedSize *= (1f - SizeMultiplier)
                } else if (forceFit) {
                    if (
                        abs(this.size.width - containerSize.width) <= MinDiff ||
                        abs(this.size.height - containerSize.height) <= MinDiff
                    ) {
                        return estimatedSize
                    } else {
                        estimatedSize *= (1f + SizeMultiplier)
                    }
                } else {
                    return estimatedSize
                }
            }
        }
    }
}
