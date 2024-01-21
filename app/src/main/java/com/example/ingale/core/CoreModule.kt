package com.example.ingale.core

import com.example.ingale.core.di.coroutineDispatcherModule
import com.example.ingale.core.presentation.navigation.dialog_navigation.dialogNavigationModule

val coreModule = coroutineDispatcherModule + dialogNavigationModule