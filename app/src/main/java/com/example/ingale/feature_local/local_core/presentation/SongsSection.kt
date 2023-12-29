package com.example.ingale.feature_local.local_core.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import com.example.ingale.core.domain.model.Song
import com.example.ingale.feature_local.local_core.presentation.song_item.LoadingSongItem
import com.example.ingale.feature_local.local_core.presentation.song_item.SongItem
import com.example.ingale.mvi.wrappers.StableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongsSection(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    songs: StableList<Song>,
    isLoading: Boolean,
    query: String = "",
    onSongClick: (Song) -> Unit
) {
    if(!isLoading) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = lazyListState
        ) {
            items(
                items = songs,
                key = { it.id }
            ) {
                SongItem(
                    modifier = Modifier
                        .animateItemPlacement(tween(300)),
                    song = it,
                    highlightedPart = query,
                    onSongClick = onSongClick,
                    onSongOperation = {}
                )
            }
        }
    } else {
        var freeHeight by remember { mutableIntStateOf(1) }
        var itemHeight by remember { mutableIntStateOf(1) }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged {
                    freeHeight = it.height
                },
            userScrollEnabled = false
        ) {
            item {
                LoadingSongItem(
                    modifier = Modifier.onSizeChanged {
                        itemHeight = it.height
                    }
                )
            }
            items(freeHeight/itemHeight + 1) {
                LoadingSongItem()
            }
        }
    }
}