package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import com.nightx.ingale.core.viewState.ComposeList
import com.nightx.ingale.featureLocal.core.presentation.common.viewState.SongViewState
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.ui.components.song_item.LoadingSongItem
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songsList.ui.components.song_item.SongItem

// TODO: make private
@Composable
fun SongsLazyList(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    songs: ComposeList<SongViewState>,
    isLoading: Boolean = false,
    header: @Composable (() -> Unit)? = null,
    query: String = "",
    onSongClick: (SongViewState) -> Unit,
    onSongOperationsClick: (SongViewState) -> Unit = {},
) {
    if (isLoading) {
        SongsListSkeleton(
            modifier = modifier
        )
    } else {
        SongsListContent(
            modifier = modifier,
            lazyListState = lazyListState,
            songs = songs,
            header = header,
            query = query,
            onSongClick = onSongClick,
            onSongOperationsClick = onSongOperationsClick,
        )
    }
}

@Composable
private fun SongsListContent(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    songs: ComposeList<SongViewState>,
    header: @Composable (() -> Unit)? = null,
    query: String = "",
    onSongClick: (SongViewState) -> Unit,
    onSongOperationsClick: (SongViewState) -> Unit = {},
) {
    var firstLoad by remember { mutableStateOf(true) }
    var animateItemAppearance by remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        if (firstLoad) {
            firstLoad = false
        } else {
            animateItemAppearance = true
        }
    }


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        state = lazyListState,
    ) {
        if (header != null) {
            item(key = "header") {
                header()
            }
        }
        items(
            items = songs,
            key = { it.id }
        ) { song ->
            SongItem(
                modifier = Modifier.animateItem(
                    fadeInSpec = if (animateItemAppearance) tween(FadeAnimationDuration) else null,
                    fadeOutSpec = if (animateItemAppearance) tween(FadeAnimationDuration) else null,
                    placementSpec = tween(PlacementAnimationDuration)
                ),
                song = song,
                highlightedPart = query,
                onSongClick = onSongClick,
                onSongOperationsClick = {
                    onSongOperationsClick(song)
                }
            )
        }
    }
}

@Composable
private fun SongsListSkeleton(
    modifier: Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val freeHeight = constraints.maxHeight
        var itemHeight by remember { mutableIntStateOf(1) }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            userScrollEnabled = false
        ) {
            item {
                LoadingSongItem(
                    modifier = Modifier.onSizeChanged {
                        itemHeight = it.height
                    }
                )
            }
            items(freeHeight / itemHeight + 1) {
                LoadingSongItem()
            }
        }
    }
}

private const val PlacementAnimationDuration = 300
private const val FadeAnimationDuration = 200
