package com.nightx.ingale.core.audioPlayer.impl

sealed interface PlayerActionType {
    val alias: String

    data object TogglePlayback : PlayerActionType {
        override val alias = "player_actions_service_toggle_playback"
    }

    data object SkipToNext : PlayerActionType {
        override val alias = "player_actions_service_skip_to_next"
    }

    data object SkipToPrevious : PlayerActionType {
        override val alias = "player_actions_service_skip_to_previous"
    }

    data object ChangePlaybackLoopMode : PlayerActionType {
        override val alias = "player_actions_service_change_favorite_state"
    }

    data object StopService : PlayerActionType {
        override val alias = "player_actions_service_stop_service"
    }
}
