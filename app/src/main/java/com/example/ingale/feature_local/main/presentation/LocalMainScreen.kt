package com.example.ingale.feature_local.main.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.ingale.core.presentation.flows.ObserveEffects
import com.example.ingale.core.presentation.ui.DataPlaceholder
import com.example.ingale.core.presentation.ui.LoadingStatePresenter
import com.example.ingale.feature_local.local_core.domain.model.SongsSet
import com.example.ingale.feature_local.local_core.presentation.SongsLazyList
import com.example.ingale.feature_local.main.presentation.ui_components.SongsSetButton
import com.example.ingale.feature_local.main.presentation.ui_components.TabRow
import com.example.ingale.feature_local.main.presentation.ui_components.songs_section.CommonSongSetsGrid
import com.example.ingale.feature_local.main.presentation.view.LocalMainContract
import com.example.ingale.feature_local.main.presentation.view.LocalMainContract.Effect
import com.example.ingale.feature_local.main.presentation.view.LocalMainContract.Event
import com.example.ingale.feature_local.main.presentation.view.LocalMainViewModel.Companion.PERMISSION_NOT_GRANTED_ERROR
import com.example.ingale.main_navigation.bottom_bar_controls.BottomBarController
import com.example.ingale.main_navigation.bottom_bar_controls.BottomBarEffect
import com.example.ingale.main_navigation.bottom_bar_controls.LocalBottomBarController
import com.example.ingale.main_navigation.bottom_bar_controls.SendBottomBarEffect
import com.example.ingale.mvi.wrappers.StableList
import com.example.ingale.ui.theme.spacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.inject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocalMainScreen(
    state: LocalMainContract.State,
    sendEvent: (event: Event) -> Unit,
    effects: Flow<Effect>,
) {
    val songsLazyColumnState = rememberLazyListState()
    val albumsGridsState = rememberLazyGridState()
    val artistsGridsState = rememberLazyGridState()

    SendBottomBarEffect(BottomBarEffect.ShowBottomBar)
    ObserveEffects(effects) { effect ->
        when (effect) {
            Effect.ScrollToTop -> {
                delay(100)
                songsLazyColumnState.scrollToItem(0)
                albumsGridsState.animateScrollToItem(0)
                artistsGridsState.animateScrollToItem(0)
            }
        }
    }

    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState(
        pageCount = { state.sections.size }
    )
    val bottomBarController = LocalBottomBarController.current
    LaunchedEffect(selectedTabIndex) {
        bottomBarController.sendEffect(BottomBarEffect.ExpandMusicInfo)
        pagerState.animateScrollToPage(
            page = selectedTabIndex
        )
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(
                top = 8.dp
            )
    ) {
        OutlinedTextField(
            placeholder = {
                Text(
                    text = "Search",
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            value = state.searchTextField,
            onValueChange = {
                sendEvent(Event.Search(it))
            },
            suffix = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove search text",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.clickable {
                        sendEvent(Event.Search(""))
                    }
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFF5C5C5C),
                    shape = RoundedCornerShape(5000.dp)
                )
                .clip(
                    shape = RoundedCornerShape(5000.dp)
                ),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.normal),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.normal)
        ) {
            SongsSetButton(
                modifier = Modifier.weight(1f, true),
                gradientWeak = Color(0xFF087700),
                text = "Playlists",
                onClick = {}
            )
            SongsSetButton(
                modifier = Modifier.weight(1f, true),
                gradientWeak = Color(0xFFFF6F00),
                text = "Favourites",
                onClick = {}
            )
            SongsSetButton(
                modifier = Modifier.weight(1f, true),
                gradientWeak = Color(0xFF0040E0),
                text = "History",
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedItemIndex = { selectedTabIndex },
            items = state.sections,
            itemContent = { title ->
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(MaterialTheme.spacing.large)
                )
            },
            onSelect = { index, _ ->
                selectedTabIndex = index
            },
        )
        HorizontalPager(
            state = pagerState,
            key = { it },
            beyondBoundsPageCount = 2
        ) {
            when (it) {
                0 -> {
                    LoadingStatePresenter(
                        loadingState = state.songLoadingState,
                        notErrorView = {
                            SongsLazyList(
                                modifier = Modifier
                                    .nestedScroll(NestedScrollForMusicBarNotification),
                                lazyListState = songsLazyColumnState,
                                songs = state.allSongs,
                                query = state.searchTextField,
                                isLoading = state.songLoadingState.isLoading,
                                onSongClick = { song ->
                                    sendEvent(Event.PlaySong(song))
                                }
                            )
                        },
                        errorView = { error ->
                            DataPlaceholder(
                                title =
                                    if (error.message == PERMISSION_NOT_GRANTED_ERROR) {
                                        PERMISSION_NOT_GRANTED_TITLE
                                    } else {
                                        NO_SONGS_FOUND_MESSAGE
                                    },
                                description =
                                    if (error.message == PERMISSION_NOT_GRANTED_ERROR) {
                                        PERMISSION_NOT_GRANTED_MESSAGE
                                    } else null,
                                onAction = {
                                    sendEvent(Event.Refresh)
                                }
                            )
                        }
                    )
                }

                1 -> {
                    CommonSongSetsGrid(
                        modifier = Modifier
                            .nestedScroll(NestedScrollForMusicBarNotification),
                        lazyGridState = albumsGridsState,
                        items = StableList(state.albums.map { album ->
                            SongsSet(
                                id = album.albumId,
                                songs = album.songs,
                                title = album.albumName,
                                icon = null
                            )
                        }),
                        onClick = { songsSet ->
                            sendEvent(Event.AlbumClicked(songsSet))
                        },
                        isLoading = state.songLoadingState.isLoading,
                        query = state.searchTextField
                    )
                }

                2 -> {
                    CommonSongSetsGrid(
                        modifier = Modifier
                            .nestedScroll(NestedScrollForMusicBarNotification),
                        lazyGridState = artistsGridsState,
                        items = StableList(state.artists.map { artist ->
                            SongsSet(
                                id = artist.artistId,
                                songs = artist.songs,
                                title = artist.artistName,
                                icon = null
                            )
                        }),
                        onClick = { songsSet ->
                            sendEvent(Event.ArtistClicked(songsSet))
                        },
                        isLoading = state.songLoadingState.isLoading,
                        query = state.searchTextField
                    )
                }
            }
        }
    }
}

private object NestedScrollForMusicBarNotification : NestedScrollConnection {

    private val bottomBarController by inject<BottomBarController>(BottomBarController::class.java)
    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        if (consumed.y < -1f) {
            BottomBarEffect.CollapseMusicInfo
            bottomBarController.sendEffect(BottomBarEffect.CollapseMusicInfo)
        } else if (consumed.y > 1f) {
            BottomBarEffect.ExpandMusicInfo
            bottomBarController.sendEffect(BottomBarEffect.ExpandMusicInfo)
        }

        return super.onPostScroll(consumed, available, source)
    }
}

private const val PERMISSION_NOT_GRANTED_TITLE =
    "Grant Permission"
private const val PERMISSION_NOT_GRANTED_MESSAGE =
    "Please grant audio permission access so that we could read your audio files"

private const val NO_SONGS_FOUND_MESSAGE =
    "Couldn't find any songs"
