package com.nightx.ingale.core.ui.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

@Composable
fun PaddingValues.copy(
    top: Dp = this.calculateTopPadding(),
    bottom: Dp = calculateBottomPadding(),
    start: Dp? = null,
    end: Dp? = null,
): PaddingValues {

    val layoutDirection = LocalLayoutDirection.current

    return PaddingValues(
        top = top,
        bottom = bottom,
        start = start ?: calculateStartPadding(layoutDirection),
        end = end ?: calculateEndPadding(layoutDirection)
    )
}
