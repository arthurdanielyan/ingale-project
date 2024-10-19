package com.nightx.ingalefeatureLocal.navigation.graph

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nightx.ingale.core.navigation.Destination
import com.nightx.ingale.core.navigation.NavigateEvent
import com.nightx.ingale.core.navigation.NavigateUpEvent
import com.nightx.ingale.core.navigation.putScreenData
import com.nightx.ingale.core.ui.flows.ComposeCollect
import com.nightx.ingale.core.ui.nothingEnter
import com.nightx.ingale.core.ui.nothingExit
import com.nightx.ingale.core.ui.slideInLeft
import com.nightx.ingale.core.ui.slideOutRight
import com.nightx.ingale.featureLocal.featureHome.presentation.ui.LocalMainScreen
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.ui.SongsSetScreen
import com.nightx.ingale.featureLocal.navigation.api.LocalNavigation
import com.nightx.ingale.featureLocal.navigation.api.destinations.SongsSetScreenDestination
import com.nightx.ingale.feature_local.local_navigation.destinations.LocalScreenDestination
import com.nightx.ingale.feature_local.local_navigation.destinations.MainScreenDestination
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

private fun NavController.popBackstackIfResumed(lifecycle: Lifecycle) {
    if(lifecycle.currentState == androidx.lifecycle.Lifecycle.State.RESUMED) {
        popBackStack()
    }
}