package com.nightx.ingale.bottomBar.impl

import com.nightx.ingale.bottomBar.api.BottomBarController
import com.nightx.ingale.bottomBar.api.BottomBarEffect
import com.nightx.ingale.bottomBar.api.BottomBarEffectsHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class BottomBarControllerImpl : BottomBarController, BottomBarEffectsHolder {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _bottomBarEffect = Channel<BottomBarEffect>(
        capacity = Channel.UNLIMITED
    )
    override val bottomBarEffect = _bottomBarEffect.receiveAsFlow()

    override fun sendEffect(effect: BottomBarEffect) {
        scope.launch {
            _bottomBarEffect.send(effect)
        }
    }
}