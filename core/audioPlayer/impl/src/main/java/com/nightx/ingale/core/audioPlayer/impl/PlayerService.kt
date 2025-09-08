package com.nightx.ingale.core.audioPlayer.impl

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.KeyEvent
import androidx.annotation.FloatRange
import androidx.media.MediaBrowserServiceCompat
import com.nightx.ingale.core.audioPlayer.api.PlaybackLoopMode
import com.nightx.ingale.core.audioPlayer.impl.playerActionReceivers.PlayerActionChangePlaybackLoopModeReceiver
import com.nightx.ingale.core.audioPlayer.impl.playerActionReceivers.PlayerActionSkipToNextReceiver
import com.nightx.ingale.core.audioPlayer.impl.playerActionReceivers.PlayerActionSkipToPrevious
import com.nightx.ingale.core.audioPlayer.impl.playerActionReceivers.PlayerActionStopServiceReceiver
import com.nightx.ingale.core.audioPlayer.impl.playerActionReceivers.PlayerActionTogglePlaybackReceiver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import androidx.core.app.NotificationCompat as AndroidNotificationCompat
import androidx.media.app.NotificationCompat as MediaNotificationCompat
import com.nightx.ingale.resources.icon.R.mipmap as IconMipmap
import com.nightx.ingale.resources.playbackActions.R.drawable as PlaybackDrawables

class PlayerService : MediaBrowserServiceCompat() {

    companion object {
        const val MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID = "music_player"

        private const val MEDIA_NOTIFICATION_ID = 1
        private const val ROOT_ID = "connection_root_id"

        private const val STOP_ACTION_ID = "custom_action_stop_id"
        private const val CHANGE_LOOP_MODE_ACTION_ID = "custom_action_change_playback_loop_mode_id"

        private const val MinSkipToPreviousTimestamp = 3000
        private const val SongFinishThreshold = 200
        private const val SeekPositionUpdateFrequency = 1000L
    }

    private val playerServiceCommunicator = this.get<PlayerServiceCommunicator>()
    private val scope = MainScope()

    private var mediaPlayer = MediaPlayer().apply {
        setOnCompletionListener {
            if (duration <= 0) return@setOnCompletionListener
            if (duration - currentPosition <= SongFinishThreshold) {
                onSongCompleted()
            }
        }
    }
    private var isMediaPlayerPrepared = false

    private lateinit var mediaSession: MediaSessionCompat

    private val customActions: List<CustomAction>
        get() = listOf(
            CustomAction(
                actionId = CHANGE_LOOP_MODE_ACTION_ID,
                actionName = "Change loop mode",
                actionIcon = when (playerServiceCommunicator.playbackLoopMode.value) {
                    PlaybackLoopMode.PlaylistLoop -> PlaybackDrawables.ic_playlist_repeat
                    PlaybackLoopMode.Shuffle -> PlaybackDrawables.ic_playlist_shuffle
                    PlaybackLoopMode.Single -> PlaybackDrawables.ic_repeat_single
                }
            ),
            CustomAction(
                actionId = STOP_ACTION_ID,
                actionName = "Stop player",
                actionIcon = PlaybackDrawables.ic_close_white
            ),
        )

    private val actionsListener = object : MediaSessionCompat.Callback() {

        override fun onCustomAction(action: String?, extras: Bundle?) {
            super.onCustomAction(action, extras)

            when (action) {
                STOP_ACTION_ID -> {
                    stopService()
                }

                CHANGE_LOOP_MODE_ACTION_ID -> {
                    this@PlayerService.changePlaybackLoopMode()
                }
            }
        }

        override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
            val ke = mediaButtonEvent?.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
                ?: return false

            if (ke.action == KeyEvent.ACTION_DOWN) {
                if (ke.keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS)
                    skipToPrevious()

                if (ke.keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE)
                    pause()

                if (ke.keyCode == KeyEvent.KEYCODE_MEDIA_PLAY)
                    play()

                if (ke.keyCode == KeyEvent.KEYCODE_MEDIA_NEXT)
                    skipToNext()

                return true // prevents onSkipToNext and onSkipToPrevious from being called
            }

            return super.onMediaButtonEvent(mediaButtonEvent)
        }

        override fun onPause() {
            super.onPause()
            pause()
        }

        override fun onPlay() {
            super.onPlay()
            play()
        }

