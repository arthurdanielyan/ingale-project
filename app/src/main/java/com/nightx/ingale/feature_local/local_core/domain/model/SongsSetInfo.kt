package com.nightx.ingale.feature_local.local_core.domain.model

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.mvi.wrappers.StableList
import java.io.Serializable

@Immutable
data class SongsSetInfo(
    val title: String,
    val songs: StableList<Song>,
    val icon: Bitmap?
) : Serializable