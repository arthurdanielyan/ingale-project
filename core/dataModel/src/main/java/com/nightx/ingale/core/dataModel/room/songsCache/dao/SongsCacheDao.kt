package com.nightx.ingale.core.dataModel.room.songsCache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.nightx.ingale.core.dataModel.room.songsCache.SongsCacheTable
import com.nightx.ingale.core.dataModel.room.songsCache.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongsCacheDao {

    @Upsert
    suspend fun upsertSong(rate: SongEntity)

    @Query("DELETE FROM ${SongsCacheTable.TABLE_NAME}")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<SongEntity>)

    @Query("SELECT * FROM ${SongsCacheTable.TABLE_NAME}")
    fun observeSongsCache(): Flow<List<SongEntity>>

    @Query("DELETE FROM ${SongsCacheTable.TABLE_NAME} WHERE id = :songId")
    suspend fun deleteById(songId: Long)

    @Transaction
    suspend fun replaceAll(songs: List<SongEntity>) {
        clearAll()
        insertAll(songs)
    }
}