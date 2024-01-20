package com.example.ingale.feature_local.local_navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ingale.core.di.ingaleViewModels
import com.example.ingale.core.presentation.ComposeCollect
import com.example.ingale.feature_local.main.presentation.LocalMainScreen
import com.example.ingale.feature_local.main.presentation.view.LocalMainViewModel
import com.example.ingale.feature_local.songs_set.presentation.SongsSetScreen
import com.example.ingale.feature_local.songs_set.presentation.view.SongsSetViewModel
import com.example.ingale.main_navigation.BottomNavigationEffects
import com.example.ingale.main_navigation.Screens
import com.example.ingale.main_navigation.bottomNavigationEffects


@Composable
fun LocalSectionNavGraph() {
    val navController = rememberNavController()
    ObserveNavigationEvents(navController = navController)

    NavHost(
        navController = navController,
        startDestination = Screens.Local.MainScreen.route
    ) {
        composable(
            route = LocalDestination.MainScreen.route
        ) {
            LaunchedEffect(Unit) { bottomNavigationEffects.emit(BottomNavigationEffects.ShowBottomBar) }
            val vm = ingaleViewModels<LocalMainViewModel>()
            LocalMainScreen(
                state = vm.state.collectAsState().value,
                sendEvent = vm::sendEvent,
                effects = vm.effect
            )
        }

        composable(
            route = LocalDestination.SongsSetScreen.route
        ) {
            LaunchedEffect(Unit) {
                bottomNavigationEffects.emit(BottomNavigationEffects.HideBottomBar)
            }
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
        navEvent.argument?.let {
            navController.currentBackStackEntry?.savedStateHandle
                ?.set(navEvent.destination.argumentKey!!, navEvent.argument)
        }
    }
}