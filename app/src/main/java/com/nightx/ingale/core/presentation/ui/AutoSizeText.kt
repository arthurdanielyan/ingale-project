package com.nightx.ingale.core.presentation.ui

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue

/**
 * Text that automatically fits its underlying container if with its
 * initial font size exceeds(if [forceFit] is true also doesn't fit) it
 * */
@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = LocalTextStyle.current,
    forceFit: Boolean = true,
) {
    val actualFontSize = remember {
        if (fontSize != TextUnit.Unspecified) {
            fontSize
        } else {
            style.fontSize
        }
    }
    var estimatedFontSize by remember {
        mutableFloatStateOf(
            if (fontSize != TextUnit.Unspecified) {
                fontSize.value
            } else {
                style.fontSize.value
            }
        )
    }
    var max by remember {
        mutableFloatStateOf(Float.NaN)
    }
    var min by remember {
        mutableFloatStateOf(Float.NaN)
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }
    Text(
        text = text,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        fontSize = if (actualFontSize.isSp) {
            estimatedFontSize.sp
        } else {
            estimatedFontSize.em
        },
        color = color,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = { result ->
            if (result.didOverflowWidth || result.didOverflowHeight) {
                max = estimatedFontSize
                if (min.isNaN()) {
                    estimatedFontSize *= 0.5f
                } else {
                    estimatedFontSize = min + 0.5f * (max - min)
                }
            } else {
                min = estimatedFontSize
                if ((max - min).absoluteValue < MinDiff || !forceFit || max.isNaN()) {
                    shouldDraw = true
                } else if (!max.isNaN()) {
                    estimatedFontSize = min + 0.5f * (max - min)
                }
            }
        },
        style = style
    )
}

private const val MinDiff = 1f
