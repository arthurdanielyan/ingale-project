package com.night.ingale.core

import com.night.ingale.core.di.coroutineDispatcherModule
import com.night.ingale.core.presentation.navigation.dialog_navigation.dialogNavigationModule

val coreModule = coroutineDispatcherModule + dialogNavigationModule