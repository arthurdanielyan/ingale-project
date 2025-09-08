package com.nightx.ingale.core.dataModel.room.songsCache.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nightx.ingale.core.dataModel.room.songsCache.SongsCacheTable

@Entity(tableName = SongsCacheTable.TABLE_NAME)
data class SongEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = SongsCacheTable.COLUMN_ID) val id: Long = 0L,
    @ColumnInfo(name = SongsCacheTable.COLUMN_TITLE) val title: String = "",
    @ColumnInfo(name = SongsCacheTable.COLUMN_ALBUM) val album: String = "",
    @ColumnInfo(name = SongsCacheTable.COLUMN_DURATION) val duration: Long = 0,
    @ColumnInfo(name = SongsCacheTable.COLUMN_ARTIST) val artist: String = "",
    @ColumnInfo(name = SongsCacheTable.COLUMN_GENRE) val genre: String = "",
    @ColumnInfo(name = SongsCacheTable.COLUMN_PATH) val path: String = "",
    @ColumnInfo(name = SongsCacheTable.COLUMN_PREVIEW_PATH) val previewPath: String = "",
    @ColumnInfo(name = SongsCacheTable.COLUMN_ARTIST_ID) val artistId: Long = 0L,
    @ColumnInfo(name = SongsCacheTable.COLUMN_ALBUM_ID) val albumId: Long = 0L,
    @ColumnInfo(name = SongsCacheTable.COLUMN_LAST_MODIFIED) val lastModified: Long = 0L,
)


