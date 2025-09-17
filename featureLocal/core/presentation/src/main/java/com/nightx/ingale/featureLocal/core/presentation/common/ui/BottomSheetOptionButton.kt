package com.nightx.ingale.featureLocal.core.presentation.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.ui.SpacerWidth
import com.nightx.ingale.core.ui.theme.dimensions

@Composable
fun BottomSheetOptionButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    iconPainter: Painter? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(MaterialTheme.dimensions.normal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconPainter?.let {
            Image(
                modifier = Modifier.size(36.dp),
                painter = iconPainter,
                contentDescription = text
            )
            SpacerWidth(MaterialTheme.dimensions.normal)
        }
        Text(
            text = text
        )
    }
}
