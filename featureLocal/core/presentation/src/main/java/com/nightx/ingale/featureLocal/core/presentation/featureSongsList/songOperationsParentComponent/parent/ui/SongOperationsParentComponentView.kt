package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.ui

import androidx.compose.runtime.Composable
import com.nightx.ingale.core.ui.ChildSlot
import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api.CreatePlaylistDialogComponent
import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.ui.CreatePlaylistDialog
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api.CurrentPlaylistsBottomSheetComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.ui.CurrentPlaylistsBottomSheet
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.parent.api.SongOperationsParentComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperationsBottomSheetComponent
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.ui.SongOperationsBottomSheet

@Composable
internal fun SongOperationsParentComponentView(
    component: SongOperationsParentComponent
) {
    ChildSlot(
        slot = component.childSlot
    ) {
        when (val childComponent = it.instance) {
            is SongOperationsBottomSheetComponent -> SongOperationsBottomSheet(childComponent)
            is CreatePlaylistDialogComponent -> CreatePlaylistDialog(childComponent)
            is CurrentPlaylistsBottomSheetComponent -> CurrentPlaylistsBottomSheet(childComponent)
        }
    }
}