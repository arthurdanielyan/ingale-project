package com.nightx.ingale.bottomBar.impl.snackbar.uiComponents

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.nightx.ingale.bottomBar.api.LocalSnackbarMessageReceiver
import com.nightx.ingale.bottomBar.api.SnackbarMessageReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

enum class SnackbarState {
    Visible, SwipedRight, SwipedLeft;

    val isDismissed: Boolean
        get() = this == SwipedRight || this == SwipedLeft
}

@OptIn(ExperimentalFoundationApi::class)
@Stable
internal class SnackbarHost(
    val snackbarMessageReceiver: SnackbarMessageReceiver,
    val density: Density,
    val screenWidth: Float,
) {
    companion object {
        private const val SnackbarDuration = 3000L
        internal const val SnackbarAnimDuration = 300
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    val anchoredDraggable = AnchoredDraggableState(
        initialValue = SnackbarState.Visible,
        positionalThreshold = { distance: Float -> distance * 0.1f },
        velocityThreshold = { density.run { 50.dp.toPx() } },
        snapAnimationSpec = tween(SnackbarAnimDuration),
        decayAnimationSpec = splineBasedDecay(density),
        anchors = DraggableAnchors {
            SnackbarState.Visible at 0f
            SnackbarState.SwipedRight at screenWidth
            SnackbarState.SwipedLeft at -screenWidth
        }
    )

    var currentSnackbarMessage by mutableStateOf("")
    var isVisible by mutableStateOf(false)

    init {
        observeSnackbarMessage()
        observeSnackbarDrag()
    }

    private fun observeSnackbarMessage() {
        scope.launch {
            snackbarMessageReceiver.snackbarMessage.collectLatest { message ->
                if (isVisible) {
                    dismiss() // dismiss previous
                    if (anchoredDraggable.settledValue.isDismissed.not()) {
                        delay((SnackbarAnimDuration.toLong() * 0.8f).roundToLong())
                    }
                }
                anchoredDraggable.snapTo(SnackbarState.Visible)
                currentSnackbarMessage = message
                isVisible = true
                Log.d("myLogs", "show")
                delay(SnackbarDuration)
                isVisible = false
                Log.d("myLogs", "hide")
            }
        }
    }

    private fun observeSnackbarDrag() {
        scope.launch {
            snapshotFlow { anchoredDraggable.settledValue }.collectLatest {
                if (it.isDismissed) {
                    dismiss()
                }
            }
        }
    }

    private fun dismiss() {
        isVisible = false
    }
}

@Composable
internal fun rememberSnackbarHost(): SnackbarHost {
    val snackbarMessageReceiver = LocalSnackbarMessageReceiver.current
    val density = LocalDensity.current
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return remember {
        SnackbarHost(
            snackbarMessageReceiver = snackbarMessageReceiver,
            density = density,
            screenWidth = density.run {
                screenWidth.dp.toPx()
            },
        )
    }
}
