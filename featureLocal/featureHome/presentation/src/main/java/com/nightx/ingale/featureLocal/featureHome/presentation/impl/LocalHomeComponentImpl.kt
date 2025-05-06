package com.nightx.ingale.featureLocal.featureHome.presentation.impl

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.arkivanov.essenty.lifecycle.doOnResume
import com.nightx.ingale.core.audioPlayer.api.PlayerUiActions
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.domainModel.LoadState
import com.nightx.ingale.core.domainModel.Song
import com.nightx.ingale.core.presentation.viewModel.UiEffectSender
import com.nightx.ingale.core.presentation.viewModel.updateIf
import com.nightx.ingale.core.utils.mapList
import com.nightx.ingale.core.utils.stateInWhileSubscribed
import com.nightx.ingale.core.viewState.LoadingViewState
import com.nightx.ingale.core.viewState.emptyStableList
import com.nightx.ingale.core.viewState.isLoading
import com.nightx.ingale.core.viewState.toLoadingViewState
import com.nightx.ingale.core.viewState.toStableList
import com.nightx.ingale.featureLocal.core.ui.viewState.SongViewState
import com.nightx.ingale.featureLocal.core.ui.viewState.SongsSetViewState
import com.nightx.ingale.featureLocal.core.ui.viewState.mapper.SongViewStateMapper
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.GetCachedState
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.GetSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.domain.usecases.OrganizeSongsUseCase
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalHomeComponent
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalMainCallbacks
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalMainScreenViewState
import com.nightx.ingale.featureLocal.featureHome.presentation.api.LocalMainUiEffect
import com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers.SongsSetToNavArgMapper
import com.nightx.ingale.featureLocal.featureHome.presentation.impl.mappers.SongsSetViewStateMapper
import com.nightx.ingale.resources.strings.StringProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class LocalHomeComponentImpl(
    appComponentContext: AppComponentContext,
    private val getSongsUseCase: GetSongsUseCase,
    private val getCachedState: GetCachedState,
    private val songViewStateMapper: SongViewStateMapper,
    private val songsSetViewStateMapper: SongsSetViewStateMapper,
    private val organizeSongsUseCase: OrganizeSongsUseCase,
    private val requiredPermissionsInspector: RequiredPermissionsInspector,
    private val songsSetToNavArgMapper: SongsSetToNavArgMapper,
    private val applicationContext: Context,
    private val playerUiActions: PlayerUiActions,
    private val stringProvider: StringProvider,
) : LocalHomeComponent,
    LocalMainCallbacks,
    StringProvider by stringProvider,
    AppComponentContext by appComponentContext,
    UiEffectSender<LocalMainUiEffect>() {

    companion object {
        const val NO_SONGS_FOUND_ERROR = "no_audio_files_found"

        private const val LongWait = 3000L
    }

    private var allSongsDomain: List<Song> = emptyList()

    private var allSongs = emptyList<SongViewState>()
    private var allAlbums = emptyList<SongsSetViewState>()
    private var allArtists = emptyList<SongsSetViewState>()

    private var wasAudioPermissionGrantedReceived = false

    private val loadSongsRequest =
        MutableSharedFlow<Unit>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    private val loadingViewState = MutableStateFlow<LoadingViewState>(LoadingViewState.Loading)
    private val songs = MutableStateFlow(emptyStableList<SongViewState>())
    private val albums = MutableStateFlow(emptyStableList<SongsSetViewState>())
    private val artists = MutableStateFlow(emptyStableList<SongsSetViewState>())
    private val query = MutableStateFlow("")

    override val uiCallbacks = this
    override val uiState = combine(
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
    }.stateInWhileSubscribed(componentScope, LocalMainScreenViewState())

    init {
        observeSongs()
        observeQuery()
        doOnResume {
            onResume()
        }
    }

    private fun observeLongLoading() {
        componentScope.launch {
            val isCached = getCachedState()
            delay(LongWait)
            if (isCached.not() && loadingViewState.value.isLoading()) {
//                snackbarMessageSender.sendSnackbarMessage(string(R.string.longer_for_first_time))
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSongs() {
        componentScope.launch {
            loadSongsRequest
                .filter { isAudioPermissionGranted }
                .flatMapLatest {
                    loadingViewState.update { LoadingViewState.Loading }
                    observeLongLoading()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeQuery() {
        query
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
                sendEffect { LocalMainUiEffect.ScrollToTop }
            }.launchIn(componentScope)
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
                            LocalMainScreenViewState.PERMISSION_NOT_GRANTED_ERROR
                        )
                    }
                }
            }
        )
    }

    override fun onAlbumClick(songsSet: SongsSetViewState) {
        appRouter.navigate(
            songsSetToNavArgMapper.map(songsSet)
        )
    }

    override fun onArtistClick(songsSet: SongsSetViewState) {
        appRouter.navigate(
            songsSetToNavArgMapper.map(songsSet)
        )
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
//        snackbarMessageSender.sendSnackbarMessage("Playlists not implemented yet")
    }

    override fun onFavouritesClick() {
//        snackbarMessageSender.sendSnackbarMessage("Favourites not implemented yet")
    }

    override fun onHistoryClick() {
//        snackbarMessageSender.sendSnackbarMessage("History not implemented yet")
    }

    override fun refreshSongs() {
        loadSongsRequest.tryEmit(Unit)
    }

    // TODO: create a helper
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