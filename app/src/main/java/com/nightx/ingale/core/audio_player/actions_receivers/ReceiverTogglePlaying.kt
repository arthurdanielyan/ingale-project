package com.nightx.ingale.core.audio_player.actions_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nightx.ingale.core.audio_player.actions_receivers.PlaybackActionService.Companion.EXTRA_ACTION_KEY
import com.nightx.ingale.core.audio_player.actions_receivers.PlaybackActionService.Companion.EXTRA_ACTION_TOGGLE

class ReceiverTogglePlaying : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val actionsServiceIntent = Intent(context, PlaybackActionService::class.java)
        actionsServiceIntent.putExtra(EXTRA_ACTION_KEY, EXTRA_ACTION_TOGGLE)
        context?.startService(actionsServiceIntent)
    }
}