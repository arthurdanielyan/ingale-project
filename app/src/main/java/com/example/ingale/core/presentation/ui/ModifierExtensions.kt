package com.example.ingale.core.presentation.ui

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.semantics.Role

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
    var maxRadius by remember {
        mutableFloatStateOf(1f)
    }

    val colorScheme = MaterialTheme.colorScheme
    val bgColor = remember {
        colorScheme.surface
    }
    val shimmerWidth = remember { 0.5f }
    val doubleShimmerWidth = remember { 2 * shimmerWidth }
    val shimmerColor = remember {
        colorScheme.background.copy(alpha = ShimmerBgAlpha)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "shimmer effect")
    val radiusProgress by infiniteTransition.animateFloat(
        initialValue = -doubleShimmerWidth,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = tween(
                durationMillis = AnimationDuration,
                delayMillis = 0,
                easing = EaseInOut
            )
        ),
        label = "shimmer effect"
    )

    background(
        brush = Brush.radialGradient(
            colorStops = arrayOf(
                radiusProgress                      to bgColor,
                radiusProgress + shimmerWidth       to shimmerColor,
                radiusProgress + doubleShimmerWidth to bgColor,
            ),
            center = Offset.Zero,
            radius = maxRadius
        )
    ).onSizeChanged {
        maxRadius = maxOf(it.width, it.height).toFloat()
    }
}

private const val ShimmerBgAlpha = 0.7f
private const val AnimationDuration = 2000
