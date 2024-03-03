package com.nightx.ingale.feature_local.local_core.presentation.song_item

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import com.nightx.ingale.R
import com.nightx.ingale.ui.theme.IngaleTheme

@Composable
fun SongIcon(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    picture: Bitmap?,
) {
    if (picture == null) {
        val context = LocalContext.current
        val pictureR = remember {
            BitmapFactory.decodeResource(
                context.resources,
                R.mipmap.ic_launcher_foreground,
                BitmapFactory.Options()
            ).asImageBitmap()
        }
        Icon(
            bitmap = pictureR,
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
        val pictureR = remember {
            picture
        }
        Image(
            bitmap = pictureR.asImageBitmap(),
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
        SongIcon(picture = null)
    }
}

@Preview
@Composable
private fun SongIconPreviewLight() {
    IngaleTheme {
        SongIcon(picture = null)
    }
}
