package com.example.ingale.feature_local

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ingale.core.di.ingaleViewModels
import com.example.ingale.feature_local.main.presentation.LocalMainScreen
import com.example.ingale.feature_local.main.presentation.view.LocalMainNavigator
import com.example.ingale.feature_local.main.presentation.view.LocalMainViewModel
import com.example.ingale.feature_local.songs_set.presentation.SongsSetScreen
import com.example.ingale.feature_local.songs_set.presentation.view.LocalSongsSetNavigator
import com.example.ingale.feature_local.songs_set.presentation.view.SongsSetViewModel
import com.example.ingale.main_navigation.BottomNavigationEffects
import com.example.ingale.main_navigation.Screens
import com.example.ingale.main_navigation.bottomNavigationEffects


@Composable
fun LocalSectionNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Local.MainScreen.route
    ) {
        composable(
            route = Screens.Local.MainScreen.route,
        ) {
            LaunchedEffect(Unit) { bottomNavigationEffects.emit(BottomNavigationEffects.ShowBottomBar) }
//            val vm = ingaleViewModels<LocalMainViewModel>(navigator = LocalMainNavigator(navController))
            val vm = hiltViewModel<LocalMainViewModel>()
            LocalMainScreen(
                state = vm.state.collectAsState().value!!,
                sendEvent = vm::sendEvent,
                effects = vm.effect,
                requiredPermissions = vm.requiredPermissions
            )
        }

        composable(
            route = Screens.Local.SongsSetScreen.route,
        ) {
            LaunchedEffect(Unit) {
                bottomNavigationEffects.emit(BottomNavigationEffects.HideBottomBar)
            }
            val vm = ingaleViewModels<SongsSetViewModel>(
                savedStateHandle = it.savedStateHandle,
                navigator = LocalSongsSetNavigator(navController)
            )
            SongsSetScreen(
                state = vm.state.collectAsState().value!!,
                sendEvent = vm::sendEvent
            )
        }
    }
}