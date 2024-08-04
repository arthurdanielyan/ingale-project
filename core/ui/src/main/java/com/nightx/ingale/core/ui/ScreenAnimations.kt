package com.nightx.ingale.core.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

const val screenTransitionDuration = 300

val slideInLeft = slideInHorizontally (
    animationSpec = tween(screenTransitionDuration),
    initialOffsetX = {
        it
    }
)

val slideOutRight = slideOutHorizontally(
    animationSpec = tween(screenTransitionDuration),
    targetOffsetX = {
        it
    }
)

val nothingExit = slideOutHorizontally (
    animationSpec = tween(
        delayMillis = screenTransitionDuration,
        durationMillis = screenTransitionDuration
    ),
    targetOffsetX = { -it }
)

val nothingEnter = slideInHorizontally (
    animationSpec = tween(
        durationMillis = 0
    ),
    initialOffsetX = { 0 }
)
