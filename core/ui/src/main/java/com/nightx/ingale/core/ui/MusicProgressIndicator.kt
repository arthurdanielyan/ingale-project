package com.nightx.ingale.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn

@Composable
fun MusicProgressIndicator(
    progress: State<Float>,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dragState = rememberLinearProgressIndicatorDragState(
        onProgressChanged = onProgressChange,
        progress = progress,
    )
    val isDragging by dragState.isUserOverridingProgress.collectAsState()
    val dotScale by animateFloatAsState(
        targetValue = if (isDragging) {
            DotTappedScale
        } else {
            1f
        },
        label = "dot_scale"
    )
    BoxWithConstraints(
        modifier = modifier
            .height(3.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()
                    dragState.onTap(down.position.x)

                    var isDrag = false

                    // Track pointer movements until up
                    drag(down.id) { change ->
                        // Once we cross slop, it's a drag
                        if (!isDrag) isDrag = true

                        dragState.onDrag(change.positionChange().x)
                        change.consume()
                    }

                    // If it was just a tap (never crossed slop), fire tap
                    if (!isDrag) {
                        dragState.onTap(down.position.x)
                    }

                    dragState.onRelease()
                }
            }
    ) {
        val indicatorWidth = this.constraints.maxWidth
        dragState.setDragBound(indicatorWidth.toFloat())
        Box(
            modifier = Modifier
                .matchParentSize()
                .clipToBounds()
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        translationX = -indicatorWidth + (dragState.dragProgress * indicatorWidth)
                    }
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationX = dragState.dragProgress * indicatorWidth
                    scaleX = dotScale
                    scaleY = dotScale
                }
                .requiredSize(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        )
    }
}

@Stable
private class LinearProgressIndicatorDragState(
    private val onProgressChanged: (Float) -> Unit,
    private val compositionScope: CoroutineScope,
    private val progress: State<Float>,
) {

    private companion object {
        const val USER_INTERACTION_COOLDOWN_MS = 1000L
    }

    private var actualOffset by mutableFloatStateOf(0f)
    private fun updateOffset(value: Float) {
        actualOffset = value.coerceIn(0f, maxOffset)
    }

    val isUserOverridingProgress = MutableStateFlow(false)
    val dragProgress: Float
        get() = actualOffset / maxOffset

    private var maxOffset = Float.MAX_VALUE

    init {
        observeProgress()
    }

    fun onTap(offset: Float) {
        isUserOverridingProgress.value = true
        updateOffset(offset)
    }

    fun onDrag(dragAmount: Float) {
        isUserOverridingProgress.value = true
        updateOffset(actualOffset + dragAmount)
    }

    fun setDragBound(maxOffset: Float) {
        this.maxOffset = maxOffset
    }

    fun onRelease() {
        onProgressChanged(actualOffset / maxOffset)
        isUserOverridingProgress.value = false
    }

    private fun observeProgress() {
        combine(
            snapshotFlow { progress.value },
            isUserOverridingProgress.debounce {
                // Debounce to allow external progress state to update after user interaction
                if (it) {
                    0
                } else {
                    USER_INTERACTION_COOLDOWN_MS
                }
            }
        ) { progress, isDragging ->
            if (isDragging.not()) {
                updateOffset(progress * maxOffset)
            }
        }.launchIn(compositionScope)
    }
}

@Composable
private fun rememberLinearProgressIndicatorDragState(
    onProgressChanged: (Float) -> Unit,
    progress: State<Float>,
): LinearProgressIndicatorDragState {
    val scope = rememberCoroutineScope()
    return remember {
        LinearProgressIndicatorDragState(
            onProgressChanged = onProgressChanged,
            compositionScope = scope,
            progress = progress,
        )
    }
}

private const val DotTappedScale = 1.5f
