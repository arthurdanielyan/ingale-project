package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.impl

import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.domain.playlists.usecase.GetPlaylistsUseCase
import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api.CreatePlaylistDialogComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api.CurrentPlaylistsBottomSheetComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.api.SongOperationsParentComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperationsBottomSheetComponent

internal class SongOperationsParentComponentFactoryImpl(
    private val songOperationsBottomSheetComponentFactory: SongOperationsBottomSheetComponent.Factory,
    private val createPlaylistDialogComponentFactory: CreatePlaylistDialogComponent.Factory,
    private val currentPlaylistsBottomSheetComponentFactory: CurrentPlaylistsBottomSheetComponent.Factory,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
) : SongOperationsParentComponent.Factory {

    override fun invoke(
        appComponentContext: AppComponentContext
    ): SongOperationsParentComponent {
        return SongOperationsParentComponentImpl(
            appComponentContext = appComponentContext,
            songOperationsBottomSheetComponentFactory = songOperationsBottomSheetComponentFactory,
            createPlaylistDialogComponentFactory = createPlaylistDialogComponentFactory,
            currentPlaylistsBottomSheetComponentFactory = currentPlaylistsBottomSheetComponentFactory,
            getPlaylistsUseCase = getPlaylistsUseCase,
        )
    }
}