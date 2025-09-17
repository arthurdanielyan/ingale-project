package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nightx.ingale.core.ui.bottomSheet.ModalBottomSheet
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.featureLocal.core.presentation.common.ui.BottomSheetOptionButton
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperation
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.songOperationsParentComponent.songOperationsBottomSheet.api.SongOperationsBottomSheetComponent
import com.nightx.ingale.featureLocal.core.presentation.R.string as LocalStrings
import com.nightx.ingale.resources.songOperations.R.drawable as SongOperationDrawables
import com.nightx.ingale.resources.strings.R.string as CoreStrings

@Composable
internal fun SongOperationsBottomSheet(
    component: SongOperationsBottomSheetComponent
) {
    SongOperationsBottomSheet(
        onOperation = component.uiCallbacks::onSongOperationClick,
        onDismissRequest = component.onDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongOperationsBottomSheet(
    onOperation: (SongOperation) -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier
            .padding(MaterialTheme.dimensions.large)
            .clip(RoundedCornerShape(MaterialTheme.dimensions.large)),
        onDismissRequest = onDismissRequest,
    ) {
        Column {
            BottomSheetOptionButton(
                text = stringResource(LocalStrings.modify_song_info),
                iconPainter = painterResource(SongOperationDrawables.operation_playlist),
                onClick = { onOperation(SongOperation.MODIFY_SONG_INFO) },
            )
            BottomSheetOptionButton(
                text = stringResource(LocalStrings.save_to_playlist),
                iconPainter = painterResource(SongOperationDrawables.operation_playlist),
                onClick = { onOperation(SongOperation.SAVE_TO_PLAYLIST) },
            )
            BottomSheetOptionButton(
                text = stringResource(LocalStrings.save_to_favourites),
                iconPainter = painterResource(SongOperationDrawables.operation_playlist),
                onClick = { onOperation(SongOperation.SAVE_TO_FAVOURITES) },
            )
            BottomSheetOptionButton(
                text = stringResource(LocalStrings.play_next),
                iconPainter = painterResource(SongOperationDrawables.operation_playlist),
                onClick = { onOperation(SongOperation.PLAY_NEXT) },
            )
            BottomSheetOptionButton(
                text = stringResource(LocalStrings.add_to_queue),
                iconPainter = painterResource(SongOperationDrawables.operation_playlist),
                onClick = { onOperation(SongOperation.ADD_TO_QUEUE) },
            )
            BottomSheetOptionButton(
                text = stringResource(CoreStrings.delete),
                iconPainter = painterResource(SongOperationDrawables.operation_playlist),
                onClick = { onOperation(SongOperation.DELETE) },
            )
        }
    }
}
