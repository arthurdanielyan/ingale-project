package com.nightx.ingale.core.audio_player

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.nightx.ingale.R
import com.nightx.ingale.core.domain.model.Song
import org.koin.java.KoinJavaComponent.inject


object AudioPlayer {

    private val applicationContext by inject<Context>(Context::class.java)

    var isPlaying = false

    val defaultSongBitmap: Bitmap
        get() = ContextCompat
            .getDrawable(applicationContext, R.mipmap.ic_launcher)!!.toBitmap()

    val currentSongBitmap: Bitmap
        get() = currentSong.picture ?: defaultSongBitmap

    val currentSong: Song
        get() {
            return songQueue[pointer]
        }

    val currentSongNumber: Long
        get() = pointer.toLong()
    val songCount: Long
        get() = songQueue.size.toLong()

    var songQueue: List<Song> = emptyList()
        private set(value) {
            field = value
        }

    var pointer = 0
        set(value) {
            field = if(value in songQueue.indices) value else
                if(value < 0) songQueue.lastIndex else 0
        }


    fun play(songQueue: List<Song>, indexToPlay: Int) {
        require(indexToPlay in songQueue.indices)
        this.songQueue = songQueue
        pointer = indexToPlay
        applicationContext.startService(Intent(applicationContext, PlayerService::class.java))
    }
}