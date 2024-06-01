package com.nightx.ingale.feature_local.main.presentation.view.viewModel

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.nightx.ingale.core.audio_player.actions.PlayerUiActions
import com.nightx.ingale.core.domain.LoadState
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.core.presentation.view.LoadingViewState
import com.nightx.ingale.feature_local.local_core.domain.model.SongsSet
import com.nightx.ingale.feature_local.local_navigation.destinations.SongsSetScreenDestination
import com.nightx.ingale.feature_local.local_navigation.screen_navigation.LocalNavigator
import com.nightx.ingale.feature_local.main.domain.model.Album
import com.nightx.ingale.feature_local.main.domain.model.Artist
import com.nightx.ingale.feature_local.main.domain.model.SongsSeparation
import com.nightx.ingale.feature_local.main.domain.usecases.FilterUseCase
import com.nightx.ingale.feature_local.main.domain.usecases.GetSongsUseCase
import com.nightx.ingale.feature_local.main.domain.usecases.OrganizeSongsUseCase
import com.nightx.ingale.feature_local.main.presentation.view.RequiredPermissionsInspector
import com.nightx.ingale.feature_local.main.presentation.view.mappers.SongsSetToNavArgMapper
import com.nightx.ingale.feature_local.main.presentation.view.viewModel.LocalMainContract.Effect
import com.nightx.ingale.feature_local.main.presentation.view.viewModel.LocalMainContract.State
import com.nightx.ingale.main_navigation.bottomBarControls.BottomBarController
import com.nightx.ingale.main_navigation.bottomBarControls.BottomBarEffect
import com.nightx.ingale.mvi.BaseViewModel
import com.nightx.ingale.mvi.wrappers.emptyStableList
import com.nightx.ingale.mvi.wrappers.toStableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class LocalMainViewModel(
    private val navigator: LocalNavigator,
    private val getSongsUseCase: GetSongsUseCase,
    private val organizeSongsUseCase: OrganizeSongsUseCase,
    private val filterUseCase: FilterUseCase,
    private val bottomBarController: BottomBarController,
    private val requiredPermissionsInspector: RequiredPermissionsInspector,
    private val songsSetToNavArgMapper: SongsSetToNavArgMapper,
    private val applicationContext: Context,
    private val playerUiActions: PlayerUiActions,
) : BaseViewModel<State, Effect>(), LocalMainCallbacks {

    companion object {
        const val NO_SONGS_FOUND_ERROR = "no_audio_files_found"
        const val PERMISSION_NOT_GRANTED_ERROR = "no_audio_permission_granted"
    }

    private lateinit var allSongs: List<Song>
    private lateinit var allAlbums: List<Album>
    private lateinit var allArtists: List<Artist>

    private var wasAudioPermissionGrantedReceived = false

    private val loadSongsRequest =
        MutableSharedFlow<Unit>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )


    override fun defineInitialState(): State =
        State(
            searchTextField = "",
            allSongs = emptyStableList(),
            albums = emptyStableList(),
            artists = emptyStableList(),
            songLoadingState = LoadingViewState.Loading
        )

    init {
        observeLoadSongsRequest()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeLoadSongsRequest() {
        viewModelScope.launch {
            loadSongsRequest
                .filter { isAudioPermissionGranted }
                .flatMapLatest {
                    updateState {
                        copy(songLoadingState = LoadingViewState.Loading)
                    }
                    getSongsUseCase()
                }.collectLatest { songsLoadState ->
                    when (songsLoadState) {
                        is LoadState.Error -> {

                        }

                        is LoadState.Loading -> {
                            updateState {
                                copy(songLoadingState = LoadingViewState.Loading)
                            }
                        }

                        is LoadState.Success -> {
                            val songsSeparation = organizeSongsUseCase(songsLoadState.data)
                            if (songsSeparation.songs.isNotEmpty()) {
                                allSongs = songsSeparation.songs
                                allAlbums = songsSeparation.albums
                                allArtists = songsSeparation.artists
                                updateState {
                                    copy(
                                        songLoadingState = LoadingViewState.Success,
                                        allSongs = songsSeparation.songs.toStableList(),
                                        albums = songsSeparation.albums.toStableList(),
                                        artists = songsSeparation.artists.toStableList(),
                                    )
                                }
                                search(currentState.searchTextField, false)
                            } else {
                                updateState {
                                    copy(
                                        songLoadingState = LoadingViewState.Error(
                                            if (isAudioPermissionGranted) {
                                                NO_SONGS_FOUND_ERROR
                                            } else {
                                                PERMISSION_NOT_GRANTED_ERROR
                                            }
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun search(query: String, scrollToTop: Boolean) {
        updateState {
            copy(
                searchTextField = query
            )
        }
        if (!this@LocalMainViewModel::allSongs.isInitialized
            || !::allAlbums.isInitialized
            || !::allArtists.isInitialized
        ) return
        viewModelScope.launch {
            val songSeparation =
                filterUseCase(query, SongsSeparation(allSongs, allAlbums, allArtists))
            updateState {
                copy(
                    allSongs = songSeparation.songs.toStableList(),
                    albums = songSeparation.albums.toStableList(),
                    artists = songSeparation.artists.toStableList()
                )
            }
            if (scrollToTop)
                sendEffect {
                    Effect.ScrollToTop
                }
        }
    }

    private val isAudioPermissionGranted: Boolean
        get() {
            val audioPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
            return ContextCompat.checkSelfPermission(
                applicationContext,
                audioPermission
            ) == PackageManager.PERMISSION_GRANTED
        }

    override fun onResume() {
        requiredPermissionsInspector.start(
            shouldReloadSongs = {
                if (it) {
                    if(!wasAudioPermissionGrantedReceived) {
                        refreshSongs()
                        wasAudioPermissionGrantedReceived = true
                    }
                } else {
                    updateState {
                        copy(
                            songLoadingState = LoadingViewState.Error(
                                PERMISSION_NOT_GRANTED_ERROR
                            )
                        )
                    }
                }
            }
        )
    }

    override fun onAlbumClick(songsSet: SongsSet) {
        bottomBarController.sendEffect(BottomBarEffect.HideBottomBar)
        navigator.navigate(SongsSetScreenDestination, songsSetToNavArgMapper(songsSet))
    }

    override fun onArtistClick(songsSet: SongsSet) {
        bottomBarController.sendEffect(BottomBarEffect.HideBottomBar)
        navigator.navigate(SongsSetScreenDestination, songsSetToNavArgMapper(songsSet))
    }

    override fun onSearchType(query: String) {
        search(query, true)
    }

    override fun onSongClick(song: Song) {
        playerUiActions.submitNewListAndPlay(allSongs, allSongs.indexOf(song))
    }

    override fun onPlaylistsClick() {
        // TODO: Not yet implemented
    }

    override fun onFavouritesClick() {
        // TODO: Not yet implemented
    }

    override fun onHistoryClick() {
        // TODO: Not yet implemented
    }

    override fun refreshSongs() {
        loadSongsRequest.tryEmit(Unit)
    }

    override fun onGoToSettingsClick() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", applicationContext.packageName, null)
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(this)
        }
    }
}