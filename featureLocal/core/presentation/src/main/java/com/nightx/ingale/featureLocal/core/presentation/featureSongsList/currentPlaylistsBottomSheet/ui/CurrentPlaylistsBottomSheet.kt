package com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.nightx.ingale.core.ui.bottomSheet.ModalBottomSheet
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.featureLocal.core.presentation.R
import com.nightx.ingale.featureLocal.core.presentation.common.ui.BottomSheetOptionButton
import com.nightx.ingale.featureLocal.core.presentation.featureSongsList.currentPlaylistsBottomSheet.api.CurrentPlaylistsBottomSheetComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrentPlaylistsBottomSheet(
    component: CurrentPlaylistsBottomSheetComponent,
) {
    val state by component.uiState.collectAsState()
    val callbacks = component.uiCallbacks

    ModalBottomSheet(
        modifier = Modifier
            .padding(MaterialTheme.dimensions.large)
            .clip(RoundedCornerShape(MaterialTheme.dimensions.large)),
        onDismissRequest = callbacks::onDismiss,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                BottomSheetOptionButton(
                    text = stringResource(R.string.create_new_playlist),
                    onClick = callbacks::onCreateNewPlaylistClick
                )
            }
            items(state.playlists) { playlist ->
                BottomSheetOptionButton(
                    text = playlist.name,
                    onClick = {
                        callbacks.onSaveToPlaylist(playlist)
                    }
                )
            }
        }
    }
}