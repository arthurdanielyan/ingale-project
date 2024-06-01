package com.nightx.ingale.core.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import com.nightx.ingale.ui.theme.dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngaleBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
    content: @Composable () -> Unit,
) {
    IngaleBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        windowInsets = windowInsets,
        cornerRadius = MaterialTheme.dimensions.large,
        bottomOffset = MaterialTheme.dimensions.large,
        content = content
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngaleBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
    cornerRadius: Dp,
    bottomOffset: Dp,
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .padding(
                horizontal = MaterialTheme.dimensions.large
            ),
        sheetState = sheetState,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        windowInsets = windowInsets,
        shape = RoundedCornerShape(
            cornerRadius
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color.Unspecified,
                    shape = RoundedCornerShape(
                        bottomStart = cornerRadius,
                        bottomEnd = cornerRadius
                    )
                )
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = cornerRadius,
                        bottomEnd = cornerRadius
                    )
                )
        ) {
            content()
        }
        Canvas(
            modifier = Modifier
                .height(bottomOffset)
                .fillMaxWidth()
        ) {
            val cornerRadiusPx = cornerRadius.toPx()
            drawPath(
                path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(cornerRadius.toPx(), 0f)
                    arcTo(
                        rect = Rect(
                            offset = Offset(0f, -2*cornerRadiusPx),
                            size = Size(2*cornerRadiusPx, 2*cornerRadiusPx)
                        ),
                        startAngleDegrees = 90f,
                        sweepAngleDegrees = 90f,
                        forceMoveTo = false
                    )
                    close()
                },
                color = scrimColor,
                blendMode = BlendMode.Src
            )
            drawPath(
                path = Path().apply {
                    moveTo(size.width, 0f)
                    lineTo(size.width - cornerRadius.toPx(), 0f)
                    arcTo(
                        rect = Rect(
                            offset = Offset(size.width-2*cornerRadiusPx, -2*cornerRadiusPx),
                            size = Size(2*cornerRadiusPx, 2*cornerRadiusPx)
                        ),
                        startAngleDegrees = 90f,
                        sweepAngleDegrees = -90f,
                        forceMoveTo = false
                    )
                    close()
                },
                color = scrimColor,
                blendMode = BlendMode.Src
            )
            drawRect(
                topLeft = Offset.Zero,
                size = this.size,
                color = scrimColor,
                blendMode = BlendMode.Src
            )
        }
    }
}