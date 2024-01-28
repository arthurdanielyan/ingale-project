package com.example.ingale.feature_local.main.presentation.view

import androidx.lifecycle.viewModelScope
import com.example.ingale.core.audio_player.AudioPlayer
import com.example.ingale.core.domain.model.Song
import com.example.ingale.core.presentation.navigation.dialog_navigation.DialogDestination.LocalDialogDestination
import com.example.ingale.core.presentation.navigation.dialog_navigation.DialogNavigator
import com.example.ingale.feature_local.local_navigation.screen_navigation.LocalScreenDestination
import com.example.ingale.feature_local.local_navigation.screen_navigation.LocalNavigator
import com.example.ingale.feature_local.main.domain.model.Album
import com.example.ingale.feature_local.main.domain.model.Artist
import com.example.ingale.feature_local.main.domain.model.SongsSeparation
import com.example.ingale.feature_local.main.domain.usecases.FilterUseCase
import com.example.ingale.feature_local.main.domain.usecases.GetSongsUseCase
import com.example.ingale.feature_local.main.domain.usecases.OrganizeSongsUseCase
import com.example.ingale.feature_local.main.presentation.view.LocalMainContract.Effect
import com.example.ingale.feature_local.main.presentation.view.LocalMainContract.Event
import com.example.ingale.feature_local.main.presentation.view.LocalMainContract.State
import com.example.ingale.mvi.BaseViewModel
import com.example.ingale.mvi.wrappers.StableList
import com.example.ingale.mvi.wrappers.emptyStableList
import com.example.ingale.mvi.wrappers.toStableList
import kotlinx.coroutines.launch


class LocalMainViewModel(
    private val navigator: LocalNavigator,
    dialogNavigator: DialogNavigator,
    private val getSongsUseCase: GetSongsUseCase,
    private val organizeSongsUseCase: OrganizeSongsUseCase,
    private val filterUseCase: FilterUseCase,
) : BaseViewModel<State, Event, Effect>() {

    companion object {
        const val SECTION_SONGS = "Songs"
        const val SECTION_ALBUMS = "Albums"
        const val SECTION_ARTISTS = "Artists"
    }

    private lateinit var allSongs: List<Song>
    private lateinit var allAlbums: List<Album>
    private lateinit var allArtists: List<Artist>

    init {
        dialogNavigator.activate<Boolean>(
            LocalDialogDestination.RequiredPermissionRequester,
            onResult = { shouldLoadSongs ->
                if(shouldLoadSongs) {
                    loadSongs()
                } else {
                    updateState {
                        copy(areSongsLoading = false)
                    }
                }
            }
        )
    }

    override fun defineInitialState(): State =
        State(
            searchTextField = "",
            allSongs = emptyStableList(),
            albums = emptyStableList(),
            artists = emptyStableList(),
            areSongsLoading = true,
            visiblePermissionDialogQueue = emptyStableList()
        )


    override fun handleEvent(event: Event) {
        when (event) {
            is Event.PermissionResult -> {
                onPermissionResult(event.permission, event.isGranted)
            }

            Event.DismissPermissionDialog -> {
                updateState {
                    copy(
                        visiblePermissionDialogQueue = StableList(
                            visiblePermissionDialogQueue.toMutableList().apply {
                                removeFirst()
                            }
                        )
                    )
                }
            }

            is Event.AlbumClicked -> {
                navigator.navigate(LocalScreenDestination.SongsSetScreen, event.songsSet)
            }

            is Event.ArtistClicked -> {
                navigator.navigate(LocalScreenDestination.SongsSetScreen, event.songsSet)
            }

            is Event.Search -> search(event.query)
            is Event.PlaySong -> {
                AudioPlayer.play(allSongs, allSongs.indexOf(event.song))
            }
        }
    }

    private fun loadSongs() {
        viewModelScope.launch {
            updateState {
                copy(areSongsLoading = true)
            }
            val songs = getSongsUseCase()
            val separatedSongs = organizeSongsUseCase(songs)
            allSongs = songs
            allAlbums = separatedSongs.albums
            allArtists = separatedSongs.artists
            updateState {
                copy(
                    areSongsLoading = false,
                    allSongs = StableList(songs),
                    albums = separatedSongs.albums.toStableList(),
                    artists = separatedSongs.artists.toStableList(),
                )
            }
            search(currentState.searchTextField)
        }
    }

    private fun search(query: String) {
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
            sendEffect {
                Effect.ScrollToTop
            }
        }
    }

    private fun onPermissionResult(permission: String, isGranted: Boolean) {
        updateState {
            copy(
                visiblePermissionDialogQueue = visiblePermissionDialogQueue.toMutableList().apply {
                    if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
                        add(0, permission)
                    }
                }.toStableList()
            )
        }
        if (currentState.visiblePermissionDialogQueue.isEmpty()) {
            loadSongs()
        }
    }
}