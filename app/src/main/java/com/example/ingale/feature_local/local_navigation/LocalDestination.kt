package com.example.ingale.feature_local.local_navigation

interface LocalDestination {

    val route: String
    val argumentKey: String?

    data object MainScreen : LocalDestination {

        override val route = "local_main_screen"
        override val argumentKey = null
    }

    data object SongsSetScreen : LocalDestination {

        override val route = "local_songs_set_screen"
        override val argumentKey = "songs_set_screen_arg_songs_set_type"
    }
}