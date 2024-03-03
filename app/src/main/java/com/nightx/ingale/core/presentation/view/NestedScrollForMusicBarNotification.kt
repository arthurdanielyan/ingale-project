package com.nightx.ingale.core.presentation.view

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import com.nightx.ingale.main_navigation.bottom_bar_controls.BottomBarController
import com.nightx.ingale.main_navigation.bottom_bar_controls.BottomBarEffect
import org.koin.java.KoinJavaComponent

object NestedScrollForMusicBarNotification : NestedScrollConnection {

    private val bottomBarController by KoinJavaComponent.inject<BottomBarController>(
        BottomBarController::class.java
    )
    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        if (consumed.y < -1f) {
            bottomBarController.sendEffect(BottomBarEffect.CollapseMusicInfo)
        } else if (consumed.y > 1f) {
            bottomBarController.sendEffect(BottomBarEffect.ExpandMusicInfo)
        }

        return super.onPostScroll(consumed, available, source)
    }
}