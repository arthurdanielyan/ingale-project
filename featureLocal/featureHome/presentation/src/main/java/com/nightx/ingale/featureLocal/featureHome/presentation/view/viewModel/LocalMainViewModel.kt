package com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.featureLocal.core.viewState.song.SongViewState
import com.nightx.featureLocal.core.viewState.song.mapper.SongViewStateMapper
import com.nightx.ingale.bottomBar.api.SnackbarMessageSender
import com.nightx.ingale.core.audioPlayer.api.PlayerUiActions
import com.nightx.ingale.core.domainModel.LoadState
import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.core.presentation.viewModel.BaseViewModel
import com.nightx.ingale.core.presentation.viewModel.updateIf
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.core.viewState.LoadingViewState
import com.nightx.ingale.core.viewState.emptyStableList
import com.nightx.ingale.core.viewState.toLoadingViewState
import com.nightx.ingale.core.viewState.toStableList
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.GetSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.OrganizeSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.presentation.view.RequiredPermissionsInspector
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers.SongsSetToNavArgMapper
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers.SongsSetViewStateMapper
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.viewState.LocalMainScreenViewState
import com.nightx.ingale.featureLocal.navigation.api.LocalNavigator
import com.nightx.ingale.featureLocal.navigation.api.destinations.SongsSetScreenDestination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class LocalMainViewModel(
    private val navigator: LocalNavigator,
    private val getSongsUseCase: GetSongsUseCase,
    private val songViewStateMapper: SongViewStateMapper,
    private val songsSetViewStateMapper: SongsSetViewStateMapper,
    private val organizeSongsUseCase: OrganizeSongsUseCase,
    private val requiredPermissionsInspector: RequiredPermissionsInspector,
    private val songsSetToNavArgMapper: SongsSetToNavArgMapper,
    private val applicationContext: Context,
    private val playerUiActions: PlayerUiActions,
    private val snackbarMessageSender: SnackbarMessageSender,
) : BaseViewModel<LocalMainScreenViewState, Effect>(), LocalMainCallbacks {

    companion object {
        const val NO_SONGS_FOUND_ERROR = "no_audio_files_found"
        const val PERMISSION_NOT_GRANTED_ERROR = "no_audio_permission_granted"

        private const val QueryDebounce = 200L
    }

    private lateinit var allSongsDomain: List<Song>

    private var allSongs = emptyList<SongViewState>()
    private var allAlbums = emptyList<SongsSetViewState>()
    private var allArtists = emptyList<SongsSetViewState>()

    private var wasAudioPermissionGrantedReceived = false

    private val loadSongsRequest =
        MutableSharedFlow<Unit>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    override fun defineInitialState() = LocalMainScreenViewState()

    private val loadingViewState = MutableStateFlow<LoadingViewState>(LoadingViewState.Loading)
    private val songs = MutableStateFlow(emptyStableList<SongViewState>())
    private val albums = MutableStateFlow(emptyStableList<SongsSetViewState>())
    private val artists = MutableStateFlow(emptyStableList<SongsSetViewState>())
    private val query = MutableStateFlow("")

    val permissionsDialogComponentHolder
        get() = requiredPermissionsInspector.permissionsDialogComponentHolder

    override val state = combine(
        loadingViewState,
        songs,
        albums,
        artists,
        query
    ) { loadingViewState, songs, albums, artists, query ->

        LocalMainScreenViewState(
            searchTextField = query,
            songs = songs,
            albums = albums,
            artists = artists,
            loadingState = loadingViewState,
        )
    }.viewModelState()

    init {
        observeSongs()
        observeQuery()
        refreshSongs() // triggers observation
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSongs() {
        viewModelScope.launch {
            loadSongsRequest
                .filter { isAudioPermissionGranted }
                .flatMapLatest {
                    loadingViewState.update { LoadingViewState.Loading }
                    getSongsUseCase()
                }.filter { songsLoadState ->
                    // if Success update later, only when the state is fully constructed
                    loadingViewState.updateIf(songsLoadState !is LoadState.Success) {
                        songsLoadState.toLoadingViewState()
                    }
                    songsLoadState is LoadState.Success
                }.mapLatest { songs ->
                    val songsSeparation = organizeSongsUseCase(songs.dataOrDefault(emptyList()))
                    allSongsDomain = songsSeparation.songs

                    if (songsSeparation.songs.isNotEmpty()) {
                        allSongs = songViewStateMapper.mapList(songsSeparation.songs)
                        allAlbums = songsSetViewStateMapper.mapList(songsSeparation.albums)
                        allArtists = songsSetViewStateMapper.mapList(songsSeparation.artists)
                        Triple(
                            allSongs.toStableList(),
                            allAlbums.toStableList(),
                            allArtists.toStableList(),
                        )
                    } else {
                        null
                    }
                }.collectLatest { songSeparation ->
                    songSeparation?.let { (songsData, albumsData, artistsData) ->
                        songs.update { songsData }
                        albums.update { albumsData }
                        artists.update { artistsData }
                        loadingViewState.update { LoadingViewState.Success }
                    } ?: loadingViewState.update {
                        LoadingViewState.Error(NO_SONGS_FOUND_ERROR)
                    }
                }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeQuery() {
        query
            .debounce(QueryDebounce)
            .mapLatest { it.lowercase() }
            .onEach { query ->
                songs.update {
                    allSongs.filter { it.title.lowercase().contains(query) }.toStableList()
                }
                albums.update {
                    allAlbums.filter { it.title.lowercase().contains(query) }.toStableList()
                }
                artists.update {
                    allArtists.filter { it.title.lowercase().contains(query) }.toStableList()
                }
                sendEffect { Effect.ScrollToTop }
            }.launchIn(viewModelScope)
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
                    if (!wasAudioPermissionGrantedReceived) {
                        refreshSongs()
                        wasAudioPermissionGrantedReceived = true
                    }
                } else {
                    loadingViewState.update {
                        LoadingViewState.Error(
                            PERMISSION_NOT_GRANTED_ERROR
                        )
                    }
                }
            }
        )
    }

    override fun onAlbumClick(songsSet: SongsSetViewState) {
        navigator.navigate(SongsSetScreenDestination, songsSetToNavArgMapper.map(songsSet))
    }

    override fun onArtistClick(songsSet: SongsSetViewState) {
        navigator.navigate(SongsSetScreenDestination, songsSetToNavArgMapper.map(songsSet))
    }

    override fun onSearchType(query: String) {
        this.query.update { query }
    }

    override fun onSongClick(song: SongViewState) {
        playerUiActions.submitNewListAndPlay(
            songQueue = allSongsDomain,
            indexToPlay = allSongsDomain
                .indexOfFirst { it.id == song.id }.coerceAtLeast(0)
        )
    }

    override fun onPlaylistsClick() {
        snackbarMessageSender.sendSnackbarMessage("Playlists not implemented yet")
    }

    override fun onFavouritesClick() {
        snackbarMessageSender.sendSnackbarMessage("Favourites not implemented yet")
    }

    override fun onHistoryClick() {
        snackbarMessageSender.sendSnackbarMessage("History not implemented yet")
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