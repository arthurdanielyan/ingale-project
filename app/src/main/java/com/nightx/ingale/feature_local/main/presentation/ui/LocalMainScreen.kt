package com.nightx.ingale.feature_local.main.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nightx.ingale.R
import com.nightx.ingale.core.presentation.di.ingaleViewModels
import com.nightx.ingale.core.presentation.flows.ObserveEffects
import com.nightx.ingale.core.presentation.ui.DataPlaceholder
import com.nightx.ingale.core.presentation.ui.LoadingStatePresenter
import com.nightx.ingale.core.presentation.ui.modifierExt.marquee
import com.nightx.ingale.core.presentation.view.ObserveState
import com.nightx.ingale.core.presentation.view.OnLifecycleEvents
import com.nightx.ingale.core.presentation.view.rememberNestedScrollForMusicBarNotification
import com.nightx.ingale.feature_local.local_core.domain.model.SongsSet
import com.nightx.ingale.feature_local.local_core.presentation.SongsLazyList
import com.nightx.ingale.feature_local.main.presentation.ui.uiComponents.CommonSongSetsGrid
import com.nightx.ingale.feature_local.main.presentation.ui.uiComponents.SearchTextField
import com.nightx.ingale.feature_local.main.presentation.ui.uiComponents.SongsSetButtons
import com.nightx.ingale.feature_local.main.presentation.ui.uiComponents.TabRow
import com.nightx.ingale.feature_local.main.presentation.view.viewModel.LocalMainCallbacks
import com.nightx.ingale.feature_local.main.presentation.view.viewModel.LocalMainContract
import com.nightx.ingale.feature_local.main.presentation.view.viewModel.LocalMainContract.Effect
import com.nightx.ingale.feature_local.main.presentation.view.viewModel.LocalMainViewModel
import com.nightx.ingale.main_navigation.bottomBarControls.BottomBarEffect
import com.nightx.ingale.main_navigation.bottomBarControls.LocalBottomBarController
import com.nightx.ingale.main_navigation.bottomBarControls.SendBottomBarEffect
import com.nightx.ingale.mvi.wrappers.StableList
import com.nightx.ingale.ui.theme.dimensions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@Composable
fun LocalMainScreen() {
    val vm = ingaleViewModels<LocalMainViewModel>()

    OnLifecycleEvents { event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            vm.onResume()
        }
    }

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
        if (it) {
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
        SearchTextField(
            text = state.searchTextField,
            onType = callbacks::onSearchType
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.normal))
        SongsSetButtons(
            onPlaylistsClick = callbacks::onPlaylistsClick,
            onFavouritesClick = callbacks::onFavouritesClick,
            onHistoryClick = callbacks::onHistoryClick
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.large))
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            pagerState = pagerState,
            items = state.sections,
            userScrollEnabled = state.songLoadingState.isError.not()
        ) { title ->
            Text(
                text = stringResource(id = title),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                modifier = Modifier
                    .padding(MaterialTheme.dimensions.large)
                    .marquee()
            )
        }
        HorizontalPager(
            state = pagerState,
            key = { it },
            beyondBoundsPageCount = 2,
            userScrollEnabled = state.songLoadingState.isError.not()
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
                        errorView = {
                            DataPlaceholder(
                                title =
                                if (state.isPermissionError) {
                                    stringResource(R.string.permission_required)
                                } else {
                                    stringResource(R.string.no_songs_found)
                                },
                                description =
                                if (state.isPermissionError) {
                                    stringResource(R.string.audio_permission_not_granted_description)
                                } else null,
                                actionButtonText =
                                if (state.isPermissionError) {
                                    stringResource(R.string.go_to_settings)
                                } else {
                                    stringResource(R.string.refresh)
                                },
                                onAction = if (state.isPermissionError)
                                    callbacks::onGoToSettingsClick
                                else callbacks::refreshSongs
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
