package com.nightx.ingale.feature_local.main.presentation.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.nightx.ingale.core.audio_player.AudioPlayer
import com.nightx.ingale.core.domain.LoadState
import com.nightx.ingale.core.domain.model.Song
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.DialogDestination
import com.nightx.ingale.core.presentation.navigation.dialog_navigation.DialogNavigator
import com.nightx.ingale.core.presentation.view.LoadingViewState
import com.nightx.ingale.feature_local.local_core.domain.model.SongsSet
import com.nightx.ingale.feature_local.local_navigation.screen_navigation.LocalNavigator
import com.nightx.ingale.feature_local.local_navigation.screen_navigation.LocalScreenDestination
import com.nightx.ingale.feature_local.main.domain.model.Album
import com.nightx.ingale.feature_local.main.domain.model.Artist
import com.nightx.ingale.feature_local.main.domain.model.SongsSeparation
import com.nightx.ingale.feature_local.main.domain.usecases.FilterUseCase
import com.nightx.ingale.feature_local.main.domain.usecases.GetSongsUseCase
import com.nightx.ingale.feature_local.main.domain.usecases.OrganizeSongsUseCase
import com.nightx.ingale.feature_local.main.presentation.view.LocalMainContract.Effect
import com.nightx.ingale.feature_local.main.presentation.view.LocalMainContract.State
import com.nightx.ingale.mvi.BaseViewModel
import com.nightx.ingale.mvi.wrappers.emptyStableList
import com.nightx.ingale.mvi.wrappers.toStableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class LocalMainViewModel(
    private val navigator: LocalNavigator,
    dialogNavigator: DialogNavigator,
    private val getSongsUseCase: GetSongsUseCase,
    private val organizeSongsUseCase: OrganizeSongsUseCase,
    private val filterUseCase: FilterUseCase,
    private val applicationContext: Context,
) : BaseViewModel<State, Effect>(), LocalMainCallbacks {

    companion object {
        const val SECTION_SONGS = "Songs"
        const val SECTION_ALBUMS = "Albums"
        const val SECTION_ARTISTS = "Artists"

        const val NO_SONGS_FOUND_ERROR = "no_audio_filed_found"
        const val PERMISSION_NOT_GRANTED_ERROR = "no_audio_permission_granted"
    }

    private lateinit var allSongs: List<Song>
    private lateinit var allAlbums: List<Album>
    private lateinit var allArtists: List<Artist>

    init {
        dialogNavigator.activate<Boolean>(
            DialogDestination.RequiredPermissionRequester,
            onResult = {
                loadSongs()
            }
        )
    }

    override fun defineInitialState(): State =
        State(
            searchTextField = "",
            allSongs = emptyStableList(),
            albums = emptyStableList(),
            artists = emptyStableList(),
            songLoadingState = LoadingViewState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadSongs() {
        viewModelScope.launch {
            updateState {
                copy(songLoadingState = LoadingViewState.Loading)
            }
            getSongsUseCase()
                .filterNot {
                    it is LoadState.Loading
                }.collectLatest { songsLoadState ->
                    when(songsLoadState) {
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
            || currentState.searchTextField.isBlank()
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
            if(scrollToTop)
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

    override fun refresh() {
        loadSongs()
    }

    override fun onAlbumClick(songsSet: SongsSet) {
        navigator.navigate(LocalScreenDestination.SongsSetScreen, songsSet)
    }

    override fun onArtistClick(songsSet: SongsSet) {
        navigator.navigate(LocalScreenDestination.SongsSetScreen, songsSet)
    }

    override fun onSearchType(query: String) {
        search(query, true)
    }

    override fun onSongClick(song: Song) {
        AudioPlayer.play(allSongs, allSongs.indexOf(song))
    }
}