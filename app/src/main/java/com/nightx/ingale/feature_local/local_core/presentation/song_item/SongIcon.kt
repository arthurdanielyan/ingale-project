package com.nightx.ingale.feature_local.local_core.presentation.song_item

import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.nightx.ingale.R
import com.nightx.ingale.core.presentation.view.SingleLaunchedEffect
import com.nightx.ingale.ui.theme.IngaleTheme

@Composable
fun SongIcon(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    picturePath: String = "",
) {
    if (picturePath.isBlank()) {
        val context = LocalContext.current
        val pictureResource = remember {
            BitmapFactory.decodeResource(
                context.resources,
                R.mipmap.ic_launcher_foreground,
                BitmapFactory.Options()
            ).asImageBitmap()
        }
        Icon(
            bitmap = pictureResource,
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
        val context = LocalContext.current
        val request = remember {
            ImageRequest.Builder(context)
                .data(picturePath)
                .build()
        }
        SingleLaunchedEffect {
            context.imageLoader.enqueue(request)
        }
        AsyncImage(
            model = request,
            contentDescription = "Song picture",
            modifier = modifier
                .size(60.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = shape
                )
                .clip(shape),
            contentScale = ContentScale.Crop
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
