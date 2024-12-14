package com.nightx.ingale.globalPlaybackPresentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.nightx.ingale.globalPlaybackPresentation.musicBarController.LocalMusicBarController
import com.nightx.ingale.globalPlaybackPresentation.musicBarController.MusicBarController
import com.nightx.ingale.globalPlaybackPresentation.musicBarController.MusicBarEffect

class NestedScrollForMusicBarNotification(
    private val musicBarController: MusicBarController,
    private val density: Density,
) : NestedScrollConnection {

    private companion object {
        val SignificantScrollSize = 0.5.dp
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        val consumedDp = density.run { consumed.y.toDp() }

        if(consumedDp < -SignificantScrollSize) {
            musicBarController.sendEffect(MusicBarEffect.CollapseMusicBar)
        } else if (consumedDp > SignificantScrollSize) {
            musicBarController.sendEffect(MusicBarEffect.ExpandMusicBar)
        }

        return super.onPostScroll(consumed, available, source)
    }
}

@Composable
fun rememberNestedScrollForMusicBarNotification(): NestedScrollForMusicBarNotification {
    val musicBarController = LocalMusicBarController.current
    val density = LocalDensity.current
    return remember {
        NestedScrollForMusicBarNotification(
            musicBarController = musicBarController,
            density = density
        )
    }
}