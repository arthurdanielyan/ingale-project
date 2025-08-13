package com.nightx.ingale.core.audioPlayer.impl.playerActionReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nightx.ingale.core.audioPlayer.impl.PlaybackActionService
import com.nightx.ingale.core.audioPlayer.impl.PlayerActionType

class PlayerActionChangeFavoriteStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val actionsServiceIntent = Intent(context, PlaybackActionService::class.java)
            .putExtra(
                PlaybackActionService.EXTRA_ACTION_KEY,
                PlayerActionType.ChangeFavoriteState.alias
            )
        context?.startService(actionsServiceIntent)
    }
}