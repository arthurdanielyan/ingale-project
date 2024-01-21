package com.example.ingale.core.presentation.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally


const val screenTransitionDuration = 300

val slideInLeft = slideInHorizontally (
    animationSpec = tween(screenTransitionDuration),
    initialOffsetX = {
        it/2
    }
) + fadeIn(tween(screenTransitionDuration))

val slideOutLeft = slideOutHorizontally(
    animationSpec = tween(screenTransitionDuration),
    targetOffsetX = {
        -it/2
    }
) + fadeOut(tween(screenTransitionDuration))

val slideOutRight = slideOutHorizontally(
    animationSpec = tween(screenTransitionDuration),
    targetOffsetX = {
        it/2
    }
) + fadeOut(tween(screenTransitionDuration))

val slideInRight = slideInHorizontally (
    animationSpec = tween(screenTransitionDuration),
    initialOffsetX = {
        -it/2
    }
) + fadeIn(tween(screenTransitionDuration))