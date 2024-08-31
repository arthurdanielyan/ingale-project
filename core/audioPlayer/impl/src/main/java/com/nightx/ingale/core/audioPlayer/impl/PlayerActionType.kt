package com.nightx.ingale.core.audioPlayer.impl

enum class PlayerActionType(val alias: String) {

    ACTION_TOGGLE_PLAYBACK("player_actions_service_toggle_playback"),
    ACTION_SKIP_TO_NEXT("player_actions_service_skip_to_next"),
    ACTION_SKIP_TO_PREVIOUS("player_actions_service_skip_to_previous"),
    ACTION_CHANGE_FAVORITE_STATE("player_actions_service_change_favorite_state"),
    ACTION_STOP_SERVICE("player_actions_service_stop_service"),
    ACTION_PLAY_SONG("player_actions_service_init"),
}