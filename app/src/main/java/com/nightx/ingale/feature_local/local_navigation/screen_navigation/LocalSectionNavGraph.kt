package com.nightx.ingale.feature_local.local_navigation.screen_navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nightx.ingale.core.di.ingaleViewModels
import com.nightx.ingale.core.presentation.flows.ComposeCollect
import com.nightx.ingale.core.presentation.navigation.destination.putScreenData
import com.nightx.ingale.feature_local.main.presentation.LocalMainScreen
import com.nightx.ingale.feature_local.main.presentation.view.LocalMainViewModel
import com.nightx.ingale.feature_local.songs_set.presentation.SongsSetScreen
import com.nightx.ingale.feature_local.songs_set.presentation.view.SongsSetViewModel
import com.nightx.ingale.main_navigation.Screens


@Composable
fun LocalSectionNavGraph() {
    val navController = rememberNavController()
    ObserveNavigationEvents(navController = navController)

    NavHost(
        navController = navController,
        startDestination = Screens.Local.MainScreen.route
    ) {
        composable(
            route = LocalScreenDestination.MainScreen.route
        ) {
            val vm = ingaleViewModels<LocalMainViewModel>()
            LocalMainScreen(
                state = vm.state.collectAsState().value,
                sendEvent = vm::sendEvent,
                effects = vm.effect
            )
        }

        composable(
            route = LocalScreenDestination.SongsSetScreen.route
        ) {
            val vm = ingaleViewModels<SongsSetViewModel>(
                savedStateHandle = it.savedStateHandle
            )
            SongsSetScreen(
                state = vm.state.collectAsState().value,
                sendEvent = vm::sendEvent
            )
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