package com.nightx.ingale.feature_local.main.data.repository

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.annotation.RequiresApi
import com.nightx.ingale.R
import com.nightx.ingale.core.data.model.SongRealm
import com.nightx.ingale.core.data.model.mapper.SongRealmMapper
import com.nightx.ingale.core.domain.CoroutineDispatchers
import com.nightx.ingale.core.domain.LoadState
import com.nightx.ingale.core.domain.mapList
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.feature_local.main.data.entity.SongEntity
import com.nightx.ingale.feature_local.main.data.helper.encodeToBase64
import com.nightx.ingale.feature_local.main.data.util.isSongInfoAvailable
import com.nightx.ingale.feature_local.main.domain.repository.LocalMainRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.LinkedList


class LocalMainRepositoryImpl(
    private val applicationContext: Context,
    private val songsDb: Realm,
    private val dispatchers: CoroutineDispatchers,
    private val songRealmMapper: SongRealmMapper,
    private val applicationScope: CoroutineScope,
) : LocalMainRepository {

    @RequiresApi(Build.VERSION_CODES.R)
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getSongs(): Flow<LoadState<List<Song>>> {
        val savingDeferred = applicationScope.async {
            saveLocally()
        }
        return songsDb.query<SongRealm>()
            .asFlow()
            .mapLatest<ResultsChange<SongRealm>, LoadState<List<Song>>> {
                if (it.list.isEmpty()) {
                    if (!savingDeferred.isCompleted) {
                        savingDeferred.await()
                    }
                }
                LoadState.Success(songRealmMapper.mapList(it.list))
            }.onStart { emit(LoadState.Loading()) }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private suspend fun saveLocally() = withContext(dispatchers.io) {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = applicationContext.contentResolver.query(uri, null, null, null, null)
        val localSongs = LinkedList<SongRealm>() // only additions happens so LinkedList is faster

        if (cursor != null && cursor.count != -1) {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        val metadata = extractSongMetadata(cursor)
                        val song = SongRealm().apply {
                            this.id = metadata.id
                            this.title = metadata.title
                            this.album = if (metadata.albumName == "Download") {
                                applicationContext.getString(R.string.unknown_album)
                            } else metadata.albumName
                            this.duration = metadata.duration
                            this.artist = if (!metadata.artistName.isSongInfoAvailable()) {
                                applicationContext.resources.getString(R.string.unknown_artist)
                            } else metadata.artistName
                            this.genre = metadata.genre
                            this.path = metadata.songPath
                            this.previewPath = metadata.previewPath
                            this.albumId = metadata.albumId
                            this.artistId = metadata.artistId
                            this.lastModified = metadata.modificationDate
                        }

                        localSongs.add(song)
                    } catch (e: Exception) {
                        Log.e("fileReadingError", "Couldn't read the media file", e)
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
            songsDb.write {
                localSongs.forEach {
                    copyToRealm(it, UpdatePolicy.ALL)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun extractSongMetadata(
        cursor: Cursor,
    ): SongEntity {
        val songPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

        val metadataRetriever = MediaMetadataRetriever().apply {
            setDataSource(songPath)
        }

        val genreNameColumnIndex =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R)
                cursor.getColumnIndex(MediaStore.Audio.Media.GENRE)
            else -1
        val genre =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
                ?: if (genreNameColumnIndex >= 0) {
                    cursor.getString(genreNameColumnIndex)
                        ?: applicationContext.resources.getString(R.string.unknown)
                } else {
                    applicationContext.resources.getString(R.string.unknown)
                }

        val encodedThumbnail = encodeToBase64(getAudioThumbnail(cursor), quality = 100)

        return getSongMetadataFromCursor(cursor, metadataRetriever, genre, encodedThumbnail)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getAudioThumbnail(cursor: Cursor): Bitmap? {
        return try {
            val uriIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val audioFile = Uri.parse(cursor.getString(uriIndex)).path.let { File(it) }
            val image = ThumbnailUtils.createAudioThumbnail(audioFile, Size.parseSize("700*+600"), null)
            image
        } catch (e: IOException) {
            null
        }
    }

    private fun getSongMetadataFromCursor(
        cursor: Cursor,
        metadataRetriever: MediaMetadataRetriever,
        genre: String,
        encodedThumbnail: String?,
    ): SongEntity {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
        val albumName = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
            ?: cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
        val duration =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLongOrNull()
                ?: cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
        val artistName =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
                ?: cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
        val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID))
        val embeddedPicture = metadataRetriever.embeddedPicture
        val songPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
        val modificationDate = File(songPath).lastModified()

        return SongEntity(
            id = id,
            title = title,
            albumName = albumName,
            duration = duration,
            artistName = artistName,
            albumId = albumId,
            embeddedPicture = embeddedPicture,
            songPath = songPath,
            genre = genre,
            previewPath = encodedThumbnail ?: "",
            artistId = albumId,
            modificationDate = modificationDate
        )
    }
}
