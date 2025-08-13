package com.nightx.ingale.core.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun SpacerHeight(height: Dp) {
    Spacer(Modifier.height(height))
}

@Composable
fun SpacerWidth(width: Dp) {
    Spacer(Modifier.height(width))
}
