package com.nightx.ingale.core.audio_player

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
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
import androidx.media.app.NotificationCompat
import com.nightx.ingale.IngaleApplication.Companion.MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID
import com.nightx.ingale.R
import com.nightx.ingale.core.audio_player.actions_receivers.ReceiverSkipToNext
import com.nightx.ingale.core.audio_player.actions_receivers.ReceiverSkipToPrevious
import com.nightx.ingale.core.audio_player.actions_receivers.ReceiverTogglePlaying
import kotlin.math.roundToInt


class PlayerService : MediaBrowserServiceCompat() {

    companion object {
        private const val ROOT_ID = "connection_root_id"
        private const val STOP_SERVICE_ACTION = "stop_player_service_action"
    }

    var mediaPlayer = MediaPlayer().apply {
        setOnCompletionListener {
            if (AudioPlayer.pointer == AudioPlayer.songQueue.size - 1) {
                AudioPlayer.pointer = 0
            } else AudioPlayer.pointer++
        }
    }

    private val isPlaying: Boolean
        get() = mediaPlayer.isPlaying

    private lateinit var mediaSession: MediaSessionCompat

    private val actionsListener = object : MediaSessionCompat.Callback() {

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

            changePlayingState(isPlaying)
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(NotificationDismissedReceiver(), IntentFilter(STOP_SERVICE_ACTION), RECEIVER_NOT_EXPORTED)
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

    private fun seekTo(@FloatRange(0.0, 1.0) progress: Float) {
        mediaPlayer.seekTo((mediaPlayer.duration * progress.coerceIn(0f, 1f)).roundToInt())
    }

    private fun pause() {
        mediaPlayer.pause()
        changePlayingState(false)
        updateNotificationIfLowerTiramisu()
        stopForeground(STOP_FOREGROUND_DETACH)
    }

    private fun play() {
        mediaPlayer.start()
        updateNotificationIfLowerTiramisu()
        changePlayingState(true)
    }

    private fun skipToNext() {
        AudioPlayer.pointer++
        onSongChanged()
    }

    private fun skipToPrevious() {
        AudioPlayer.pointer--
        onSongChanged()
    }

    private fun changePlayingState(isPlaying: Boolean) {
        AudioPlayer.isPlaying = isPlaying
        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(
                    if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                    mediaPlayer.currentPosition.toLong(),
                    1f
                )
                .setActions(
                    PlaybackStateCompat.ACTION_SEEK_TO
                            or PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
                .build()
        )
    }

    private fun applyNewSongData() {
        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    0L,
                    1f
                )
                .setActions(
                    PlaybackStateCompat.ACTION_SEEK_TO
                            or PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
                .build()
        )

        val metadataBuilder = MediaMetadataCompat.Builder()
            .putBitmap(
                MediaMetadataCompat.METADATA_KEY_ART,
                AudioPlayer.currentSong.picture ?: BitmapFactory.decodeResource(
                    applicationContext.resources,
                    R.mipmap.ic_launcher
                )
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
                AudioPlayer.currentSong.duration.toLong()
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
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            updateNotification()
    }

    private fun updateNotification() {
        val mediaStyle = NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2, 3)
            .setMediaSession(mediaSession.sessionToken)

        val togglePlayingPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, ReceiverTogglePlaying::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val nextPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, ReceiverSkipToNext::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val previousPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, ReceiverSkipToPrevious::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val togglePlayingIcon = if (isPlaying)
            R.drawable.pause
        else {
            R.drawable.play
        }

        val notification =
            androidx.core.app.NotificationCompat.Builder(this, MUSIC_PLAYER_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .addAction(R.drawable.arrow_previous, "Previous", previousPendingIntent) // #0
                .addAction(togglePlayingIcon, "Pause", togglePlayingPendingIntent) // #1
                .addAction(R.drawable.arrow_next, "Next", nextPendingIntent) // #2
                .setStyle(mediaStyle)
                .setContentTitle(AudioPlayer.currentSong.title)
                .setContentText(AudioPlayer.currentSong.artist)
                .setLargeIcon(AudioPlayer.currentSongBitmap)
                .setDeleteIntent(onDismissedIntent)
                .build()

        startForeground(1, notification)
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