package com.nightx.ingale.featureLocal.navigation.api

import androidx.compose.runtime.staticCompositionLocalOf
import com.nightx.ingale.core.navigation.Navigator
import com.nightx.ingale.feature_local.local_navigation.destinations.LocalScreenDestination

interface LocalNavigator : Navigator<LocalScreenDestination>

val LocalNavigation = staticCompositionLocalOf<LocalNavigator> {
    throw Exception("No LocalNavigator provided")
}
