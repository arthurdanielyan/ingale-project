package com.nightx.ingale.core.ui.bottomSheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.ui.extensions.alpha

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ModalBottomSheetContent(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
    dragHandle: @Composable () -> Unit,
    bottomSheetController: BottomSheetController,
) {
    LaunchedEffect(bottomSheetController.anchoredDraggableState.settledValue) {
        if (
            bottomSheetController.anchoredDraggableState.settledValue == BottomSheetState.Hidden &&
            bottomSheetController.isNewlyAttached.not()
        ) {
            onDismissRequest()
        }
    }

    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        val screenHeight = this.constraints.maxHeight.toFloat()
        Scrim(
            onDismissRequest = onDismissRequest,
            alpha = { bottomSheetController.scrimAlpha }
        )
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationY = bottomSheetController.anchoredDraggableState.requireOffset()
                }
                .anchoredDraggable(
                    state = bottomSheetController.anchoredDraggableState,
                    orientation = Orientation.Vertical,
                    enabled = true,
                )
                .onSizeChanged { sheetContentSize ->
                    val shouldAddPartialExpansionAnchor =
                        screenHeight - sheetContentSize.height < density.run { PartialExpansionExistenceThreshold.toPx() }
                    bottomSheetController.anchoredDraggableState.updateAnchors(
                        DraggableAnchors {
                            BottomSheetState.Hidden at screenHeight
                            if (shouldAddPartialExpansionAnchor) {
                                BottomSheetState.PartiallyExpanded at screenHeight * PartialExpansionPercentage
                            }
                            BottomSheetState.Expanded at (screenHeight - sheetContentSize.height)
                        },
                    )
                }
        ) {
            Surface(modifier) {
                Column(
                    modifier = Modifier.nestedScroll(bottomSheetController.nestedScroll),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    dragHandle()
                    content()
                }
            }
        }
    }
}

@Composable
private fun Scrim(
    onDismissRequest: () -> Unit,
    alpha: () -> Float,
) {
    Box(
        Modifier
            .fillMaxSize()
            .alpha {
                alpha()
            }
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures { onDismissRequest() }
            }
    )
}

private val PartialExpansionExistenceThreshold = 25.dp
private const val PartialExpansionPercentage = 0.4f
