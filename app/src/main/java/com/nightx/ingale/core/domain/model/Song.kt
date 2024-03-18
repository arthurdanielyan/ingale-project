package com.nightx.ingale.core.domain.model

import android.graphics.Bitmap
import java.io.Serializable

data class Song(
    val id: Long,
    val title: String,
    val album: String,
    val duration: Int,
    val artist: String,
    val genre: String,
    val path: String,
    val picture: Bitmap?,
    val artistId: Long,
    val albumId: Long,
    val lastModified: Long
) : Serializable