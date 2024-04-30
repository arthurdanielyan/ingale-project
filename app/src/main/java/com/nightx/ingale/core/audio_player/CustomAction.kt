package com.nightx.ingale.core.audio_player

import androidx.annotation.DrawableRes

data class CustomAction(
    val actionId: String,
    val actionName: String,
    @DrawableRes val actionIcon: Int
)
