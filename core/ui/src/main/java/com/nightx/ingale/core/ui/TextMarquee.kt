package com.nightx.ingale.core.ui

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.ui.extensions.marquee
import com.nightx.ingale.core.ui.extensions.modifyIf
import com.nightx.ingale.core.ui.extensions.transparentEdges

/**
 * Applies marquee effect with transparent edges to the text.
 * */
@Composable
fun TextMarquee(
    text: String,
    modifier: Modifier = Modifier,
    opacityWidth: Dp = 16.dp,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    onTextLayout: (TextLayoutResult) -> Unit = {},
) {
    val measurer = rememberTextMeasurer()

    SubcomposeLayout(modifier) { constraints ->
        // Measure the text width independently (single line, no wrap)
        val layoutResult = measurer.measure(
            text = text,
            style = style.merge(
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight
            ),
            maxLines = 1,
            softWrap = false
        )
        val textPxWidth = layoutResult.size.width
        val containerWidth = constraints.maxWidth
        val shouldScroll = textPxWidth > containerWidth

        // Subcompose exactly one version with the *final* modifiers
        val placeables = subcompose(if (shouldScroll) "scroll" else "static") {
            val base = Modifier
                .modifyIf(shouldScroll) {
                    transparentEdges(opacityWidth = opacityWidth)
                        .marquee()
                }

            Text(
                text = text,
                modifier = base,
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign,
                lineHeight = lineHeight,
                maxLines = 1,
                softWrap = false,
                onTextLayout = onTextLayout,
                style = style,
            )
        }.map { measurable ->
            // Force width to container so we don't relayout later
            measurable.measure(
                Constraints.fixedWidth(containerWidth).copy(
                    minHeight = 0,
                    maxHeight = constraints.maxHeight
                )
            )
        }

        val height = placeables.maxOf { it.height }.coerceAtLeast(0)
        layout(containerWidth, height) {
            placeables.forEach { it.place(0, 0) }
        }
    }
}

