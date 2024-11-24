package com.nightx.ingale.core.ui.bottomSheet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.ui.theme.dimensions
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ModalBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
    dragHandle: @Composable () -> Unit = { DragHandle() },
    content: @Composable () -> Unit,
) {
    val bottomSheetController = rememberBottomSheetController()

    val scope = rememberCoroutineScope()
    val hide: (() -> Unit) = remember {
        {
            scope.launch {
                bottomSheetController.anchoredDraggableState.animateTo(BottomSheetState.Hidden)
                onDismissRequest()
            }
        }
    }

    ModalBottomSheetDialog(
        onDismissRequest = hide,
        properties = properties,
    ) {
        ModalBottomSheetContent(
            modifier = modifier,
            onDismissRequest = hide,
            content = content,
            dragHandle = dragHandle,
            bottomSheetController = bottomSheetController,
        )
    }
}

@Composable
private fun DragHandle() {
    Box(
        Modifier
            .padding(vertical = MaterialTheme.dimensions.small)
            .requiredWidth(DragHandleWidth)
            .requiredHeight(DragHandleHeight)
            .background(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = CircleShape
            )
    )
}

private val DragHandleWidth = 60.dp
private val DragHandleHeight = 4.dp

