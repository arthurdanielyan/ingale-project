package com.nightx.ingale.core.audio_player

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
import com.nightx.ingale.IngaleApplication.Companion.MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID
import com.nightx.ingale.R
import com.nightx.ingale.core.audio_player.PlaybackActionService.Companion.EXTRA_ACTION_CHANGE_FAVORITE_STATE
import com.nightx.ingale.core.audio_player.PlaybackActionService.Companion.EXTRA_ACTION_SKIP_TO_NEXT
import com.nightx.ingale.core.audio_player.PlaybackActionService.Companion.EXTRA_ACTION_SKIP_TO_PREVIOUS
import com.nightx.ingale.core.audio_player.PlaybackActionService.Companion.EXTRA_ACTION_STOP_SERVICE
import com.nightx.ingale.core.audio_player.PlaybackActionService.Companion.EXTRA_ACTION_TOGGLE_PLAYBACK
import com.nightx.ingale.core.audio_player.player_action_receivers.PlayerActionChangeFavoriteStateReceiver
import com.nightx.ingale.core.audio_player.player_action_receivers.PlayerActionSkipToNextReceiver
import com.nightx.ingale.core.audio_player.player_action_receivers.PlayerActionSkipToPrevious
import com.nightx.ingale.core.audio_player.player_action_receivers.PlayerActionStopServiceReceiver
import com.nightx.ingale.core.audio_player.player_action_receivers.PlayerActionTogglePlaybackReceiver
import kotlin.math.roundToInt
import androidx.core.app.NotificationCompat as AndroidNotificationCompat
import androidx.media.app.NotificationCompat as MediaNotificationCompat

class PlayerService : MediaBrowserServiceCompat() {

    companion object {
        private const val MEDIA_NOTIFICATION_ID = 1
        private const val ROOT_ID = "connection_root_id"
        private const val STOP_SERVICE_ACTION = "stop_player_service_action"

        private const val STOP_ACTION_ID = "custom_action_stop_id"
        private const val FAVORITE_ACTION_ID = "custom_action_add_to_favorites_id"

        private const val MinSkipToPreviousTimestamp = 3000
    }

    private var mediaPlayer = MediaPlayer().apply {
        setOnCompletionListener {
            skipToNext()
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                mediaPlayer.seekTo(pos, MediaPlayer.SEEK_PREVIOUS_SYNC)
            else
                mediaPlayer.seekTo(pos.toInt())

            updateState()
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
        object : Binder(), PlayerActions {
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
            registerReceiver(NotificationDismissedReceiver(), IntentFilter(STOP_SERVICE_ACTION))
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onSongChanged()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.release()
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

        val albumList = AudioPlayer.songQueue
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
    private fun seekTo(@FloatRange(0.0, 1.0) progress: Float) {
        mediaPlayer.seekTo((mediaPlayer.duration * progress.coerceIn(0f, 1f)).roundToInt())
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
        AudioPlayer.pointer++
        onSongChanged()
    }

    private fun skipToPrevious() {
        if(mediaPlayer.currentPosition <= MinSkipToPreviousTimestamp) {
            AudioPlayer.pointer--
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
                        PlaybackStateCompat.STATE_PLAYING
                    } else {
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
                AudioPlayer.currentSongBitmap
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE,
                AudioPlayer.currentSong.title
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_TITLE,
                AudioPlayer.currentSong.title
            )
            .putString(
                MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
                AudioPlayer.currentSong.artist
            )
            .putLong(
                MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER,
                AudioPlayer.currentSongNumber
            )
            .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, AudioPlayer.songCount)
            .putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION,
                AudioPlayer.currentSong.duration
            )
        mediaSession.setMetadata(metadataBuilder.build())
    }

    private fun onSongChanged() {
        applyNewSongData()
        updateNotification()
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(AudioPlayer.currentSong.path)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            it.start()
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
            R.drawable.pause
        else {
            R.drawable.play
        }

        val notification =
            AndroidNotificationCompat.Builder(this, MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .addAction(R.drawable.arrow_previous, "Previous",
                    getPendingIntent(EXTRA_ACTION_SKIP_TO_PREVIOUS)
                ) // #0
                .addAction(togglePlayingIcon, "Pause",
                    getPendingIntent(EXTRA_ACTION_TOGGLE_PLAYBACK)
                ) // #1
                .addAction(R.drawable.arrow_next, "Next",
                    getPendingIntent(EXTRA_ACTION_SKIP_TO_NEXT)
                ) // #2
                .addAction(R.drawable.ic_close_white, "Stop playback",
                    getPendingIntent(EXTRA_ACTION_STOP_SERVICE)
                )
                .setStyle(mediaStyle)
                .setContentTitle(AudioPlayer.currentSong.title)
                .setContentText(AudioPlayer.currentSong.artist)
                .setLargeIcon(AudioPlayer.currentSongBitmap)
                .setDeleteIntent(onDismissedIntent)
                .build()

        startForeground(MEDIA_NOTIFICATION_ID, notification)
    }

    private fun getPendingIntent(action: String): PendingIntent =
        when(action) {
            EXTRA_ACTION_TOGGLE_PLAYBACK ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionTogglePlaybackReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            EXTRA_ACTION_SKIP_TO_NEXT ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionSkipToNextReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            EXTRA_ACTION_SKIP_TO_PREVIOUS ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionSkipToPrevious::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            EXTRA_ACTION_CHANGE_FAVORITE_STATE ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionChangeFavoriteStateReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            EXTRA_ACTION_STOP_SERVICE ->
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, PlayerActionStopServiceReceiver::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            else -> throw IllegalArgumentException("Specified action $action doesn't exist")
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