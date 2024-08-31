package com.nightx.ingale.core.presentation.dialogComponent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * All dialogs should be rendered with this composable.
 *
 * [DialogComponentHolder.component] is not passed to [content] as compose internally holds
 * a reference to [DialogComponentHolder.component] inside [androidx.compose.runtime.internal.ComposableLambdaImpl]
 *
 * @param componentHolder - The holder of the [DialogComponent].
 * @param onDismissRequest - The callback that will be called when the dialog is dismissed.
 * @param properties - Properties used to customize the behavior of a [Dialog].
 * @param content - The content of the dialog.
 * */
@Composable
fun <C: DialogComponent<*,*,*>> Dialog(
    componentHolder: DialogComponentHolder<C>,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
) {
    val isVisible by componentHolder.isVisible.collectAsState()

    if(isVisible) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties,
            content = content,
        )
    }
}

/**
 * Temporary content controlled by a [DialogComponent].
 *
 * For instance [com.nightx.ingale.featureLocal.featureRequirePermissions.RequiredPermissionsRequesterKt.RequiredPermissionsRequesterDialog]
 * doesn't always show a Dialog but even at that point might need to stay alive.
 * */
@Composable
fun <C : DialogComponent<*, *, *>> DialogComponentView(
    componentHolder: DialogComponentHolder<C>,
    content: @Composable (C) -> Unit,
) {
    val isVisible by componentHolder.isVisible.collectAsState()

    if (isVisible) {
        val component = remember {
            componentHolder.component
        }
        content(component)
    }
}
