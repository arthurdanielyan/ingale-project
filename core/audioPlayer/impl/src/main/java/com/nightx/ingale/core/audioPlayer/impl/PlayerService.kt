package com.nightx.ingale.core.audioPlayer.impl

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import com.nightx.ingale.core.audioPlayer.impl.playerActionReceivers.PlayerActionChangeFavoriteStateReceiver
import com.nightx.ingale.core.audioPlayer.impl.playerActionReceivers.PlayerActionSkipToNextReceiver
import com.nightx.ingale.core.audioPlayer.impl.playerActionReceivers.PlayerActionSkipToPrevious
import com.nightx.ingale.core.audioPlayer.impl.playerActionReceivers.PlayerActionStopServiceReceiver
import com.nightx.ingale.core.audioPlayer.impl.playerActionReceivers.PlayerActionTogglePlaybackReceiver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import androidx.core.app.NotificationCompat as AndroidNotificationCompat
import androidx.media.app.NotificationCompat as MediaNotificationCompat
import com.nightx.ingale.resources.icon.R.mipmap as IconMipmap

class PlayerService : MediaBrowserServiceCompat() {

    companion object {
        const val MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID = "music_player"

        private const val MEDIA_NOTIFICATION_ID = 1
        private const val ROOT_ID = "connection_root_id"
        private const val STOP_SERVICE_ACTION = "stop_player_service_action"

        private const val STOP_ACTION_ID = "custom_action_stop_id"
        private const val FAVORITE_ACTION_ID = "custom_action_add_to_favorites_id"

        private const val MinSkipToPreviousTimestamp = 3000
        private const val SongFinishThreshold = 200
        private const val SeekPositionUpdateFrequency = 1000L
    }

    private val playerServiceCommunicator = this.get<PlayerServiceCommunicator>()
    private val scope = MainScope()

    private var mediaPlayer = MediaPlayer().apply {
        setOnCompletionListener {
            if (duration - currentPosition <= SongFinishThreshold) {
                skipToNext()
            }
        }
    }

    private lateinit var mediaSession: MediaSessionCompat

    private val customActions: List<CustomAction>
        get() = listOf(
            CustomAction(
                actionId = FAVORITE_ACTION_ID,
                actionName = "Add to favourites",
                actionIcon = R.drawable.ic_add_to_favorites
            ),
            CustomAction(
                actionId = STOP_ACTION_ID,
                actionName = "Stop player",
                actionIcon = R.drawable.ic_close_white
            ),
        )

