package com.night.ingale.core.audio_player.actions_receivers

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.night.ingale.core.audio_player.PlayerActions
import com.night.ingale.core.audio_player.PlayerService

class PlaybackActionService : Service() {

    companion object {
        const val EXTRA_ACTION_KEY = "playback_action_receiver_action_key"

        const val EXTRA_ACTION_TOGGLE = "playback_action_receiver_action_value_toggle_playback"
        const val EXTRA_ACTION_NEXT = "playback_action_receiver_action_value_skip_to_next"
        const val EXTRA_ACTION_PREVIOUS = "playback_action_receiver_action_value_skip_to_previous"
    }

    private lateinit var action: String

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.getStringExtra(EXTRA_ACTION_KEY)?.let {
            action = it
            val serviceIntent = Intent(this, PlayerService::class.java)
            bindService(serviceIntent, connection, 0)
        } ?: stopSelf()

        return super.onStartCommand(intent, flags, startId)
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val playbackActions = service as? PlayerActions

            if(playbackActions == null){
                stopSelf()
                return
            }

            when(action) {
                EXTRA_ACTION_TOGGLE -> playbackActions.togglePlaying()
                EXTRA_ACTION_NEXT -> playbackActions.skipToNext()
                EXTRA_ACTION_PREVIOUS -> playbackActions.skipToPrevious()
            }

            stopSelf()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}