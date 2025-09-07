package com.nightx.ingale.featureLocal.featureHome.data.repository

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.core.net.toUri
import com.nightx.ingale.core.dataModel.SongRealm
import com.nightx.ingale.core.dataModel.mapper.SongRealmMapper
import com.nightx.ingale.core.domainModel.LoadState
import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.core.utils.CoroutineDispatchers
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.core.utils.yap
import com.nightx.ingale.featureLocal.featureHome.domain.repository.LocalMainRepository
import com.nightx.ingale.resources.strings.StringProvider
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.LinkedList
import kotlin.math.sqrt
import com.nightx.ingale.resources.strings.R.string as Strings


class LocalMainRepositoryImpl(
    private val applicationContext: Context,
    private val songsDb: Realm,
    private val dispatchers: CoroutineDispatchers,
    private val songRealmMapper: SongRealmMapper,
    private val stringProvider: StringProvider,
    private val applicationScope: CoroutineScope,
) : LocalMainRepository {

    override suspend fun isCached(): Boolean {
        return songsDb.query<SongRealm>()
            .asFlow()
            .first().list.isEmpty().not()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getSongs(): Flow<LoadState<List<Song>>> {
        val savingDeferred = applicationScope.async {
            saveLocally()
        }
        return songsDb.query<SongRealm>()
            .asFlow()
            .mapLatest<ResultsChange<SongRealm>, LoadState<List<Song>>> {
                var localSave: List<SongRealm>? = null
                if (it.list.isEmpty()) {
                    if (!savingDeferred.isCompleted) {
                        localSave = savingDeferred.await()
                    }
                }
                LoadState.Success(
                    songRealmMapper.mapList(
                        // take already saved if exists not to wait for Realm to emit them
                        localSave ?: it.list
                    )
                )
            }.onStart { emit(LoadState.Loading()) }
    }

    private suspend fun saveLocally() = withContext(dispatchers.io) {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = applicationContext.contentResolver.query(uri, null, null, null, null)
        val localSongs = LinkedList<SongRealm>() // only additions happens so LinkedList is faster

        if ((cursor?.count ?: -1) > 0) {
            cursor!!
            do {
                try {
                    val idColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                    val id = cursor.getLong(idColumnIndex)
                    val pathColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
                    val songPath = cursor.getString(pathColumnIndex)

                    val metadataRetriever = MediaMetadataRetriever().apply {
                        setDataSource(songPath)
                    }

                    val titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                    val title = cursor.getString(titleColumnIndex)

                    val albumNameColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
                    val albumName =
                        metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                            ?: cursor.getString(albumNameColumnIndex)


                    val durationColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                    val duration =
                        metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                            ?.toLongOrNull()
                            ?: cursor.getLong(durationColumnIndex)

                    val artistNameColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                    val artistName =
                        metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
                            ?: cursor.getString(artistNameColumnIndex)
                    yap(artistName)


                    val albumIdCol = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                    val albumId = cursor.getLong(albumIdCol)

                    val genreNameColumnIndex =
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R)
                            cursor.getColumnIndex(MediaStore.Audio.Media.GENRE)
                        else -1
                    val genre =
                        metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
                            ?: if (genreNameColumnIndex >= 0) {
                                cursor.getString(genreNameColumnIndex)
                            } else {
                                null
                            }

                    val artistIdCol = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)
                    val artistId = cursor.getLong(artistIdCol)

                    val modificationDate = File(songPath).lastModified()

                    val embeddedPicture = metadataRetriever.embeddedPicture

                    var albumArt: Bitmap? =
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                val extUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                                val songUri = ContentUris.withAppendedId(extUri, id)

                                applicationContext.contentResolver.loadThumbnail(
                                    songUri,
                                    MaxImageSizeByte.let {
                                        val side = sqrt(it.toFloat()).toInt()
                                        Size(side, side)
                                    },
                                    null
                                )
                            } else {

                                val sArtWorkUri = "content://media/external/audio/album".toUri()
                                val albumArtUri =
                                    ContentUris.withAppendedId(sArtWorkUri, albumId)

                                MediaStore.Images.Media.getBitmap(
                                    applicationContext.contentResolver,
                                    albumArtUri
                                )
                            }
                        } catch (e: Exception) {
                            null
                        }

                    if (albumArt == null && embeddedPicture != null && embeddedPicture.isNotEmpty()) {
                        albumArt =
                            BitmapFactory.decodeByteArray(embeddedPicture, 0, MaxImageSizeByte)
                    }

                    var previewPath = ""
                    if (albumArt != null) {
                        try {
                            val previewName = "$id.$ImageExtension"
                            val appDataDir = applicationContext.filesDir.apply {
                                mkdirs()
                                setReadable(true, true)
                            }
                            previewPath = "${appDataDir.path}/$previewName"
                            val file = File(appDataDir, previewName)
                            if (file.exists().not()) {
                                val fos = FileOutputStream(file)
                                albumArt.compress(ImageCompressFormat, 100, fos)
                                fos.close()
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                    val song = SongRealm().apply {
                        this.id = id
                        this.title = title
                        this.album = albumName
                        this.duration = duration
                        this.artist =
                            if (artistName in UnknownArtistPlaceholders) {
                                stringProvider.string(Strings.unknown_artist)
                            } else artistName
                        this.genre = genre ?: stringProvider.string(Strings.unknown_genre)
                        this.path = songPath
                        this.previewPath = previewPath
                        this.albumId = albumId
                        this.artistId = artistId
                        this.lastModified = modificationDate
                    }

                    localSongs.add(song)
                } catch (e: Exception) {
                    Log.e("fileReadingError", "Couldn't read the media file", e)
                }

            } while (cursor.moveToNext())
            cursor.close()
            songsDb.write {
                localSongs.forEach {
                    copyToRealm(it, UpdatePolicy.ALL)
                }
            }
        }

        localSongs
    }

    private companion object {
        private const val MaxImageSizeByte = 1048576 // 1024*1024, 1 MB
        private const val ImageExtension = "png"
        private val ImageCompressFormat = Bitmap.CompressFormat.PNG
        private val UnknownArtistPlaceholders = setOf(null, "", "<unknown>")
    }
}