        override fun onSeekTo(pos: Long) {
            super.onSeekTo(pos)
            seekTo(pos / mediaPlayer.duration.toFloat())
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
            skipToNext()
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            skipToPrevious()
        }
    }

    init {
        trackSeekPosition()
        observePlaybackLoopMode()
    }

    // Overriding methods /////////////////////////////////////////////////////////////////////////////
    override fun onBind(intent: Intent?): IBinder =
        object : Binder(), PlayerServiceActions {

            override fun prepareNewSong() {
                this@PlayerService.prepareNewSong()
            }

            override fun togglePlaying() {
                if (mediaPlayer.isPlaying) {
                    this@PlayerService.pause()
                } else {
                    this@PlayerService.play()
                }
            }

            override fun skipToNext() {
                this@PlayerService.skipToNext()
            }

            override fun skipToPrevious() {
                this@PlayerService.skipToPrevious()
            }

            override fun seekTo(progress: Float) {
                this@PlayerService.seekTo(progress)
            }

            override fun stopService() {
                this@PlayerService.stopService()
            }

            override fun changePlaybackLoopMode() {
                this@PlayerService.changePlaybackLoopMode()
            }
        }

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(
            this,
            "PlayerService",
        ).apply {
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or
                        MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            setCallback(actionsListener)
            isActive = true
        }
        sessionToken = mediaSession.sessionToken
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        playerServiceCommunicator.seekPosition.update {
            mediaPlayer.currentPosition
        }
        mediaSession.release()
        mediaPlayer.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?,
    ): BrowserRoot =
        BrowserRoot(ROOT_ID, null)

    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowserCompat.MediaItem>>,
    ) {
        val items = mutableListOf<MediaBrowserCompat.MediaItem>()

        val albumList = playerServiceCommunicator.currentQueue
        for (it in albumList) {
            val descriptionBuilder = MediaDescriptionCompat.Builder()
                .setTitle(it.title)
            items.add(
                MediaBrowserCompat.MediaItem(
                    descriptionBuilder.build(),
                    MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                )
            )
        }

        result.sendResult(items)
    }

    // Player actions ///////////////////////////////////////////////////////////////////////////////////
    private fun seekTo(@FloatRange(from = 0.0, to = 1.0) progress: Float) {
        val seekPosition = (mediaPlayer.duration * progress.coerceIn(0f, 1f))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaPlayer.seekTo(
                seekPosition.roundToLong(),
                MediaPlayer.SEEK_PREVIOUS_SYNC
            )
        } else {
            mediaPlayer.seekTo(seekPosition.roundToInt())
        }
        playerServiceCommunicator.seekPosition.update { seekPosition.roundToInt() }
        updateMediaSessionState()
    }

    private fun trackSeekPosition() {
        scope.launch {
            while (this.isActive) {
                if (isMediaPlayerPrepared) {
                    playerServiceCommunicator.seekPosition.update {
                        mediaPlayer.currentPosition
                    }
                }
                delay(SeekPositionUpdateFrequency)
            }
        }
    }

    private fun observePlaybackLoopMode() {
        scope.launch {
            playerServiceCommunicator.playbackLoopMode.collectLatest {
                updateMediaSessionState()
            }
        }
    }

    private fun pause() {
        mediaPlayer.pause()
        updateMediaSessionState()
        updateNotificationIfLowerTiramisu()
    }

    private fun play() {
        if (isMediaPlayerPrepared.not()) {
            prepareNewSong(playerServiceCommunicator.seekPosition.value)
        } else {
            mediaPlayer.start()
            updateNotificationIfLowerTiramisu()
            updateMediaSessionState()
        }
    }

    private fun skipToNext() {
        playerServiceCommunicator.handleSkipToNext()
    }

    private fun skipToPrevious() {
        if (mediaPlayer.currentPosition <= MinSkipToPreviousTimestamp) {
            playerServiceCommunicator.handleSkipToPrevious()
        } else {
            seekTo(0f)
            updateMediaSessionState(true)
        }
    }

    private fun onSongCompleted() {
        playerServiceCommunicator.handleSongCompletion()
    }

    private fun changePlaybackLoopMode() {
        playerServiceCommunicator.changePlaybackLoopMode(
            loopMode = playerServiceCommunicator.playbackLoopMode.value.next
        )
    }

    private fun stopService() {
        pause()
        stopForeground(STOP_FOREGROUND_REMOVE)
        playerServiceCommunicator.unbindService()
        stopSelf()
    }

    // State management ////////////////////////////////////////////////////////////////////////////
    private fun updateMediaSessionState(isNewSong: Boolean = false) {
        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .apply {
                    customActions.forEach { action ->
                        addCustomAction(action.actionId, action.actionName, action.actionIcon)
                    }
                }
                .setState(
                    if (isNewSong || mediaPlayer.isPlaying) {
                        playerServiceCommunicator.isPlaying.update { true }
                        PlaybackStateCompat.STATE_PLAYING
                    } else {
                        playerServiceCommunicator.isPlaying.update { false }
                        PlaybackStateCompat.STATE_PAUSED
                    },
                    playerServiceCommunicator.seekPosition.value.toLong(),
                    1f
                )
                .setActions(
                    PlaybackStateCompat.ACTION_SEEK_TO
                            or PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                ).build()
        )
    }

    private fun applyNewSongData() {
        updateMediaSessionState(true)
        val currentSong = playerServiceCommunicator.getCurrentSong()

        val metadataBuilder = MediaMetadataCompat.Builder()
            .putBitmap(
                MediaMetadataCompat.METADATA_KEY_ART,
                playerServiceCommunicator.getCurrentSongBitmap()
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE,
                currentSong?.title.orEmpty()
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_TITLE,
                currentSong?.title.orEmpty()
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
                currentSong?.artist
            )
            .putLong(
                MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER,
                playerServiceCommunicator.getTrackNumber().toLong()
            )
            .putLong(
                MediaMetadataCompat.METADATA_KEY_NUM_TRACKS,
                playerServiceCommunicator.currentQueue.size.toLong()
            )
            .putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION,
                currentSong?.duration ?: 0
            )
        mediaSession.setMetadata(metadataBuilder.build())
    }

    private fun prepareNewSong(
        seekPosition: Int = 0
    ) {
        updateNotification()
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(playerServiceCommunicator.getCurrentSong()?.path.orEmpty())
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            isMediaPlayerPrepared = true
            it.start()
            it.seekTo(seekPosition)
            playerServiceCommunicator.seekPosition.update { seekPosition }
            updateNotificationIfLowerTiramisu()
            applyNewSongData()
        }
    }

    private fun updateNotificationIfLowerTiramisu() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            updateNotification()
    }

    private fun updateNotification() {
        val mediaStyle = MediaNotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2)
            .setMediaSession(mediaSession.sessionToken)


        val togglePlayingIcon = if (mediaPlayer.isPlaying)
            PlaybackDrawables.ic_pause
        else {
            PlaybackDrawables.ic_play
        }

        val currentSong = playerServiceCommunicator.getCurrentSong()

        val notification =
            AndroidNotificationCompat.Builder(this, MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(IconMipmap.ic_launcher_foreground)
                .addAction(
                    PlaybackDrawables.ic_arrow_previous, "Previous",
                    getPendingIntent(PlayerActionType.SkipToPrevious)
                ) // #0
                .addAction(
                    togglePlayingIcon, "Pause",
                    getPendingIntent(PlayerActionType.TogglePlayback)
                ) // #1
                .addAction(
                    PlaybackDrawables.ic_arrow_next, "Next",
                    getPendingIntent(PlayerActionType.SkipToNext)
                ) // #2
                .addAction(
                    PlaybackDrawables.ic_close_white, "Stop playback",
                    getPendingIntent(PlayerActionType.StopService)
                )
                .setStyle(mediaStyle)
                .setContentTitle(currentSong?.title.orEmpty())
                .setContentText(currentSong?.artist.orEmpty())
                .setLargeIcon(playerServiceCommunicator.getCurrentSongBitmap())
                .build()

        startForeground(MEDIA_NOTIFICATION_ID, notification)
    }

    private fun getPendingIntent(action: PlayerActionType): PendingIntent =
        when (action) {
            PlayerActionType.TogglePlayback ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionTogglePlaybackReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )

            PlayerActionType.SkipToNext ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionSkipToNextReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )

            PlayerActionType.SkipToPrevious ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionSkipToPrevious::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )

            PlayerActionType.ChangePlaybackLoopMode ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionChangePlaybackLoopModeReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )

            PlayerActionType.StopService ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionStopServiceReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
        }
}