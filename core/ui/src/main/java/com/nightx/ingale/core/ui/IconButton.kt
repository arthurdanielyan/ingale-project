package com.nightx.ingale.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.ui.theme.dimensions

@Composable
fun PassiveIconButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String?,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var actualTint by remember {
        mutableStateOf(tint)
    }
    Icon(
        modifier = modifier
            .size(IconSize)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        painter = painter,
        contentDescription = contentDescription,
        tint = actualTint,
    )
    val isPressed by interactionSource.collectIsPressedAsState()
    ObserveState(isPressed) {
        actualTint = if(it) {
            tint.darkenColor(PressedAlpha)
        } else {
            tint
        }
    }
}

@Composable
fun ActiveIconButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String?,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(tint.copy(alpha = ActiveIconButtonBackgroundAlpha))
            .clickable(onClick = onClick)
            .padding(MaterialTheme.dimensions.large),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = modifier
                .size(IconSize),
            painter = painter,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}

private const val PressedAlpha = 0.2f
private const val ActiveIconButtonBackgroundAlpha = 0.1f
private val IconSize = 24.dp
