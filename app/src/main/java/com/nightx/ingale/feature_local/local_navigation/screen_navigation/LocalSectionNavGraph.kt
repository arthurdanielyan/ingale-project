package com.nightx.ingale.feature_local.local_navigation.screen_navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nightx.ingale.core.presentation.flows.ComposeCollect
import com.nightx.ingale.core.presentation.navigation.coreNavigation.Destination
import com.nightx.ingale.core.presentation.navigation.coreNavigation.NavigateEvent
import com.nightx.ingale.core.presentation.navigation.coreNavigation.NavigateUpEvent
import com.nightx.ingale.core.presentation.navigation.coreNavigation.putScreenData
import com.nightx.ingale.core.presentation.ui.nothingEnter
import com.nightx.ingale.core.presentation.ui.nothingExit
import com.nightx.ingale.core.presentation.ui.slideInLeft
import com.nightx.ingale.core.presentation.ui.slideOutRight
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
        startDestination = MainScreenDestination.route,
        enterTransition = { slideInLeft },
        exitTransition = { nothingExit },
        popEnterTransition = { nothingEnter },
        popExitTransition = { slideOutRight }
    ) {
        appDestinations.forEach { (destination, screenContent) ->
            composable(
                route = destination.route
            ) {
                screenContent(it.savedStateHandle)
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
private fun ObserveNavigationEvents(navController: NavController) {
    val navigation = LocalNavigation.current
    val lifecycleOwner = LocalLifecycleOwner.current
    ComposeCollect(navigation.navigationEvent) { navEvent ->
        when (navEvent) {
            is NavigateEvent<*, *> -> {
                val event = navEvent as NavigateEvent<Parcelable, Parcelable>
                navController.navigate(navEvent.destination.route) {
                    launchSingleTop = true
                }

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

            NavigateUpEvent -> {
                navController.popBackstackIfResumed(lifecycleOwner.lifecycle)
            }
        }
    }
}

fun NavController.popBackstackIfResumed(lifecycle: Lifecycle) {
    if(lifecycle.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}
