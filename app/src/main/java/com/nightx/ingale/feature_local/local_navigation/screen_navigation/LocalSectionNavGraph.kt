package com.nightx.ingale.feature_local.local_navigation.screen_navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nightx.ingale.core.presentation.flows.ComposeCollect
import com.nightx.ingale.core.presentation.navigation.coreNavigation.Destination
import com.nightx.ingale.core.presentation.navigation.coreNavigation.putScreenData
import com.nightx.ingale.feature_local.local_navigation.LocalNavigateEvent
import com.nightx.ingale.feature_local.local_navigation.LocalNavigateUpEvent
import com.nightx.ingale.feature_local.local_navigation.destinations.LocalScreenDestination
import com.nightx.ingale.feature_local.local_navigation.destinations.MainScreenDestination
import com.nightx.ingale.feature_local.local_navigation.destinations.SongsSetScreenDestination
import com.nightx.ingale.feature_local.main.presentation.ui.LocalMainScreen
import com.nightx.ingale.feature_local.songs_set.presentation.ui.SongsSetScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private val appDestinations = mapOf<LocalScreenDestination, @Composable (SavedStateHandle) -> Unit>(
    MainScreenDestination to { LocalMainScreen() },
    SongsSetScreenDestination to { SongsSetScreen(it) }
)

@Composable
fun LocalSectionNavGraph() {
    val navController = rememberNavController()
    ObserveNavigationEvents(navController = navController)

    NavHost(
        navController = navController,
        startDestination = MainScreenDestination.route
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

@Suppress("UNCHECKED_CAST")
@Composable
private fun ObserveNavigationEvents(navController: NavController) {
    val navigation = LocalNavigation.current
    ComposeCollect(navigation.navigationEvent) { navEvent ->
        when(navEvent) {
            is LocalNavigateEvent<*, *> -> {
                val event = navEvent as LocalNavigateEvent<Parcelable, Parcelable>
                navController.navigate(navEvent.destination.route)

                navController.currentBackStackEntry?.savedStateHandle
                    ?.putScreenData(
                        argument = event.argument
                    )
                navController.currentBackStackEntry?.let { backStackEntry ->
                    event.onResult?.let { onResult ->
                        backStackEntry.savedStateHandle
                            .getStateFlow<Parcelable?>(Destination.RESULT_KEY, null)
                            .onEach {
                                it?.let(onResult)
                            }.launchIn(backStackEntry.lifecycleScope)
                    }
                }
            }
            LocalNavigateUpEvent -> {
                navController.popBackStack()
            }
        }
    }
}