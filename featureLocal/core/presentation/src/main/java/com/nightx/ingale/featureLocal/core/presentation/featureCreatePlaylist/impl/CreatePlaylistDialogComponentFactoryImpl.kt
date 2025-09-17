package com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.impl

import com.nightx.ingale.bottomBarApi.SnackbarMessageSender
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.domain.playlists.usecase.CreateNewPlaylistUseCase
import com.nightx.ingale.core.domain.playlists.usecase.GetPlaylistsUseCase
import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api.CreatePlaylistDialogComponent
import com.nightx.ingale.resources.strings.StringProvider
import kotlinx.coroutines.CoroutineScope

internal class CreatePlaylistDialogComponentFactoryImpl(
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val createNewPlaylistUseCase: CreateNewPlaylistUseCase,
    private val applicationScope: CoroutineScope,
    private val snackbarMessageSender: SnackbarMessageSender,
    private val stringProvider: StringProvider,
) : CreatePlaylistDialogComponent.Factory {

    override operator fun invoke(
        appComponentContext: AppComponentContext,
        params: CreatePlaylistDialogComponent.Params,
        onDismiss: () -> Unit
    ): CreatePlaylistDialogComponent {
        return CreatePlaylistDialogComponentImpl(
            appComponentContext = appComponentContext,
            onDismiss = onDismiss,
            params = params,
            getPlaylistsUseCase = getPlaylistsUseCase,
            createNewPlaylistUseCase = createNewPlaylistUseCase,
            applicationScope = applicationScope,
            snackbarMessageSender = snackbarMessageSender,
            stringProvider = stringProvider,
        )
    }
}