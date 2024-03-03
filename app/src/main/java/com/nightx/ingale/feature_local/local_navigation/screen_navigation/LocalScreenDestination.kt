package com.nightx.ingale.feature_local.local_navigation.screen_navigation

import com.nightx.ingale.core.presentation.navigation.destination.Destination

interface LocalScreenDestination : Destination {

    data object MainScreen : LocalScreenDestination {

        override val route = "local_main_screen"
    }

    data object SongsSetScreen : LocalScreenDestination {

        override val route = "local_songs_set_screen"
    }
}