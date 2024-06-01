package com.nightx.ingale.main_navigation.bottomBarControls

import com.nightx.ingale.mvi.asFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class BottomBarControllerImpl : BottomBarController, BottomBarEffectsHolder {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _bottomBarEffect = Channel<BottomBarEffect>(
        capacity = Channel.UNLIMITED
    )
    override val bottomBarEffect = _bottomBarEffect.asFlow(scope)

    override fun sendEffect(effect: BottomBarEffect) {
        scope.launch {
            _bottomBarEffect.send(effect)
        }
    }
}