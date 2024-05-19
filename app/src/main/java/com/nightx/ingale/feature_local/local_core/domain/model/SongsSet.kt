package com.nightx.ingale.feature_local.local_core.domain.model

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.mvi.wrappers.StableList

@Immutable
data class SongsSet(
    val id: Long,
    val title: String,
    val songs: StableList<Song>,
    val iconPath: String = ""
)