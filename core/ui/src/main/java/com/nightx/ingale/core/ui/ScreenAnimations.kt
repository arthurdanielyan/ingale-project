package com.nightx.ingale.core.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimator

const val ScreenTransitionDuration = 300

fun slideInFromRightOutToLeft(): StackAnimator =
    stackAnimator(tween(ScreenTransitionDuration)) { factor, _, content ->
        content(
            Modifier.graphicsLayer {
                translationX = size.width * factor
            }
        )
    }

fun slideInFromLeftOutToRight(): StackAnimator =
    stackAnimator(tween(ScreenTransitionDuration)) { factor, _, content ->
        content(
            Modifier.graphicsLayer {
                translationX = size.width * -factor
            }
        )
    }

fun slideInFromBottom(): EnterTransition =
    slideInVertically(tween(ScreenTransitionDuration)) {
        it
    }

fun slideOutToBottom(): ExitTransition =
    slideOutVertically(tween(ScreenTransitionDuration)) {
        it
    }

