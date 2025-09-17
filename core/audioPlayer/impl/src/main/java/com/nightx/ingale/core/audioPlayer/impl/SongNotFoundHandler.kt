package com.nightx.ingale.core.audioPlayer.impl

import com.nightx.ingale.bottomBarApi.SnackbarMessageSender
import com.nightx.ingale.core.domain.songs.usecase.RemoveSongUseCase
import com.nightx.ingale.resources.strings.R
import com.nightx.ingale.resources.strings.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SongNotFoundHandler(
    private val removeSongUseCase: RemoveSongUseCase,
    private val snackbarMessageSender: SnackbarMessageSender,
    private val applicationScope: CoroutineScope,
    private val stringProvider: StringProvider,
) {

    fun handleSongNotFound(
        songId: Long,
        songTitle: String,
    ) {
        applicationScope.launch {
            removeSongUseCase(songId)
        }
        snackbarMessageSender.sendSnackbarMessage(
            stringProvider.string(R.string.audio_deleted, songTitle)
        )
    }
}