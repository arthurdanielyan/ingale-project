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
import com.nightx.ingale.core.audioPlayer.api.PlayerUiActions
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType.ACTION_CHANGE_FAVORITE_STATE
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType.ACTION_PLAY_SONG
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType.ACTION_SKIP_TO_NEXT
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType.ACTION_SKIP_TO_PREVIOUS
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType.ACTION_STOP_SERVICE
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType.ACTION_TOGGLE_PLAYBACK
import com.nightx.ingale.core.domainModel.DomainConstants
import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.resources.strings.StringProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.nightx.ingale.resources.icon.R.mipmap as IconMipmap
import com.nightx.ingale.resources.strings.R.string as Strings

class AudioPlayer(
    private val applicationContext: Context,
    private val stringProvider: StringProvider,
) : PlayerUiActions, CurrentSongInfoStateProvider {

    private val defaultSongBitmap: Bitmap
        get() = ContextCompat
            .getDrawable(applicationContext, IconMipmap.ic_launcher)!!.toBitmap()

    var isServiceRunning = false

    var isPlaying = false
        set(value) {
            field = value
            _currentSongInfo.update {
                it.copy(isPlaying = value)
            }
        }

    @Volatile
    var pointer = 0
        set(value) {
            field = if (value in songQueue.indices) value else
                if (value < 0) songQueue.lastIndex else 0
            _currentSongInfo.update { createMusicBarState() }
        }

    var seekPosition = 0

    var songQueue: List<Song> = emptyList()

    private val _currentSongInfo = MutableStateFlow(createMusicBarState())
    override val currentSongInfo = _currentSongInfo.asStateFlow()

    val currentSong: Song
        get() {
            return songQueue[pointer]
        }

    val songCount: Long
        get() = songQueue.size.toLong()

    val currentSongBitmap: Bitmap
        get() = getCurrentSongPreviewPath()?.let {
            BitmapFactory.decodeFile(it)
        } ?: defaultSongBitmap

    private fun initService() {
        if (isServiceRunning.not()) {
            applicationContext.startService(Intent(applicationContext, PlayerService::class.java))
        } else {
            fireServiceAction(ACTION_PLAY_SONG)
        }
    }

    override fun submitNewListAndPlay(songQueue: List<Song>, indexToPlay: Int) {
        require(indexToPlay in songQueue.indices) {
            "indexToPlay must be in the range of songQueue"
        }
        this.songQueue = songQueue
        pointer = indexToPlay
        seekPosition = 0
        initService()
    }

    override fun togglePlaying() {
        fireServiceAction(ACTION_TOGGLE_PLAYBACK)
    }

    override fun skipToNext() {
        seekPosition = 0
        fireServiceAction(ACTION_SKIP_TO_NEXT)
    }

    override fun skipToPrevious() {
        seekPosition = 0
        fireServiceAction(ACTION_SKIP_TO_PREVIOUS)
    }

    override fun seekTo(progress: Float) {

    }

    override fun changeFavoriteState() {
        fireServiceAction(ACTION_CHANGE_FAVORITE_STATE)
    }

    private fun fireServiceAction(action: PlayerActionType) {
        if (isServiceRunning.not()) {
            applicationContext.startService(Intent(applicationContext, PlayerService::class.java))
        }
        val serviceIntent = Intent(applicationContext, PlayerService::class.java)
            .putExtra(
                PlaybackActionService.EXTRA_ACTION_KEY,
                ACTION_SKIP_TO_NEXT.alias
            )
        applicationContext.bindService(serviceIntent, getConnection(action), 0)
    }

    private fun getCurrentSongPreviewPath(): String? =
        songQueue.getOrNull(pointer)?.picturePath

    private fun createMusicBarState(): CurrentSongInfo =
        CurrentSongInfo(
            currentSongPreviewPath = getCurrentSongPreviewPath(),
            isPlaying = isPlaying,
            songName = getCurrentSongOrNull()?.title,
            artistName =
            if (getCurrentSongOrNull()?.artist == DomainConstants.UNKNOWN_SONG_DATA_ID) {
                stringProvider.string(Strings.unknown_artist)
            } else {
                getCurrentSongOrNull()?.artist
            }
        )

    private fun getCurrentSongOrNull(): Song? = songQueue.getOrNull(pointer)

    private fun getConnection(action: PlayerActionType) =
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val playerServiceActions = service as? PlayerServiceActions ?: return

                when (action) {
                    ACTION_TOGGLE_PLAYBACK -> playerServiceActions.togglePlaying()
                    ACTION_SKIP_TO_NEXT -> playerServiceActions.skipToNext()
                    ACTION_SKIP_TO_PREVIOUS -> playerServiceActions.skipToPrevious()
                    ACTION_CHANGE_FAVORITE_STATE -> playerServiceActions.changeFavoriteState()
                    ACTION_STOP_SERVICE -> playerServiceActions.stopService()
                    ACTION_PLAY_SONG -> playerServiceActions.initService()
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) = Unit
        }
}