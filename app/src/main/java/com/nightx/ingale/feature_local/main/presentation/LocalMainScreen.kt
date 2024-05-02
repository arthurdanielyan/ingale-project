package com.nightx.ingale.feature_local.main.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nightx.ingale.core.presentation.di.ingaleViewModels
import com.nightx.ingale.core.presentation.flows.ObserveEffects
import com.nightx.ingale.core.presentation.ui.DataPlaceholder
import com.nightx.ingale.core.presentation.ui.LoadingStatePresenter
import com.nightx.ingale.core.presentation.ui.marquee
import com.nightx.ingale.core.presentation.view.ObserveState
import com.nightx.ingale.core.presentation.view.rememberNestedScrollForMusicBarNotification
import com.nightx.ingale.feature_local.local_core.domain.model.SongsSet
import com.nightx.ingale.feature_local.local_core.presentation.SongsLazyList
import com.nightx.ingale.feature_local.main.presentation.ui_components.SongsSetButton
import com.nightx.ingale.feature_local.main.presentation.ui_components.TabRow
import com.nightx.ingale.feature_local.main.presentation.ui_components.songs_section.CommonSongSetsGrid
import com.nightx.ingale.feature_local.main.presentation.view.LocalMainCallbacks
import com.nightx.ingale.feature_local.main.presentation.view.LocalMainContract
import com.nightx.ingale.feature_local.main.presentation.view.LocalMainContract.Effect
import com.nightx.ingale.feature_local.main.presentation.view.LocalMainViewModel
import com.nightx.ingale.feature_local.main.presentation.view.LocalMainViewModel.Companion.PERMISSION_NOT_GRANTED_ERROR
import com.nightx.ingale.main_navigation.bottom_bar_controls.BottomBarEffect
import com.nightx.ingale.main_navigation.bottom_bar_controls.LocalBottomBarController
import com.nightx.ingale.main_navigation.bottom_bar_controls.SendBottomBarEffect
import com.nightx.ingale.mvi.wrappers.StableList
import com.nightx.ingale.ui.theme.colorScheme.ingaleColors
import com.nightx.ingale.ui.theme.spacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@Composable
fun LocalMainScreen() {
    val vm = ingaleViewModels<LocalMainViewModel>()
    LocalMainScreen(
        state = vm.state.collectAsStateWithLifecycle().value,
        callbacks = vm,
        effects = vm.effect
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LocalMainScreen(
    state: LocalMainContract.State,
    callbacks: LocalMainCallbacks,
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
                albumsGridsState.scrollToItem(0)
                artistsGridsState.scrollToItem(0)
            }
        }
    }

    val pagerState = rememberPagerState(
        pageCount = { state.sections.size }
    )
    val bottomBarController = LocalBottomBarController.current

    val isPagerScrolling = remember {
        derivedStateOf {
            pagerState.currentPageOffsetFraction != 0f
        }
    }

    ObserveState(isPagerScrolling.value) {
        if(it) {
            bottomBarController.sendEffect(BottomBarEffect.ExpandMusicBar)
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
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }
        val isFocused by interactionSource.collectIsFocusedAsState()
        BackHandler(enabled = isFocused) {
            focusManager.clearFocus()
        }
        OutlinedTextField(
            interactionSource = interactionSource,
            placeholder = {
                Text(
                    text = "Search",
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            value = state.searchTextField,
            onValueChange = callbacks::onSearchType,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove search text",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.clickable {
                        callbacks.onSearchType("")
                    }
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = CircleShape,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.normal),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.normal)
        ) {
            SongsSetButton(
                modifier = Modifier
                    .weight(1f, true),
                gradientWeak = MaterialTheme.ingaleColors.additional1,
                text = "Playlists",
                onClick = {}
            )
            SongsSetButton(
                modifier = Modifier
                    .weight(1f, true),
                gradientWeak = MaterialTheme.ingaleColors.additional2,
                text = "Favourites",
                onClick = {}
            )
            SongsSetButton(
                modifier = Modifier
                    .weight(1f, true),
                gradientWeak = MaterialTheme.ingaleColors.additional3,
                text = "History",
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            pagerState = pagerState,
            items = state.sections,
            itemContent = { title ->
                Text(
                    text = stringResource(id = title),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.large)
                        .marquee()
                )
            }
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
                                    .nestedScroll(rememberNestedScrollForMusicBarNotification()),
                                lazyListState = songsLazyColumnState,
                                songs = state.allSongs,
                                query = state.searchTextField,
                                isLoading = state.songLoadingState.isLoading,
                                onSongClick = callbacks::onSongClick
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
                                onAction = callbacks::refresh
                            )
                        }
                    )
                }

                1 -> {
                    CommonSongSetsGrid(
                        modifier = Modifier
                            .nestedScroll(rememberNestedScrollForMusicBarNotification()),
                        lazyGridState = albumsGridsState,
                        items = StableList(state.albums.map { album ->
                            SongsSet(
                                id = album.albumId,
                                songs = album.songs,
                                title = album.albumName
                            )
                        }),
                        onClick = callbacks::onAlbumClick,
                        isLoading = state.songLoadingState.isLoading,
                        query = state.searchTextField
                    )
                }

                2 -> {
                    CommonSongSetsGrid(
                        modifier = Modifier
                            .nestedScroll(rememberNestedScrollForMusicBarNotification()),
                        lazyGridState = artistsGridsState,
                        items = StableList(state.artists.map { artist ->
                            SongsSet(
                                id = artist.artistId,
                                songs = artist.songs,
                                title = artist.artistName
                            )
                        }),
                        onClick = callbacks::onArtistClick,
                        isLoading = state.songLoadingState.isLoading,
                        query = state.searchTextField
                    )
                }
            }
        }
    }
}

private const val PERMISSION_NOT_GRANTED_TITLE =
    "Grant Permission"
private const val PERMISSION_NOT_GRANTED_MESSAGE =
    "Please grant audio permission access so that we could read your audio files"

private const val NO_SONGS_FOUND_MESSAGE =
    "Couldn't find any songs"
