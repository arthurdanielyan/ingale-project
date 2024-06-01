package com.nightx.ingale.core.audio_player.actions.player_action_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nightx.ingale.core.audio_player.actions.PlaybackActionService
import com.nightx.ingale.core.audio_player.actions.PlayerActionType

class PlayerActionSkipToPrevious : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val actionsServiceIntent = Intent(context, PlaybackActionService::class.java)
            .putExtra(
                PlaybackActionService.EXTRA_ACTION_KEY,
                PlayerActionType.ACTION_SKIP_TO_PREVIOUS.alias
            )
        context?.startService(actionsServiceIntent)
    }
}