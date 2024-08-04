package com.nightx.ingale.core.presentation.dialogComponent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.DialogProperties

@Composable
fun <C: DialogComponent<*,*,*>> Dialog(
    component: C,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable (C) -> Unit,
) {
    val isVisible by component.isVisible.collectAsState()

    if(isVisible) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties,
            content = {
                content(component)
            },
        )
    }
}