package com.nightx.ingale.musicbar.view

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Velocity
import com.nightx.ingale.musicbar.musicBarControls.LocalMusicBarController
import com.nightx.ingale.musicbar.musicBarControls.MusicBarController
import com.nightx.ingale.musicbar.musicBarControls.MusicBarEffect

class NestedScrollForMusicBarNotification(
    private val musicBarController: MusicBarController,
    private val context: Context,
) : NestedScrollConnection {

    private companion object {
         const val SignificantScrollSize = 1 // in dp
    }

    private var startOffset = 0f

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        val consumedDp = consumed.y / this.context.resources.displayMetrics.density
        startOffset += consumedDp

        if(consumedDp < -SignificantScrollSize) {
            musicBarController.sendEffect(MusicBarEffect.CollapseMusicBar)
        } else if (consumedDp > SignificantScrollSize) {
            musicBarController.sendEffect(MusicBarEffect.ExpandMusicBar)
        }

        return super.onPostScroll(consumed, available, source)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        startOffset = 0f
        return super.onPostFling(consumed, available)
    }
}

@Composable
fun rememberNestedScrollForMusicBarNotification(): NestedScrollForMusicBarNotification {
    val musicBarController = LocalMusicBarController.current
    val context = LocalContext.current
    return remember {
        NestedScrollForMusicBarNotification(
            musicBarController = musicBarController,
            context = context
        )
    }
}