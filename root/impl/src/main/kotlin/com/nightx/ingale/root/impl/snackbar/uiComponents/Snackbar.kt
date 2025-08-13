package com.nightx.ingale.root.impl.snackbar.uiComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.root.api.snackbar.SnackbarComponent
import com.nightx.ingale.root.api.snackbar.SnackbarMessageReceiver
import com.nightx.ingale.root.impl.root.ui.components.BottomBarHeight
import com.nightx.ingale.root.impl.snackbar.uiComponents.SnackbarHost.Companion.SnackbarAnimDuration

@Composable
internal fun SnackbarView(
    modifier: Modifier = Modifier,
    bottomBarPadding: () -> Dp,
    snackbarComponent: SnackbarComponent,
) {
    val keyboardHeight by keyboardHeight()
    val snackbarBottomPadding by remember {
        derivedStateOf {
            if (keyboardHeight > BottomBarHeight) {
                keyboardHeight
            } else {
                bottomBarPadding()
            }
        }
    }
    SnackbarHost(
        modifier = modifier
            .offset {
                IntOffset(
                    x = 0,
                    y = -snackbarBottomPadding.roundToPx()
                )
            },
        snackbarMessageReceiver = snackbarComponent.snackbarMessageReceiver,
    )
}

@Composable
private fun SnackbarHost(
    modifier: Modifier = Modifier,
    snackbarMessageReceiver: SnackbarMessageReceiver,
) {
    val snackbarHost = rememberSnackbarHost(
        snackbarMessageReceiver = snackbarMessageReceiver
    )
    Snackbar(
        modifier = modifier,
        snackbarHost = snackbarHost
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Snackbar(
    modifier: Modifier = Modifier,
    snackbarHost: SnackbarHost,
) {
    AnimatedVisibility(
        modifier = modifier
            .anchoredDraggable(
                state = snackbarHost.anchoredDraggable,
                orientation = Orientation.Horizontal,
            )
            .graphicsLayer {
                translationX = snackbarHost.anchoredDraggable.requireOffset()
            },
        visible = snackbarHost.isVisible,
        enter = SnackbarEnterAnim,
        exit = SnackbarExitAnim,
    ) {
        BaseSnackbar(
            message = snackbarHost.currentSnackbarMessage
        )
    }
}

@Composable
private fun BaseSnackbar(
    modifier: Modifier = Modifier,
    message: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                MaterialTheme.dimensions.normal,
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(MaterialTheme.dimensions.normal),
            )
    ) {
        Text(
            modifier = Modifier
                .padding(MaterialTheme.dimensions.normal),
            text = message,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = MaxLineCount,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun keyboardHeight(): State<Dp> {
    val keyboardHeight = LocalDensity.current
        .run {
            WindowInsets.ime.getBottom(this).toDp()
        }
    return rememberUpdatedState(keyboardHeight)
}

private const val MaxLineCount = 3

private val SnackbarEnterAnim = slideInVertically(
    animationSpec = tween(SnackbarAnimDuration),
) {
    it
} + fadeIn(tween(SnackbarAnimDuration))
private val SnackbarExitAnim = slideOutVertically(
    animationSpec = tween(SnackbarAnimDuration),
) {
    it
} + fadeOut(tween(SnackbarAnimDuration))
