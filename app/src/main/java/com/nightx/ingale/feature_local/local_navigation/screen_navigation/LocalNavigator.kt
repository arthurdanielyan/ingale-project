package com.nightx.ingale.feature_local.local_navigation.screen_navigation

import androidx.compose.runtime.staticCompositionLocalOf
import com.nightx.ingale.core.presentation.navigation.coreNavigation.Navigator
import com.nightx.ingale.feature_local.local_navigation.destinations.LocalScreenDestination

// moved
interface LocalNavigator : Navigator<LocalScreenDestination>

val LocalNavigation = staticCompositionLocalOf<LocalNavigator> {
    throw Exception("No LocalNavigator provided")
}
