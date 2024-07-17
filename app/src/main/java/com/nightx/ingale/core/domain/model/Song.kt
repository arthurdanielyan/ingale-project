package com.nightx.ingale.core.domain.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Long,
    val title: String,
    val album: String,
    val duration: Long,
    val artist: String,
    val genre: String,
    val path: String,
    val thumbnail: Bitmap?,
    val artistId: Long,
    val albumId: Long,
    val lastModified: Long
) : Parcelable