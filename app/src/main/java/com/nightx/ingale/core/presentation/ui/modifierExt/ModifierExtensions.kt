package com.nightx.ingale.core.presentation.ui.modifierExt

import androidx.compose.ui.Modifier

fun Modifier.modifyIf(condition: Boolean, modifier: Modifier.() -> Modifier) =
    this.then(
        if(condition) {
            this.modifier()
        } else {
            Modifier
        }
    )
