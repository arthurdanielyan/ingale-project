package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.ui.SongOperationsParentComponentView
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api.SongsListCallbacks
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api.SongsListComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.api.SongsListViewState
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.ui.components.SongsLazyList

@Composable
fun SongsList(
    component: SongsListComponent,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    isLoading: Boolean = false,
    header: @Composable (() -> Unit)? = null,
    query: String = "",
) {
    val uiState by component.uiState.collectAsState()
    val callbacks = component.uiCallbacks

    SongsListContent(
        uiState = uiState,
        callbacks = callbacks,
        modifier = modifier,
        lazyListState = lazyListState,
        isLoading = isLoading,
        header = header,
        query = query,
    )

    SongOperationsParentComponentView(
        component = component.songOperationsParentComponent
    )
}

@Composable
private fun SongsListContent(
    uiState: SongsListViewState,
    callbacks: SongsListCallbacks,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    isLoading: Boolean = false,
    header: @Composable (() -> Unit)? = null,
    query: String = "",
) {
    SongsLazyList(
        modifier = modifier,
        lazyListState = lazyListState,
        songs = uiState.songs,
        isLoading = isLoading,
        header = header,
        query = query,
        onSongClick = callbacks::onSongClick,
        onSongOperationsClick = callbacks::onSongOperationsClick,
    )
}
