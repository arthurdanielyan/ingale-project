package com.example.ingale.feature_local.main.presentation.view

import androidx.navigation.NavController
import com.example.ingale.feature_local.local_core.domain.model.SongsSet
import com.example.ingale.main_navigation.Navigator
import com.example.ingale.main_navigation.Screens

class LocalMainNavigator(private val navController: NavController) : Navigator(navController) {

    fun toSongsSet(data: SongsSet) {
        navController.navigate(Screens.Local.SongsSetScreen.route)
        navController.currentBackStackEntry?.savedStateHandle
            ?.set(Screens.Local.SongsSetScreen.ARG_SONGS_SET, data)
    }
}