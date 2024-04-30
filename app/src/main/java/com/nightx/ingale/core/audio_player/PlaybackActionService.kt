package com.nightx.ingale.core.audio_player

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

class PlaybackActionService : Service() {

    companion object {
        const val EXTRA_ACTION_KEY = "extra_action_key"

        const val EXTRA_ACTION_TOGGLE_PLAYBACK = "player_actions_service_toggle_playback"
        const val EXTRA_ACTION_SKIP_TO_NEXT = "player_actions_service_skip_to_next"
        const val EXTRA_ACTION_SKIP_TO_PREVIOUS = "player_actions_service_skip_to_previous"
        const val EXTRA_ACTION_CHANGE_FAVORITE_STATE = "player_actions_service_change_favorite_state"
        const val EXTRA_ACTION_STOP_SERVICE = "player_actions_service_stop_service"
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
            val playerActions = service as? PlayerActions

            if(playerActions == null){
                stopSelf()
                return
            }

            when(action) {
                EXTRA_ACTION_TOGGLE_PLAYBACK -> playerActions.togglePlaying()
                EXTRA_ACTION_SKIP_TO_NEXT -> playerActions.skipToNext()
                EXTRA_ACTION_SKIP_TO_PREVIOUS -> playerActions.skipToPrevious()
                EXTRA_ACTION_CHANGE_FAVORITE_STATE -> playerActions.changeFavoriteState()
                EXTRA_ACTION_STOP_SERVICE -> playerActions.stopService()
            }
            stopSelf()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}