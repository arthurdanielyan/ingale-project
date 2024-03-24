package com.nightx.ingale.core.domain.model

import java.io.Serializable

data class Song(
    val id: Long,
    val title: String,
    val album: String,
    val duration: Long,
    val artist: String,
    val genre: String,
    val path: String,
    val picturePath: String,
    val artistId: Long,
    val albumId: Long,
    val lastModified: Long
) : Serializable