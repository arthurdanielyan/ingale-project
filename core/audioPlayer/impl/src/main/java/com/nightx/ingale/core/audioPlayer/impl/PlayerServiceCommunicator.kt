package com.nightx.ingale.core.audioPlayer.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.nightx.ingale.core.audioPlayer.api.CurrentPlaybackInfo
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfoStateProvider
import com.nightx.ingale.core.audioPlayer.api.PlaybackLoopMode
import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import com.nightx.ingale.core.domainModel.DomainConstants
import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.resources.icon.R
import com.nightx.ingale.resources.strings.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import com.nightx.ingale.resources.strings.R.string as Strings

class PlayerServiceCommunicator(
    private val applicationContext: Context,
    private val stringProvider: StringProvider,
    applicationScope: CoroutineScope,
) : PlaybackUserActions, CurrentSongInfoStateProvider {

    private var playlist: List<Song> = emptyList()
    val playbackLoopMode = MutableStateFlow(PlaybackLoopMode.PlaylistLoop)
    var currentQueue = emptyList<Song>()
        private set
    private var currentSongIndex = MutableStateFlow(0) // points to a song in playbackOrder

    private val playerServiceConnection = PlayerServiceConnection(
        applicationContext = applicationContext,
        applicationScope = applicationScope
    )

    val seekPosition = MutableStateFlow(0) // updates from service
    var isPlaying = MutableStateFlow(false) // updates from service

    override val currentPlaybackInfo = combine(
        currentSongIndex,
        seekPosition,
        isPlaying,
        playbackLoopMode
    ) { _, seekPosition, isPlaying, playbackLoopMode ->
        val currentSong = getCurrentSong() ?: return@combine null
        CurrentPlaybackInfo(
            currentSongPreviewPath = currentSong.picturePath,
            isPlaying = isPlaying,
            songName = currentSong.title,
            artistName = if (currentSong.artist == DomainConstants.UNKNOWN_SONG_DATA_ID) {
                stringProvider.string(Strings.unknown_artist)
            } else {
                currentSong.artist
            },
            seekPercentage = if (currentSong.duration == 0L) {
                0f
            } else {
                seekPosition.toFloat() / currentSong.duration
            },
            loopMode = playbackLoopMode,
        )
    }.stateIn(applicationScope, SharingStarted.Eagerly, null)

    private fun handleNewPlaylistAndPlay(playlist: List<Song>, indexToPlay: Int) {
        require(indexToPlay in playlist.indices) {
            "indexToPlay must be in the range of playlist"
        }
        this.playlist = playlist
        when (playbackLoopMode.value) {
            PlaybackLoopMode.PlaylistLoop -> {
                currentQueue = playlist
                currentSongIndex.update { indexToPlay }
            }

            PlaybackLoopMode.Shuffle -> {
                val songToPlay = playlist[indexToPlay]
                currentQueue = playlist.toMutableList().apply {
                    remove(songToPlay)
                    shuffle()
                    add(0, songToPlay)
                }
                currentSongIndex.update { 0 }
            }

            PlaybackLoopMode.Single -> {
                currentQueue = playlist
                currentSongIndex.update { indexToPlay }
            }
        }
        seekPosition.update { 0 }
        playerServiceConnection.withConnection {
            prepareNewSong()
        }
    }

    private fun handleChangePlaybackLoopMode(loopMode: PlaybackLoopMode) {
        when (loopMode) {
            PlaybackLoopMode.PlaylistLoop -> {
                val currentSong = getCurrentSong()
                currentQueue = playlist
                currentSongIndex.update {
                    currentQueue.indexOfFirst { it == currentSong }.coerceAtLeast(0)
                }
            }

            PlaybackLoopMode.Shuffle -> {
                val currentSong = getCurrentSong()
                currentQueue = playlist.toMutableList().apply {
                    remove(currentSong)
                    shuffle()
                    currentSong?.let { add(0, it) }
                }
                currentSongIndex.update { 0 }
            }

            PlaybackLoopMode.Single -> Unit
        }
        playbackLoopMode.update { loopMode }
    }

    fun handleSkipToNext() { // called from service
        if (currentQueue.isEmpty()) return

        playNext()
    }

    fun handleSkipToPrevious() { // called from service
        if (currentQueue.isEmpty()) return

        seekPosition.update { 0 }
        if (currentSongIndex.value - 1 < 0) {
            currentSongIndex.update { currentQueue.lastIndex }
        } else {
            currentSongIndex.update { it - 1 }
        }
        playerServiceConnection.withConnection {
            prepareNewSong()
        }
    }

    fun handleSongCompletion() {
        if (currentQueue.isEmpty()) return

        if (playbackLoopMode.value == PlaybackLoopMode.Single) {
            seekPosition.update { 0 }
            playerServiceConnection.withConnection {
                prepareNewSong()
            }
        } else {
            playNext()
        }
    }

    fun getTrackNumber(): Int {
        return currentSongIndex.value
    }

    fun getCurrentSong(): Song? {
        return currentQueue.getOrNull(currentSongIndex.value)
    }

    fun unbindService() {
        playerServiceConnection.unbind()
    }

    fun getCurrentSongBitmap(): Bitmap {
        return getCurrentSong()?.let {
            BitmapFactory.decodeFile(it.picturePath)
        } ?: ContextCompat
            .getDrawable(applicationContext, R.mipmap.ic_launcher)!!.toBitmap()
    }

    private fun playNext() {
        if (currentSongIndex.value + 1 > currentQueue.lastIndex) {
            currentSongIndex.update { 0 }
        } else {
            currentSongIndex.update { it + 1 }
        }
        playerServiceConnection.withConnection {
            prepareNewSong()
        }
    }

    override fun submitNewPlaylistAndPlay(playlist: List<Song>, indexToPlay: Int) {
        handleNewPlaylistAndPlay(playlist, indexToPlay)
    }

    override fun togglePlaying() {
        playerServiceConnection.withConnection {
            togglePlaying()
        }
    }

    override fun skipToNext() {
        playerServiceConnection.withConnection {
            skipToNext()
        }
    }

    override fun skipToPrevious() {
        playerServiceConnection.withConnection {
            skipToPrevious()
        }
    }

    override fun seekTo(percentage: Float) {
        playerServiceConnection.withConnection {
            seekTo(percentage)
        }
    }

    override fun changeFavoriteState() {
        // TODO: Not implemented yet
    }

    override fun changePlaybackLoopMode(loopMode: PlaybackLoopMode) {
        handleChangePlaybackLoopMode(loopMode)
    }
}