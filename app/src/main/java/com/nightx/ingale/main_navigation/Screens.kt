package com.nightx.ingale.main_navigation

sealed class Screens(val route: String) {

    interface Local {
        object MainScreen : Screens("local_main_screen")

        object SongsSetScreen : Screens("local_songs_set_screen") {
            const val ARG_SONGS_SET = "songs_set_screen_arg_songs_set_type"
        }
    }

    interface Youtube
}