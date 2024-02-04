package com.night.ingale.feature_local.main.presentation.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.night.ingale.core.audio_player.AudioPlayer
import com.night.ingale.core.domain.model.Song
import com.night.ingale.core.presentation.navigation.dialog_navigation.DialogDestination.LocalDialogDestination
import com.night.ingale.core.presentation.navigation.dialog_navigation.DialogNavigator
import com.night.ingale.core.presentation.view.LoadingViewState
import com.night.ingale.feature_local.local_navigation.screen_navigation.LocalScreenDestination
import com.night.ingale.feature_local.local_navigation.screen_navigation.LocalNavigator
import com.night.ingale.feature_local.main.domain.model.Album
import com.night.ingale.feature_local.main.domain.model.Artist
import com.night.ingale.feature_local.main.domain.model.SongsSeparation
import com.night.ingale.feature_local.main.domain.usecases.FilterUseCase
import com.night.ingale.feature_local.main.domain.usecases.GetSongsUseCase
import com.night.ingale.feature_local.main.domain.usecases.OrganizeSongsUseCase
import com.night.ingale.feature_local.main.presentation.view.LocalMainContract.Effect
import com.night.ingale.feature_local.main.presentation.view.LocalMainContract.Event
import com.night.ingale.feature_local.main.presentation.view.LocalMainContract.State
import com.night.ingale.mvi.BaseViewModel
import com.night.ingale.mvi.wrappers.StableList
import com.night.ingale.mvi.wrappers.emptyStableList
import com.night.ingale.mvi.wrappers.toStableList
import kotlinx.coroutines.launch


class LocalMainViewModel(
    private val navigator: LocalNavigator,
    dialogNavigator: DialogNavigator,
    private val getSongsUseCase: GetSongsUseCase,
    private val organizeSongsUseCase: OrganizeSongsUseCase,
    private val filterUseCase: FilterUseCase,
    private val applicationContext: Context,
) : BaseViewModel<State, Event, Effect>() {

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
            LocalDialogDestination.RequiredPermissionRequester,
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


    override fun handleEvent(event: Event) {
        when (event) {
            Event.Refresh -> loadSongs()
            is Event.AlbumClicked ->
                navigator.navigate(LocalScreenDestination.SongsSetScreen, event.songsSet)

            is Event.ArtistClicked ->
                navigator.navigate(LocalScreenDestination.SongsSetScreen, event.songsSet)

            is Event.Search -> search(event.query)
            is Event.PlaySong ->
                AudioPlayer.play(allSongs, allSongs.indexOf(event.song))
        }
    }

    private fun loadSongs() {
        viewModelScope.launch {
            updateState {
                copy(songLoadingState = LoadingViewState.Loading)
            }
            val songs = getSongsUseCase()
            if (songs.isNotEmpty()) {
                val separatedSongs = organizeSongsUseCase(songs)
                allSongs = songs
                allAlbums = separatedSongs.albums
                allArtists = separatedSongs.artists
                updateState {
                    copy(
                        songLoadingState = LoadingViewState.Success,
                        allSongs = StableList(songs),
                        albums = separatedSongs.albums.toStableList(),
                        artists = separatedSongs.artists.toStableList(),
                    )
                }
                search(currentState.searchTextField)
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
}