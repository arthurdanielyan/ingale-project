package com.nightx.ingale.core.presentation.view

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import com.nightx.ingale.main_navigation.bottom_bar_controls.BottomBarController
import com.nightx.ingale.main_navigation.bottom_bar_controls.BottomBarEffect
import org.koin.java.KoinJavaComponent.inject

class NestedScrollForMusicBarNotification : NestedScrollConnection {

    companion object {
        private const val SignificantScrollSize = 20 // in dp
    }

    private val bottomBarController by inject<BottomBarController>(
        BottomBarController::class.java
    )
    private val applicationContext by inject<Context>(Context::class.java)

    private var startOffset = 0f


    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        /*
        * 1 dp - density px
        * ? dp - x px
        * */
        val consumedDp = consumed.y / applicationContext.resources.displayMetrics.density
        startOffset += consumedDp
        Log.d("myLogs", "consumed: $consumedDp, total: $startOffset")

        if (startOffset < -SignificantScrollSize) {
            bottomBarController.sendEffect(BottomBarEffect.CollapseMusicBar)
        } else if (startOffset > SignificantScrollSize) {
            bottomBarController.sendEffect(BottomBarEffect.ExpandMusicBar)
        }

        return super.onPostScroll(consumed, available, source)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        startOffset = 0f
        return super.onPostFling(consumed, available)
    }
}

@Composable
fun rememberNestedScrollForMusicBarNotification() = remember {
    NestedScrollForMusicBarNotification()
}