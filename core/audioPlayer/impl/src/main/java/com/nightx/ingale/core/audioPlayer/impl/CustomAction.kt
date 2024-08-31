package com.nightx.ingale.core.audioPlayer.impl

import androidx.annotation.DrawableRes

data class CustomAction(
    val actionId: String,
    val actionName: String,
    @DrawableRes val actionIcon: Int
)
