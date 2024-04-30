package com.nightx.ingale.core.audio_player.player_action_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nightx.ingale.core.audio_player.PlaybackActionService

class PlayerActionChangeFavoriteStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val actionsServiceIntent = Intent(context, PlaybackActionService::class.java)
            .putExtra(
                PlaybackActionService.EXTRA_ACTION_KEY,
                PlaybackActionService.EXTRA_ACTION_CHANGE_FAVORITE_STATE
            )
        context?.startService(actionsServiceIntent)
    }
}