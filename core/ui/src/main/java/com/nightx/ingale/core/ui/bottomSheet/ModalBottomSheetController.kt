@file:OptIn(ExperimentalFoundationApi::class)

package com.nightx.ingale.core.ui.bottomSheet

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal enum class BottomSheetState {
    Hidden, PartiallyExpanded, Expanded
}

@Stable
internal class BottomSheetController(
    private val density: Density,
    private val scope: CoroutineScope,
) {

    private companion object {
        const val MaxScrimAlpha = 0.4f
    }

    val anchoredDraggableState = AnchoredDraggableState(
        initialValue = BottomSheetState.Hidden,
        positionalThreshold = { distance: Float -> distance * 0.5f },
        velocityThreshold = { density.run { 10.dp.toPx() } },
        snapAnimationSpec = SpringSpec(),
        decayAnimationSpec = splineBasedDecay(density)
    ).apply {
        scope.launch {
            animateTo(
                if (anchors.hasAnchorFor(BottomSheetState.PartiallyExpanded)) {
                    BottomSheetState.PartiallyExpanded
                } else {
                    BottomSheetState.Expanded
                }
            )
            isNewlyAttached = false
        }
    }

    val nestedScroll = BottomSheetNestedScrollConnection(anchoredDraggableState)

    var isNewlyAttached by mutableStateOf(true)

    val scrimAlpha by derivedStateOf {
        if (anchoredDraggableState.anchors.hasAnchorFor(BottomSheetState.PartiallyExpanded)) {
            anchoredDraggableState.progress(
                BottomSheetState.Hidden,
                BottomSheetState.PartiallyExpanded
            )
        } else {
            anchoredDraggableState.progress(BottomSheetState.Hidden, BottomSheetState.Expanded)
        } * MaxScrimAlpha
    }
}

/** !!Almost copy from androidx.compose.material3.ConsumeSwipeWithinBottomSheetBoundsNestedScrollConnection*/
internal class BottomSheetNestedScrollConnection(
    private val anchoredDraggableState: AnchoredDraggableState<BottomSheetState>,
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        return if (available.y < 0f && source == NestedScrollSource.UserInput) {
            anchoredDraggableState.dispatchRawDelta(available.y).toOffset()
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        return if (source == NestedScrollSource.UserInput) {
            anchoredDraggableState.dispatchRawDelta(available.y).toOffset()
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val toFling = available.y
        val currentAnchor = anchoredDraggableState.currentValue
        return if (toFling < 0 && currentAnchor != BottomSheetState.Expanded) {
            val consumed = anchoredDraggableState.settle(toFling)
            Velocity(0f, consumed)
        } else {
            Velocity.Zero
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        anchoredDraggableState.settle(available.y)
        return available
    }

    private fun Float.toOffset() = Offset(0f, this)
}

@Composable
internal fun rememberBottomSheetController(): BottomSheetController {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    return remember {
        BottomSheetController(
            density = density,
            scope = scope,
        )
    }
}