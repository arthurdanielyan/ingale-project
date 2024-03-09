package com.nightx.ingale.feature_local.local_navigation.screen_navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nightx.ingale.core.presentation.flows.ComposeCollect
import com.nightx.ingale.core.presentation.navigation.destination.putScreenData
import com.nightx.ingale.feature_local.main.presentation.LocalMainScreen
import com.nightx.ingale.feature_local.songs_set.presentation.SongsSetScreen
import com.nightx.ingale.main_navigation.Screens

private val appDestinations = mapOf<LocalScreenDestination, @Composable (SavedStateHandle) -> Unit>(
    LocalScreenDestination.MainScreen to { LocalMainScreen() },
    LocalScreenDestination.SongsSetScreen to { SongsSetScreen(it) }
)

@Composable
fun LocalSectionNavGraph() {
    val navController = rememberNavController()
    ObserveNavigationEvents(navController = navController)

    NavHost(
        navController = navController,
        startDestination = Screens.Local.MainScreen.route
    ) {
        appDestinations.forEach { (destination, composable) ->
            composable(
                route = destination.route
            ) {
                composable(it.savedStateHandle)
            }
        }
    }
}

@Composable
private fun ObserveNavigationEvents(navController: NavController) {
    val navigation = LocalNavigation.current
    ComposeCollect(navigation.navigationEvent) { navEvent ->
        navController.navigate(navEvent.destination.route)
        navController.currentBackStackEntry?.savedStateHandle
            ?.putScreenData(
                argument = navEvent.argument,
                onResult = navEvent.onResult
            )
    }
}