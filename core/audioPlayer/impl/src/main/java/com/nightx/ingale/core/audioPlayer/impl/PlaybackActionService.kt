package com.nightx.ingale.core.audioPlayer.impl

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType.ACTION_CHANGE_FAVORITE_STATE
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType.ACTION_SKIP_TO_NEXT
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType.ACTION_SKIP_TO_PREVIOUS
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType.ACTION_STOP_SERVICE
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType.ACTION_TOGGLE_PLAYBACK

class PlaybackActionService : Service() {

    companion object {
        const val EXTRA_ACTION_KEY = "extra_action_key"
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
            val playerServiceActions = service as? PlayerServiceActions

            if(playerServiceActions == null){
                stopSelf()
                return
            }

            when(action) {
                ACTION_TOGGLE_PLAYBACK.alias -> playerServiceActions.togglePlaying()
                ACTION_SKIP_TO_NEXT.alias -> playerServiceActions.skipToNext()
                ACTION_SKIP_TO_PREVIOUS.alias -> playerServiceActions.skipToPrevious()
                ACTION_CHANGE_FAVORITE_STATE.alias -> playerServiceActions.changeFavoriteState()
                ACTION_STOP_SERVICE.alias -> playerServiceActions.stopService()
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