package com.nightx.ingale.core.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import kotlin.math.roundToInt

const val screenTransitionDuration = 300

val enterTransition = slideInHorizontally(
    animationSpec = tween(screenTransitionDuration),
    initialOffsetX = {
        it
    }
)

val popEnterTransition: EnterTransition
    @Composable get() {
        val targetOffset = ScreenTransitionOffset
        return slideInHorizontally(
            animationSpec = tween(screenTransitionDuration),
            initialOffsetX = { targetOffset }
        )
    }

val exitTransition: ExitTransition
    @Composable get() {
        val targetOffset = ScreenTransitionOffset
        return slideOutHorizontally(
            animationSpec = tween(screenTransitionDuration),
            targetOffsetX = { targetOffset }
        )
    }

val popExitTransition = slideOutHorizontally(
    animationSpec = tween(screenTransitionDuration),
    targetOffsetX = {
        it
    }
)

private val ScreenTransitionOffset: Int
    @Composable get() =
        (-LocalConfiguration.current.screenWidthDp * 0.8f).roundToInt()
