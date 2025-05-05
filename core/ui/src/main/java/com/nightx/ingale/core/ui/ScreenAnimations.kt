package com.nightx.ingale.core.ui

import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimator

const val screenTransitionDuration = 300

fun slideInFromRightOutToLeft(): StackAnimator =
    stackAnimator(tween(screenTransitionDuration)) { factor, _, content ->
        content(
            Modifier.graphicsLayer {
                translationX = size.width * factor
            }
        )
    }

fun slideInFromLeftOutToRight(): StackAnimator =
    stackAnimator(tween(screenTransitionDuration)) { factor, _, content ->
        content(
            Modifier.graphicsLayer {
                translationX = size.width * -factor
            }
        )
    }

