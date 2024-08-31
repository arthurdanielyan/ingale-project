package com.nightx.ingale.core.ui

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

@Composable
fun Color.darken(@FloatRange(from = 0.0, to = 1.0) darkenAlpha: Float = 0.3f): Color =
    Color.Black.copy(alpha = darkenAlpha).compositeOver(this)

fun Color.darkenColor(@FloatRange(from = 0.0, to = 1.0) darkenAlpha: Float = 0.3f): Color =
    Color.Black.copy(alpha = darkenAlpha).compositeOver(this)