package com.example.ingale.feature_local.local_core.domain.model

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.example.ingale.core.domain.model.Song
import com.example.ingale.mvi.wrappers.StableList
import java.io.Serializable

@Immutable
data class SongsSet(
    val id: Long,
    val title: String,
    val songs: StableList<Song>,
    val icon: Bitmap?
) : Serializable