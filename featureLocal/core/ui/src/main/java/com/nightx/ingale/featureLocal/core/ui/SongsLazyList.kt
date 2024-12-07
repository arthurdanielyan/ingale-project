package com.nightx.ingale.featureLocal.core.ui

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
import com.nightx.featureLocal.core.viewState.song.SongViewState
import com.nightx.ingale.core.viewState.StableList
import com.nightx.ingale.featureLocal.core.ui.song_item.LoadingSongItem
import com.nightx.ingale.featureLocal.core.ui.song_item.SongItem

@Composable
fun SongsLazyList(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    songs: StableList<SongViewState>,
    isLoading: Boolean = false,
    header: @Composable (() -> Unit)? = null,
    query: String = "",
    onSongClick: (SongViewState) -> Unit,
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
        )
    }
}

@Composable
private fun SongsListContent(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    songs: StableList<SongViewState>,
    header: @Composable (() -> Unit)? = null,
    query: String = "",
    onSongClick: (SongViewState) -> Unit,
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
        ) {
            SongItem(
                modifier = Modifier.animateItem(
                    fadeInSpec = if (animateItemAppearance) tween(FadeAnimationDuration) else null,
                    fadeOutSpec = if (animateItemAppearance) tween(FadeAnimationDuration) else null,
                    placementSpec = tween(PlacementAnimationDuration)
                ),
                song = it,
                highlightedPart = query,
                onSongClick = onSongClick,
                onSongOperation = {}
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
