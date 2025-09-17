package com.nightx.ingale.core.domain.playlists.exceptions

class PlaylistAlreadyExistsException(
    private val playlistName: String,
) : IllegalArgumentException() {

    override val message: String?
        get() = "Playlist with name $playlistName already exists."

    override fun getLocalizedMessage(): String? {
        return message
    }
}