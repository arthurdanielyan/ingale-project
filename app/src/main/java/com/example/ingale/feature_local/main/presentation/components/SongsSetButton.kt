package com.example.ingale.feature_local.main.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.example.ingale.ui.theme.spacing

@Composable
fun SongsSetButton(
    modifier: Modifier = Modifier,
    gradientWeak: Color,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.radialGradient(
                    center = Offset.Zero,
                    colors = listOf(
                        gradientWeak,
                        Color(0, 0, 0, 77).compositeOver(gradientWeak)
                    ),
                ),
                shape = RoundedCornerShape(30)
            )
            .clip(RoundedCornerShape(30))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(MaterialTheme.spacing.large),
            text = text,
            color = Color.White
        )
    }
}