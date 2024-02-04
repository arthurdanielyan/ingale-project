package com.night.ingale.feature_local.main.presentation.ui_components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.onSizeChanged
import com.night.ingale.core.presentation.ui.navigationClickable
import com.night.ingale.ui.theme.spacing
import kotlin.math.max

@Composable
fun SongsSetButton(
    modifier: Modifier = Modifier,
    gradientWeak: Color,
    text: String,
    onClick: () -> Unit
) {
    var gradientRadius by remember {
        mutableIntStateOf(1)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.radialGradient(
                    center = Offset.Zero,
                    colors = listOf(
                        gradientWeak,
                        Color.Black.copy(alpha = DarkerAlpha).compositeOver(gradientWeak)
                    ),
                    radius = gradientRadius.toFloat()
                ),
                shape = Shape
            )
            .onSizeChanged {
                gradientRadius = max(it.height, it.width)
            }
            .clip(Shape)
            .navigationClickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(MaterialTheme.spacing.large),
            text = text,
            color = Color.White
        )
    }
}

private const val DarkerAlpha = 0.3f
private val Shape = RoundedCornerShape(30)
