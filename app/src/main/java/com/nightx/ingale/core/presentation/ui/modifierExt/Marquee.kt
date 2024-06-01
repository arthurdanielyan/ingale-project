package com.nightx.ingale.core.presentation.ui.modifierExt

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.marquee() = this.basicMarquee(
    iterations = Int.MAX_VALUE,
)

fun Modifier.transparentEdges(
    opacityWidth: Dp = 16.dp,
): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        drawContent()

        if(this.size.width < 2*opacityWidth.toPx()) return@drawWithContent

        val opacityWidthPx = opacityWidth.toPx()

        drawRect(
            topLeft = Offset.Zero,
            size = Size(
                width = opacityWidthPx,
                height = size.height
            ),
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Black
                ),
                startX = 0f,
                endX = opacityWidthPx
            ),
            blendMode = BlendMode.DstIn
        )

        drawRect(
            topLeft = Offset(
                x = size.width - opacityWidthPx,
                y = 0f
            ),
            size = Size(
                width = opacityWidthPx,
                height = size.height
            ),
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Black,
                    Color.Transparent
                ),
                startX = size.width - opacityWidthPx,
                endX = size.width
            ),
            blendMode = BlendMode.DstIn
        )
    }.clipToBounds()