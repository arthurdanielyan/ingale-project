package com.nightx.ingale.feature_local.main.data.entity

data class SongEntity(
    val id: Long,
    val title: String,
    val albumName: String,
    val duration: Long,
    val artistName: String,
    val albumId: Long,
    val embeddedPicture: ByteArray? = null,
    val songPath: String,
    val genre: String,
    val previewPath: String,
    val artistId: Long,
    val modificationDate: Long,
)
