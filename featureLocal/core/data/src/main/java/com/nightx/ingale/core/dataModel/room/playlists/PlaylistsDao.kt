package com.nightx.ingale.core.dataModel.room.playlists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nightx.ingale.core.dataModel.room.playlists.entity.PlaylistEntity
import com.nightx.ingale.core.dataModel.room.playlists.entity.relations.PlaylistSongCrossRef
import com.nightx.ingale.core.dataModel.room.playlists.entity.relations.PlaylistWithSongs

@Dao
interface PlaylistsDao {

    @Transaction
    @Query(
        "SELECT * FROM ${PlaylistsTable.TABLE_NAME} " +
                "WHERE ${PlaylistsTable.COLUMN_NAME} = :playlistName"
    )
    suspend fun getPlaylistWithSongs(playlistName: Int): PlaylistWithSongs

    @Transaction
    @Query("SELECT * FROM ${PlaylistsTable.TABLE_NAME}")
    suspend fun getPlaylists(): List<PlaylistWithSongs>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSongCrossRef(crossRef: PlaylistSongCrossRef)
}