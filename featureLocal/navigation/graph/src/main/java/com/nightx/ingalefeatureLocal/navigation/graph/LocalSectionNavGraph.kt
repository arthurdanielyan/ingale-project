package com.nightx.ingalefeatureLocal.navigation.graph

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import com.nightx.ingale.featureLocal.featureHome.presentation.ui.LocalMainScreen
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.ui.SongsSetScreen
import com.nightx.ingale.featureLocal.navigation.api.destinations.SongsSetScreenDestination
import com.nightx.ingale.feature_local.local_navigation.destinations.LocalScreenDestination
import com.nightx.ingale.feature_local.local_navigation.destinations.MainScreenDestination

private val appDestinations = mapOf<LocalScreenDestination, @Composable (SavedStateHandle) -> Unit>(
    MainScreenDestination to { LocalMainScreen() },
    SongsSetScreenDestination to { SongsSetScreen(it) }
)

/*
@Composable
fun LocalSectionNavGraph() {
    val navController = rememberNavController()
    ObserveNavigationEvents(navController = navController)
    ControlBottomBar(navController = navController)

    val exitTransition = exitTransition
    val popEnterTransition = popEnterTransition
    NavHost(
        navController = navController,
        startDestination = MainScreenDestination.route,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition },
        popEnterTransition = { popEnterTransition },
        popExitTransition = { popExitTransition }
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

@Composable
private fun ControlBottomBar(navController: NavController) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val bottomBarController = com.nightx.ingale.root.api.LocalBottomBarController.current
    val tabActivityState = com.nightx.ingale.root.api.LocalBottomTabActivityState.current
    LaunchedEffect(currentDestination, tabActivityState) { destination, isTabActive ->
        if (isTabActive) {
            bottomBarController.setVisibility(isBottomBarAllowed(destination?.destination?.route))
        }
    }
}

private fun isBottomBarAllowed(route: String?) = when (route) {
    MainScreenDestination.route -> true
    SongsSetScreenDestination.route -> false
    else -> true
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
}*/
