package com.nightx.ingale.core.audioPlayer.impl

import androidx.annotation.FloatRange

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

    data object ChangeFavoriteState : PlayerActionType {
        override val alias = "player_actions_service_change_favorite_state"
    }

    data object StopService : PlayerActionType {
        override val alias = "player_actions_service_stop_service"
    }

    data object PlaySong : PlayerActionType {
        override val alias = "player_actions_service_init"
    }

    data class SeekTo(
        @FloatRange(from = 0.0, to = 1.0) val percentage: Float
    ) : PlayerActionType {
        override val alias = "player_actions_service_seek_to"
    }
}
