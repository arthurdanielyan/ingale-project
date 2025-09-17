package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.impl

import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.dismiss
import com.nightx.ingale.core.decompose.AppComponentContext
import com.nightx.ingale.core.decompose.appChildSlot
import com.nightx.ingale.core.domain.playlists.usecase.GetPlaylistsUseCase
import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api.CreatePlaylistDialogComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api.CurrentPlaylistsBottomSheetComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.api.SongOperationsChildConfig
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.api.SongOperationsParentComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperation
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperationsBottomSheetComponent
import kotlinx.coroutines.launch

internal class SongOperationsParentComponentImpl(
    appComponentContext: AppComponentContext,
    private val songOperationsBottomSheetComponentFactory: SongOperationsBottomSheetComponent.Factory,
    private val createPlaylistDialogComponentFactory: CreatePlaylistDialogComponent.Factory,
    private val currentPlaylistsBottomSheetComponentFactory: CurrentPlaylistsBottomSheetComponent.Factory,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
) : SongOperationsParentComponent, AppComponentContext by appComponentContext {

    private val childSlotNavigation = SlotNavigation<SongOperationsChildConfig>()
    override val childSlot = appChildSlot(
        source = childSlotNavigation,
        serializer = SongOperationsChildConfig.serializer(),
        initialConfiguration = null,
        handleBackButton = true,
        childFactory = { config, childContext ->
            when (config) {
                is SongOperationsChildConfig.SongOperationsBottomSheetConfig -> {
                    songOperationsBottomSheetComponentFactory(
                        appComponentContext = childContext,
                        params = SongOperationsBottomSheetComponent.Params(
                            songId = config.songId
                        ),
                        onSongOperationClickCallback = ::onSongOperationClick,
                        onDismiss = {
                            childSlotNavigation.dismiss()
                        }
                    )
                }

                is SongOperationsChildConfig.CreatePlaylistConfig -> {
                    createPlaylistDialogComponentFactory(
                        appComponentContext = childContext,
                        params = CreatePlaylistDialogComponent.Params(
                            initialSongId = config.initialSongId
                        ),
                        onDismiss = {
                            childSlotNavigation.dismiss()
                        }
                    )
                }

                is SongOperationsChildConfig.CurrentPlaylistsBottomSheet -> {
                    currentPlaylistsBottomSheetComponentFactory(
                        appComponentContext = childContext,
                        params = CurrentPlaylistsBottomSheetComponent.Params(
                            songId = config.songId
                        ),
                        onCreateNewPlaylistClick = {
                            childSlotNavigation.activate(
                                SongOperationsChildConfig.CreatePlaylistConfig(
                                    initialSongId = config.songId
                                )
                            )
                        },
                        onDismiss = {
                            childSlotNavigation.dismiss()
                        }
                    )
                }

                is SongOperationsChildConfig.DeleteSongConfirmationConfig -> {

                }
            }
        }
    )

    private fun onSongOperationClick(operation: SongOperation, songId: Long) {
        when (operation) {
            SongOperation.SAVE_TO_PLAYLIST -> {
                onSaveToPlaylistClick(songId)
            }

            SongOperation.SAVE_TO_FAVOURITES -> Unit // TODO: Not yet implemented
            SongOperation.PLAY_NEXT -> Unit // TODO: Not yet implemented
            SongOperation.ADD_TO_QUEUE -> Unit // TODO: Not yet implemented
            SongOperation.DELETE -> Unit // TODO: Not yet implemented
            SongOperation.MODIFY_SONG_INFO -> Unit // TODO: Not yet implemented
        }
    }

    private fun onSaveToPlaylistClick(songId: Long) {
        componentScope.launch {
            if (getPlaylistsUseCase().isEmpty()) {
                childSlotNavigation.activate(
                    SongOperationsChildConfig.CreatePlaylistConfig(
                        initialSongId = songId
                    )
                )
            } else {
                childSlotNavigation.activate(
                    SongOperationsChildConfig.CurrentPlaylistsBottomSheet(
                        songId = songId
                    )
                )
            }
        }
    }

    override fun openSongOperationsBottomSheet(songId: Long) {
        childSlotNavigation.activate(
            SongOperationsChildConfig.SongOperationsBottomSheetConfig(
                songId = songId
            )
        )
    }
}