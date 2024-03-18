package com.nightx.ingale.core

import com.nightx.ingale.core.data.di.mappersModule
import com.nightx.ingale.core.data.di.realmModule
import com.nightx.ingale.core.domain.di.coroutineDispatcherModule
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.dialogNavigationModule

val coreModule = coroutineDispatcherModule + dialogNavigationModule + realmModule + mappersModule