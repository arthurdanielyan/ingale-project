package com.nightx.ingale.core.ui.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.modifyIf(condition: Boolean, modifier: Modifier.() -> Modifier) =
    this.then(
        if(condition) {
            this.modifier()
        } else {
            Modifier
        }
    )

fun Modifier.alpha(alpha: () -> Float) =
    this.graphicsLayer {
        this.alpha = alpha()
    }
