package com.example.ingale.core.presentation.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntSize
import com.example.ingale.ui.theme.colorScheme.ingaleColors

fun Modifier.navigationClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier = composed {

    val navigationClickListener = remember { NavigationClickListener(onClick) }

    this.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = navigationClickListener::onNavigationClick
    )
}

private class NavigationClickListener(
    private val onClick: () -> Unit,
) {
    private var lastClick = -1L

    fun onNavigationClick() {
        if (System.currentTimeMillis() - lastClick > screenTransitionDuration + 500 || lastClick == -1L) {
            onClick()
            lastClick = System.currentTimeMillis()
        }
    }
}

fun Modifier.shimmer(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize(1,1))
    }
    val shimmerWidth = remember {
        0.9f
    }

    val colorScheme = MaterialTheme.ingaleColors
    val bgColor = remember {
        colorScheme.primaryInverse
    }
    val shimmerColor = remember {
        colorScheme.backgroundInverse.copy(alpha = ShimmerBgAlpha)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "shimmer effect")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -shimmerWidth,
        targetValue = 1f+shimmerWidth,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(
                durationMillis = AnimationDuration,
                delayMillis = 0,
                easing = LinearEasing
            )
        ),
        label = "shimmer effect"
    )

    background(
        brush = Brush.linearGradient(
            colorStops = arrayOf(
                shimmerOffset - shimmerWidth to bgColor,
                shimmerOffset to shimmerColor,
                shimmerOffset + shimmerWidth to bgColor,
            ),
            start = Offset.Zero,
            end = Offset(size.width.toFloat(), size.height.toFloat())
        )
    ).onSizeChanged {
        size = it
    }
}

const val ShimmerBgAlpha = 0.2f
const val AnimationDuration = 2000
