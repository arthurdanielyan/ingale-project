package com.nightx.ingale.musicbar.musicBarControls

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class MusicBarControllerImpl : MusicBarController, MusicBarEffectsHolder {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _musicBarEffect = Channel<MusicBarEffect>(
        capacity = Channel.UNLIMITED
    )
    override val musicBarEffect = _musicBarEffect.receiveAsFlow()

    override fun sendEffect(effect: MusicBarEffect) {
        scope.launch {
            _musicBarEffect.send(effect)
        }
    }
}