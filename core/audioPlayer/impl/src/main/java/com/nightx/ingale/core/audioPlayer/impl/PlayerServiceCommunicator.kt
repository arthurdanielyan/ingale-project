package com.nightx.ingale.core.audioPlayer.impl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfo
import com.nightx.ingale.core.audioPlayer.api.CurrentSongInfoStateProvider
import com.nightx.ingale.core.audioPlayer.api.PlaybackUserActions
import com.nightx.ingale.core.domainModel.DomainConstants
import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.resources.strings.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import com.nightx.ingale.resources.icon.R.mipmap as IconMipmap
import com.nightx.ingale.resources.strings.R.string as Strings

class PlayerServiceCommunicator(
    private val applicationContext: Context,
    private val stringProvider: StringProvider,
    applicationScope: CoroutineScope,
) : PlaybackUserActions, CurrentSongInfoStateProvider {

    private val defaultSongBitmap: Bitmap
        get() = ContextCompat
            .getDrawable(applicationContext, IconMipmap.ic_launcher)!!.toBitmap()

    var isPlaying = false
        set(value) {
            field = value
            _currentSongInfo.update {
                it?.copy(isPlaying = value)
            }
        }

    var songQueue: List<Song> = emptyList()

    var seekPosition = MutableStateFlow(0)

    private val _currentSongInfo = MutableStateFlow<CurrentSongInfo?>(null)
    private val seekPercentage = seekPosition.map { seekPosition ->
        currentSong?.duration?.toFloat()?.let {
            seekPosition.toFloat() / it
        } ?: 0f
    }.stateIn(applicationScope, SharingStarted.Eagerly, 0f)

    override val currentSongInfo = combine(
        _currentSongInfo,
        seekPercentage
    ) { currentSongInfo, seekPercentage ->
        currentSongInfo?.copy(seekPercentage = seekPercentage)
    }.stateIn(applicationScope, SharingStarted.WhileSubscribed(), null)

    @Volatile
    var pointer = 0
        set(value) {
            field = if (value in songQueue.indices) value else
                if (value < 0) songQueue.lastIndex else 0
            _currentSongInfo.update { createMusicBarState() }
        }

    val currentSong: Song?
        get() {
            return songQueue.getOrNull(pointer)
        }

    val songCount: Long
        get() = songQueue.size.toLong()

    val currentSongBitmap: Bitmap
        get() = getCurrentSongPreviewPath()?.let {
            BitmapFactory.decodeFile(it)
        } ?: defaultSongBitmap

    private fun initService() {
        fireServiceAction(PlayerActionType.PlaySong)
    }

    override fun submitNewListAndPlay(songQueue: List<Song>, indexToPlay: Int) {
        require(indexToPlay in songQueue.indices) {
            "indexToPlay must be in the range of songQueue"
        }
        this.songQueue = songQueue
        pointer = indexToPlay
        seekPosition.update { 0 }
        initService()
    }

    override fun togglePlaying() {
        fireServiceAction(PlayerActionType.TogglePlayback)
    }

    override fun skipToNext() {
        seekPosition.update { 0 }
        fireServiceAction(PlayerActionType.SkipToNext)
    }

    override fun skipToPrevious() {
        seekPosition.update { 0 }
        fireServiceAction(PlayerActionType.SkipToPrevious)
    }

    override fun seekTo(percentage: Float) {
        fireServiceAction(
            PlayerActionType.SeekTo(percentage)
        )
    }

    override fun changeFavoriteState() {
        fireServiceAction(PlayerActionType.ChangeFavoriteState)
    }

    private fun getCurrentSongPreviewPath(): String? =
        songQueue.getOrNull(pointer)?.picturePath

    private fun createMusicBarState(): CurrentSongInfo =
        CurrentSongInfo(
            currentSongPreviewPath = getCurrentSongPreviewPath(),
            isPlaying = isPlaying,
            songName = getCurrentSongOrNull()?.title.orEmpty(),
            artistName =
            if (getCurrentSongOrNull()?.artist == DomainConstants.UNKNOWN_SONG_DATA_ID) {
                stringProvider.string(Strings.unknown_artist)
            } else {
                getCurrentSongOrNull()?.artist ?: stringProvider.string(Strings.unknown_artist)
            },
            seekPercentage = seekPercentage.value
        )

    private fun getCurrentSongOrNull(): Song? = songQueue.getOrNull(pointer)

    private fun fireServiceAction(action: PlayerActionType) {
        val serviceIntent = Intent(applicationContext, PlayerService::class.java)
        applicationContext.bindService(
            serviceIntent,
            getConnection(action),
            Context.BIND_AUTO_CREATE
        )
    }

    private fun getConnection(action: PlayerActionType) =
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val playerServiceActions = service as? PlayerServiceActions ?: return

                when (action) {
                    PlayerActionType.TogglePlayback -> playerServiceActions.togglePlaying()
                    PlayerActionType.SkipToNext -> playerServiceActions.skipToNext()
                    PlayerActionType.SkipToPrevious -> playerServiceActions.skipToPrevious()
                    PlayerActionType.ChangeFavoriteState -> playerServiceActions.changeFavoriteState()
                    PlayerActionType.StopService -> playerServiceActions.stopService()
                    PlayerActionType.PlaySong -> playerServiceActions.initService()
                    is PlayerActionType.SeekTo -> playerServiceActions.seekTo(action.percentage)
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) = Unit
        }
}