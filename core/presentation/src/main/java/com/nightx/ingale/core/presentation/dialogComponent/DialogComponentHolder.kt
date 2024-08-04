package com.nightx.ingale.core.presentation.dialogComponent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

class DialogComponentHolder<C : DialogComponent<*,*,*>>(
    private val factory: () -> C,
    private val scope: CoroutineScope,
) {

    @Volatile
    private var dialogComponent: C? = null
    private var observeDisposeEventJob: Job? = null

    init {
        observeDisposeEvent()
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

    @Synchronized
    operator fun getValue(thisRef: Any?, property: KProperty<*>): C =
        dialogComponent ?: synchronized(this) {
            factory().also {
                dialogComponent = it
                observeDisposeEvent()
            }
        }
}

fun <C: DialogComponent<*,*,*>> ViewModel.dialogComponent(factory: () -> C) =
    DialogComponentHolder(
        factory = factory,
        scope = viewModelScope
    )

fun <C: DialogComponent<*,*,*>> dialogComponent(scope: CoroutineScope, factory: () -> C) =
    DialogComponentHolder(
        factory = factory,
        scope = scope
    )
