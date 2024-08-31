package com.nightx.ingale.featureLocal.featureHome.presentation.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nightx.ingale.bottomBar.api.BottomBarEffect
import com.nightx.ingale.bottomBar.api.SetBottomBarState
import com.nightx.ingale.core.presentation.diExt.ingaleViewModels
import com.nightx.ingale.core.presentation.flows.ObserveEffects
import com.nightx.ingale.core.ui.DataPlaceholder
import com.nightx.ingale.core.ui.LoadingStatePresenter
import com.nightx.ingale.core.ui.ObserveState
import com.nightx.ingale.core.ui.OnLifecycleEvents
import com.nightx.ingale.core.ui.modifierExt.marquee
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.featureLocal.core.ui.SongsLazyList
import com.nightx.ingale.featureLocal.featureHome.presentation.ui.components.CommonSongSetsGrid
import com.nightx.ingale.featureLocal.featureHome.presentation.ui.components.SearchTextField
import com.nightx.ingale.featureLocal.featureHome.presentation.ui.components.SongsSetButtons
import com.nightx.ingale.featureLocal.featureHome.presentation.ui.components.TabRow
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.Effect
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.LocalMainCallbacks
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.LocalMainViewModel
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.viewState.LocalMainScreenViewState
import com.nightx.ingale.featureLocal.featureRequirePermissions.RequiredPermissionsRequesterDialog
import com.nightx.ingale.musicbar.musicBarControls.LocalMusicBarController
import com.nightx.ingale.musicbar.musicBarControls.MusicBarEffect
import com.nightx.ingale.musicbar.view.rememberNestedScrollForMusicBarNotification
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import com.nightx.ingale.resources.strings.R.string as Strings

@Composable
fun LocalMainScreen() {
    val vm = ingaleViewModels<LocalMainViewModel>()

    OnLifecycleEvents { event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            vm.onResume()
        }
    }

    SetBottomBarState(BottomBarEffect.ShowBottomBar)

    RequiredPermissionsRequesterDialog(vm.permissionsDialogComponentHolder)

    LocalMainScreen(
        state = vm.state.collectAsStateWithLifecycle().value,
        callbacks = vm,
        effects = vm.effect
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LocalMainScreen(
    state: LocalMainScreenViewState,
    callbacks: LocalMainCallbacks,
    effects: Flow<Effect>,
) {
    val songsLazyColumnState = rememberLazyListState()
    val albumsGridsState = rememberLazyGridState()
    val artistsGridsState = rememberLazyGridState()


    ObserveEffects(effects) { effect ->
        when (effect) {
            Effect.ScrollToTop -> {
                delay(100) // delay for the list to have time to update
                if (songsLazyColumnState.canScrollBackward) {
                    songsLazyColumnState.animateScrollToItem(0)
                }
                if (albumsGridsState.canScrollBackward) {
                    albumsGridsState.animateScrollToItem(0)
                }
                if (artistsGridsState.canScrollBackward) {
                    artistsGridsState.animateScrollToItem(0)
                }
            }
        }
    }

    val pagerState = rememberPagerState(
        pageCount = { state.sections.size }
    )
    val musicBarController = LocalMusicBarController.current

    ObserveState(pagerState.currentPage) {
        musicBarController.sendEffect(MusicBarEffect.ExpandMusicBar)
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
            userScrollEnabled = state.loadingState.isError.not()
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
            userScrollEnabled = state.loadingState.isError.not()
        ) {
            when (it) {
                0 -> {
                    LoadingStatePresenter(
                        loadingState = state.loadingState,
                        notErrorView = {
                            SongsLazyList(
                                modifier = Modifier
                                    .nestedScroll(rememberNestedScrollForMusicBarNotification()),
                                lazyListState = songsLazyColumnState,
                                songs = state.songs,
                                query = state.searchTextField,
                                isLoading = state.loadingState.isLoading,
                                onSongClick = callbacks::onSongClick
                            )
                        },
                        errorView = {
                            DataPlaceholder(
                                title =
                                if (state.isPermissionError) {
                                    stringResource(Strings.permission_required)
                                } else {
                                    stringResource(Strings.no_songs_found)
                                },
                                description =
                                if (state.isPermissionError) {
                                    stringResource(Strings.audio_permission_not_granted_description)
                                } else null,
                                actionButtonText =
                                if (state.isPermissionError) {
                                    stringResource(Strings.go_to_settings)
                                } else {
                                    stringResource(Strings.refresh)
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
                        items = state.albums,
                        onClick = callbacks::onAlbumClick,
                        isLoading = state.loadingState.isLoading,
                        query = state.searchTextField
                    )
                }

                2 -> {
                    CommonSongSetsGrid(
                        modifier = Modifier
                            .nestedScroll(rememberNestedScrollForMusicBarNotification()),
                        lazyGridState = artistsGridsState,
                        items = state.artists,
                        onClick = callbacks::onArtistClick,
                        isLoading = state.loadingState.isLoading,
                        query = state.searchTextField
                    )
                }
            }
        }
    }
}
