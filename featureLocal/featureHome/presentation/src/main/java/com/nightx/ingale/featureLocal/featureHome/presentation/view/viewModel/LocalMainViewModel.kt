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
import com.nightx.featureLocal.core.viewState.song.mapper.SongToViewStateMapper
import com.nightx.ingale.core.audioPlayer.api.PlayerUiActions
import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.core.presentation.viewModel.BaseViewModel
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.core.viewState.LoadingViewState
import com.nightx.ingale.core.viewState.emptyStableList
import com.nightx.ingale.core.viewState.toLoadingViewState
import com.nightx.ingale.core.viewState.toStableList
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.GetSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.OrganizeSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.presentation.view.RequiredPermissionsInspector
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers.AlbumMapper
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers.ArtistMapper
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.mappers.SongsSetToNavArgMapper
import com.nightx.ingale.featureLocal.featureHome.presentation.view.viewModel.viewState.LocalMainScreenViewState
import com.nightx.ingale.featureLocal.navigation.api.LocalNavigator
import com.nightx.ingale.feature_local.local_navigation.destinations.SongsSetScreenDestination
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
    private val songToViewStateMapper: SongToViewStateMapper,
    private val albumMapper: AlbumMapper,
    private val artistMapper: ArtistMapper,
    private val organizeSongsUseCase: OrganizeSongsUseCase,
    private val requiredPermissionsInspector: RequiredPermissionsInspector,
    private val songsSetToNavArgMapper: SongsSetToNavArgMapper,
    private val applicationContext: Context,
    private val playerUiActions: PlayerUiActions,
) : BaseViewModel<LocalMainScreenViewState, Effect>(), LocalMainCallbacks {

    companion object {
        const val NO_SONGS_FOUND_ERROR = "no_audio_files_found"
        const val PERMISSION_NOT_GRANTED_ERROR = "no_audio_permission_granted"

        private const val QueryDebounce = 200L
    }

    private lateinit var allSongs: List<Song>
    private lateinit var allAlbums: List<SongsSetViewState>
    private lateinit var allArtists: List<SongsSetViewState>

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
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSongs() {
        viewModelScope.launch {
            loadSongsRequest
                .filter { isAudioPermissionGranted }
                .flatMapLatest {
                    updateState {
                        copy(loadingState = LoadingViewState.Loading)
                    }
                    getSongsUseCase()
                }.collectLatest { songsLoadState ->
                    updateState {
                        copy(
                            loadingState = songsLoadState.toLoadingViewState(),
                        )
                    }
                    val songsSeparation = organizeSongsUseCase(songsLoadState.dataOrDefault(emptyList()))
                    if (songsSeparation.songs.isNotEmpty()) {
                        allSongs = songsSeparation.songs
                        allAlbums = albumMapper.mapList(songsSeparation.albums)
                        allArtists = artistMapper.mapList(songsSeparation.artists)
                        updateState {
                            copy(
                                loadingState = LoadingViewState.Success,
                                songs = songToViewStateMapper.mapList(songsSeparation.songs).toStableList(),
                                albums = albumMapper.mapList(songsSeparation.albums).toStableList(),
                                artists = artistMapper.mapList(songsSeparation.artists).toStableList(),
                            )
                        }
                    } else {
                        updateState {
                            copy(
                                loadingState = LoadingViewState.Error(
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

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeQuery() {
        query
            .debounce(QueryDebounce)
            .mapLatest { it.lowercase() }
            .onEach { query ->
                songs.update { songs ->
                    songs.filter { it.title.lowercase().contains(query) }.toStableList()
                }
                albums.update { albums ->
                    albums.filter { it.title.lowercase().contains(query) }.toStableList()
                }
                artists.update { artists ->
                    artists.filter { it.title.lowercase().contains(query) }.toStableList()
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
                    if(!wasAudioPermissionGrantedReceived) {
                        refreshSongs()
                        wasAudioPermissionGrantedReceived = true
                    }
                } else {
                    updateState {
                        copy(
                            loadingState = LoadingViewState.Error(
                                PERMISSION_NOT_GRANTED_ERROR
                            )
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
            songQueue = allSongs,
            indexToPlay = allSongs
                .indexOfFirst { it.id == song.id }.coerceAtLeast(0)
        )
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