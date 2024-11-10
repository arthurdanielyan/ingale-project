package com.nightx.ingale.core.presentation.dialogComponent

import androidx.annotation.UiThread
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
class DialogComponentHolder<C : DialogComponent<*, *, *>>(
    private val factory: () -> C,
    private val scope: CoroutineScope,
) {

    @Volatile
    private var dialogComponent: C? = null
    private var observeDisposeEventJob: Job? = null

    private val _isVisible = MutableStateFlow(false)
    val isVisible = _isVisible.asStateFlow()

    val component: C
        @Synchronized
        @UiThread
        get() {
            var result = dialogComponent
            return result ?: synchronized(this) {
                result = dialogComponent
                result ?: factory().also {
                    result = it
                    dialogComponent = it
                    observeDisposeEvent()
                }
            }
        }

    private fun observeDisposeEvent() {
        observeDisposeEventJob?.cancel()
        observeDisposeEventJob = scope.launch {
            dialogComponent?.disposeEvent?.collect {
                dialogComponent = null
                observeDisposeEventJob = null
            }
        }
    }

    fun show() {
        _isVisible.update { true }
    }

    fun dismiss() {
        _isVisible.update { false }
        dialogComponent?.dismiss()
    }
}

fun <C : DialogComponent<*, *, *>> ViewModel.dialogComponent(factory: () -> C) =
    DialogComponentHolder(
        factory = factory,
        scope = viewModelScope
    )

fun <C : DialogComponent<*, *, *>> dialogComponent(scope: CoroutineScope, factory: () -> C) =
    DialogComponentHolder(
        factory = factory,
        scope = scope
    )
