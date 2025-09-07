package com.nightx.ingale.core.audioPlayer.api

enum class PlaybackLoopMode {
    PlaylistLoop, Shuffle, Single;

    val next: PlaybackLoopMode
        get() = PlaybackLoopMode.entries[
            if (ordinal + 1 > entries.lastIndex) {
                0
            } else {
                ordinal + 1
            }
        ]
}