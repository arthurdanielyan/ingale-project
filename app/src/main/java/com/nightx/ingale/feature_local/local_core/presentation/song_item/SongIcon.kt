package com.nightx.ingale.feature_local.local_core.presentation.song_item

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nightx.ingale.R
import com.nightx.ingale.ui.theme.IngaleTheme

@Composable
fun SongIcon(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    model: Bitmap? = null,
) {
    if (model == null) {
        Icon(
            painter = painterResource(R.mipmap.ic_launcher_foreground),
            contentDescription = "Song picture",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = shape
                )
                .clip(shape)
        )
    } else {
        AsyncImage(
            model = model,
            contentDescription = "Song picture",
            modifier = modifier
                .size(60.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = shape
                )
                .clip(shape),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.mipmap.ic_launcher_foreground),
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SongIconPreviewDark() {
    IngaleTheme {
        SongIcon()
    }
}

@Preview
@Composable
private fun SongIconPreviewLight() {
    IngaleTheme {
        SongIcon()
    }
}
