package com.nightx.ingale.core.audioPlayer.impl

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

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
                PlayerActionType.TogglePlayback.alias -> playerServiceActions.togglePlaying()
                PlayerActionType.SkipToNext.alias -> playerServiceActions.skipToNext()
                PlayerActionType.SkipToPrevious.alias -> playerServiceActions.skipToPrevious()
                PlayerActionType.ChangePlaybackLoopMode.alias -> playerServiceActions.changePlaybackLoopMode()
                PlayerActionType.StopService.alias -> playerServiceActions.stopService()
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