package com.nightx.ingale.core

import com.nightx.ingale.core.di.coroutineDispatcherModule
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.dialogNavigationModule

val coreModule = coroutineDispatcherModule + dialogNavigationModule