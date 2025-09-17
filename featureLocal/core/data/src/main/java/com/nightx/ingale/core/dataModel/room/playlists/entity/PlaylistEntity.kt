package com.nightx.ingale.core.dataModel.room.playlists.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nightx.ingale.core.dataModel.room.playlists.PlaylistsTable

@Entity(tableName = PlaylistsTable.TABLE_NAME)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = PlaylistsTable.COLUMN_NAME) val name: String,
)