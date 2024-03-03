package com.nightx.ingale.feature_local.main.data.repository

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.feature_local.main.domain.repository.LocalMainRepository
import java.io.File

class LocalMainRepositoryImpl(
    private val applicationContext: Context
) : LocalMainRepository {

    override fun getSongs(): List<Song> {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = applicationContext.contentResolver.query(uri, null, null, null, null)
        val localSongs = mutableListOf<Song>()

        if((cursor?.count ?: -1) > 0) {
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
                    val duration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        ?: cursor.getString(durationColumnIndex)

                    val artistNameColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                    val artistName = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
                        ?: cursor.getString(artistNameColumnIndex)


                    val albumIdCol = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                    val albumId = cursor.getLong(albumIdCol)
                    var albumArt: Bitmap? =
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                val extUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                                val songUri = ContentUris.withAppendedId(extUri, id)

                                applicationContext.contentResolver.loadThumbnail(
                                    songUri,
                                    Size(400, 400),
                                    null
                                )
                            } else {

                                val sArtWorkUri = Uri.parse("content://media/external/audio/album")
                                val albumArtUri =
                                    ContentUris.withAppendedId(sArtWorkUri, albumId)

                                MediaStore.Images.Media.getBitmap(
                                    applicationContext.contentResolver,
                                    albumArtUri
                                )
                            }
                        } catch (e: Exception) { null }


                    val genreNameColumnIndex =
                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R)
                            cursor.getColumnIndex(MediaStore.Audio.Media.GENRE)
                        else -1
                    val genre = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
                        ?: if(genreNameColumnIndex >= 0)cursor.getString(genreNameColumnIndex) ?: "Unknown" else "Unknown"


                    val embeddedPicture = metadataRetriever.embeddedPicture

                    if (albumArt == null && embeddedPicture != null && embeddedPicture.isNotEmpty()) {
                        albumArt = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.size)
                    }

                    val artistIdCol = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)
                    val artistId = cursor.getLong(artistIdCol)

                    val modificationDate = File(songPath).lastModified()

                    val song = Song(
                        title = title,
                        album = if(albumName == "Download") "Unknown album" else albumName,
                        duration = duration.toInt(),
                        artist = if(artistName == "<unknown>") "Unknown artist" else artistName,
                        genre = genre,
                        path = songPath,
                        picture = albumArt,
                        id = id,
                        albumId = albumId,
                        artistId = artistId,
                        lastModified = modificationDate
                    )

                    localSongs += song

                } catch (e: Exception) {
                    Log.e("fileReadingError", "Couldn't read the media file", e)
                }

            } while (cursor.moveToNext())
            cursor.close()

        }
        return localSongs
    }
}