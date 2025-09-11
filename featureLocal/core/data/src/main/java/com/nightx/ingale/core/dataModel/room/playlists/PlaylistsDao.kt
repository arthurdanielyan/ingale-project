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
                "WHERE ${PlaylistsTable.COLUMN_ID} = :playlistId"
    )
    suspend fun getPlaylistWithSongs(playlistId: Int): PlaylistWithSongs

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun initializePlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSongCrossRef(crossRef: PlaylistSongCrossRef)

    @Transaction
    suspend fun createPlaylist(playlist: PlaylistEntity, songId: Long) {
        initializePlaylist(playlist)
        insertPlaylistSongCrossRef(PlaylistSongCrossRef(playlist.id, songId))
    }
}