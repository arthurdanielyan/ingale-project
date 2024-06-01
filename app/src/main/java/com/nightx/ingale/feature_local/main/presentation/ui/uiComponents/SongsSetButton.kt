package com.nightx.ingale.feature_local.main.presentation.ui.uiComponents


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.onSizeChanged
import com.nightx.ingale.core.presentation.ui.AutoSizeText
import com.nightx.ingale.core.presentation.ui.darken
import com.nightx.ingale.ui.theme.dimensions
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
                        gradientWeak.darken()
                    ),
                    radius = gradientRadius.toFloat()
                ),
                shape = Shape
            )
            .onSizeChanged {
                gradientRadius = max(it.height, it.width)
            }
            .clip(Shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        AutoSizeText(
            text = text,
            modifier = Modifier.padding(MaterialTheme.dimensions.large),
            color = Color.White,
            maxLines = 1,
            forceFit = true
        )
    }
}

private const val DarkerAlpha = 0.3f
private val Shape = RoundedCornerShape(30)