    private val actionsListener = object : MediaSessionCompat.Callback() {

        override fun onCustomAction(action: String?, extras: Bundle?) {
            super.onCustomAction(action, extras)

            when(action) {
                STOP_ACTION_ID -> {
                    stopService()
                }
                FAVORITE_ACTION_ID -> {
                    changeFavoriteState()
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

    // Overriding methods /////////////////////////////////////////////////////////////////////////////
    override fun onBind(intent: Intent?): IBinder =
        object : Binder(), PlayerServiceActions {

            override fun initService() {
                this@PlayerService.initService()
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

            override fun changeFavoriteState() {
                this@PlayerService.changeFavoriteState()
            }

            override fun stopService() {
                this@PlayerService.stopService()
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                NotificationDismissedReceiver(),
                IntentFilter(STOP_SERVICE_ACTION),
                RECEIVER_NOT_EXPORTED
            )
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(
                    NotificationDismissedReceiver(),
                    IntentFilter(STOP_SERVICE_ACTION),
                )
            } else {
                registerReceiver(
                    NotificationDismissedReceiver(),
                    IntentFilter(STOP_SERVICE_ACTION),
                    RECEIVER_NOT_EXPORTED
                )
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initService()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        playerServiceCommunicator.seekPosition.update { mediaPlayer.currentPosition }
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

        val albumList = playerServiceCommunicator.songQueue
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaPlayer.seekTo(
                (mediaPlayer.duration * progress.coerceIn(0f, 1f)).roundToLong(),
                MediaPlayer.SEEK_PREVIOUS_SYNC
            )
        } else {
            mediaPlayer.seekTo((mediaPlayer.duration * progress.coerceIn(0f, 1f)).roundToInt())
        }
        updateState()
    }

    private fun initService() {
        onSongChanged(playerServiceCommunicator.seekPosition.value)
        trackSeekPosition()
    }

    private fun trackSeekPosition() {
        scope.launch {
            while (this.isActive) {
                playerServiceCommunicator.seekPosition.update {
                    mediaPlayer.currentPosition
                }
                delay(SeekPositionUpdateFrequency)
            }
        }
    }

    private fun pause() {
        mediaPlayer.pause()
        updateState()
        updateNotificationIfLowerTiramisu()
    }

    private fun play() {
        mediaPlayer.start()
        updateNotificationIfLowerTiramisu()
        updateState()
    }

    private fun skipToNext() {
        playerServiceCommunicator.pointer++
        onSongChanged()
    }

    private fun skipToPrevious() {
        if(mediaPlayer.currentPosition <= MinSkipToPreviousTimestamp) {
            playerServiceCommunicator.pointer--
            onSongChanged()
        } else {
            seekTo(0f)
            updateState(true)
        }
    }

    private fun changeFavoriteState() {
        // TODO: Not Implemented Yet
    }

    private fun stopService() {
        pause()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    // State management ////////////////////////////////////////////////////////////////////////////
    private fun updateState(isNewSong: Boolean = false) {
        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .apply {
                    customActions.forEach { action ->
                        addCustomAction(action.actionId, action.actionName, action.actionIcon)
                    }
                }
                .setState(
                    if (isNewSong || mediaPlayer.isPlaying) {
                        playerServiceCommunicator.isPlaying = true
                        PlaybackStateCompat.STATE_PLAYING
                    } else {
                        playerServiceCommunicator.isPlaying = false
                        PlaybackStateCompat.STATE_PAUSED
                    },
                    if(isNewSong){
                        0L
                    } else {
                        mediaPlayer.currentPosition.toLong()
                    },
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
        updateState(true)

        val metadataBuilder = MediaMetadataCompat.Builder()
            .putBitmap(
                MediaMetadataCompat.METADATA_KEY_ART,
                playerServiceCommunicator.currentSongBitmap
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE,
                playerServiceCommunicator.currentSong?.title.orEmpty()
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_TITLE,
                playerServiceCommunicator.currentSong?.title.orEmpty()
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
                playerServiceCommunicator.currentSong?.artist.orEmpty()
            )
            .putLong(
                MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER,
                playerServiceCommunicator.pointer.toLong()
            )
            .putLong(
                MediaMetadataCompat.METADATA_KEY_NUM_TRACKS,
                playerServiceCommunicator.songCount
            )
            .putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION,
                playerServiceCommunicator.currentSong?.duration ?: 0
            )
        mediaSession.setMetadata(metadataBuilder.build())
    }

    private fun onSongChanged(seekPosition: Int = 0) {
        applyNewSongData()
        updateNotification()
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(playerServiceCommunicator.currentSong?.path.orEmpty())
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            it.start()
            it.seekTo(seekPosition)
            updateNotificationIfLowerTiramisu()
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
            R.drawable.ic_pause
        else {
            R.drawable.ic_play
        }

        val notification =
            AndroidNotificationCompat.Builder(this, MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(IconMipmap.ic_launcher_foreground)
                .addAction(R.drawable.ic_arrow_previous, "Previous",
                    getPendingIntent(PlayerActionType.SkipToPrevious)
                ) // #0
                .addAction(togglePlayingIcon, "Pause",
                    getPendingIntent(PlayerActionType.TogglePlayback)
                ) // #1
                .addAction(R.drawable.ic_arrow_next, "Next",
                    getPendingIntent(PlayerActionType.SkipToNext)
                ) // #2
                .addAction(R.drawable.ic_close_white, "Stop playback",
                    getPendingIntent(PlayerActionType.StopService)
                )
                .setStyle(mediaStyle)
                .setContentTitle(playerServiceCommunicator.currentSong?.title.orEmpty())
                .setContentText(playerServiceCommunicator.currentSong?.artist.orEmpty())
                .setLargeIcon(playerServiceCommunicator.currentSongBitmap)
                .setDeleteIntent(onDismissedIntent)
                .build()

        startForeground(MEDIA_NOTIFICATION_ID, notification)
    }

    private fun getPendingIntent(action: PlayerActionType): PendingIntent =
        when(action) {
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

            PlayerActionType.ChangeFavoriteState ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionChangeFavoriteStateReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )

            PlayerActionType.StopService ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionStopServiceReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )

            PlayerActionType.PlaySong ->
                throw IllegalAccessException("PlayerService is already started")

            is PlayerActionType.SeekTo ->
                throw IllegalArgumentException("Seeking from notifications is handled automatically")
        }

    private val onDismissedIntent: PendingIntent
        get() = PendingIntent.getBroadcast(
            this,
            0,
            Intent(STOP_SERVICE_ACTION),
            PendingIntent.FLAG_IMMUTABLE
        )

    inner class NotificationDismissedReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            this@PlayerService.stopSelf()
        }
    }
}