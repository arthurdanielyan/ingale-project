package com.nightx.ingale.core

import com.nightx.ingale.core.audio_player.audioPlayerModule
import com.nightx.ingale.core.dataModel.di.mappersModule
import com.nightx.ingale.core.dataModel.di.realmModule
import com.nightx.ingale.core.domain.di.coroutinesModule
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.dialogNavigationModule

val coreModule = coroutinesModule + dialogNavigationModule + realmModule +
        mappersModule + audioPlayerModule