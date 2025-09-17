package com.nightx.ingale.featureLocal.featureHome.presentation.api

import androidx.compose.runtime.Immutable
import com.nightx.ingale.core.viewState.ComposeList
import com.nightx.ingale.core.viewState.LoadingViewState
import com.nightx.ingale.core.viewState.composeListOf
import com.nightx.ingale.core.viewState.emptyComposeList
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.SongViewState
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.SongsSetViewState
import com.nightx.ingale.resources.strings.R.string as Strings

@Immutable
data class LocalMainScreenViewState(
    val searchTextField: String = "",
    val songs: ComposeList<SongViewState> = emptyComposeList(),
    val albums: ComposeList<SongsSetViewState> = emptyComposeList(),
    val artists: ComposeList<SongsSetViewState> = emptyComposeList(),
    val loadingState: LoadingViewState = LoadingViewState.Loading,
) {

    companion object {
        const val PERMISSION_NOT_GRANTED_ERROR = "no_audio_permission_granted"
    }

    val sections: ComposeList<Int> = composeListOf(
        Strings.section_songs, Strings.section_albums, Strings.section_artist
    )

    val isPermissionError: Boolean
        get() {
            return loadingState is LoadingViewState.Error &&
                    loadingState.message == PERMISSION_NOT_GRANTED_ERROR
        }
}