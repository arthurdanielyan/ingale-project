package com.nightx.ingale.globalPlaybackPresentation.musicBar.impl

import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarController
import com.nightx.ingale.globalPlaybackPresentation.musicBar.api.MusicBarEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class MusicBarControllerImpl(
    private val applicationScope: CoroutineScope
) : MusicBarController {

    private val _musicBarEffect = Channel<MusicBarEffect>(
        capacity = Channel.UNLIMITED
    )
    val musicBarEffect = _musicBarEffect.receiveAsFlow()

    override fun sendEffect(effect: MusicBarEffect) {
        applicationScope.launch {
            _musicBarEffect.send(effect)
        }
    }
}