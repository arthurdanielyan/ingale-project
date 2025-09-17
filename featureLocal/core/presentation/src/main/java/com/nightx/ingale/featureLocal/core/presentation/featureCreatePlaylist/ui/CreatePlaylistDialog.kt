package com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nightx.ingale.core.ui.IngaleTextField
import com.nightx.ingale.core.ui.buttons.PrimaryButton
import com.nightx.ingale.core.ui.buttons.SecondaryButton
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.featureLocal.core.presentation.R
import com.nightx.ingale.featureLocal.core.presentation.featureCreatePlaylist.api.CreatePlaylistDialogComponent
import com.nightx.ingale.resources.strings.R.string as CoreStrings

@Composable
fun CreatePlaylistDialog(
    component: CreatePlaylistDialogComponent,
) {
    val state by component.uiState.collectAsState()
    val callbacks = component.uiCallbacks

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small,
                )
                .padding(MaterialTheme.dimensions.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(MaterialTheme.dimensions.normal)
        ) {
            IngaleTextField(
                value = state.playlistName,
                onValueChange = callbacks::onPlaylistNameType,
                placeholder = stringResource(R.string.playlist_name),
            )
            if (state.requestedPlaylistExistsError) {
                Text(
                    text = stringResource(R.string.playlist_exists),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Row(
                horizontalArrangement = Arrangement
                    .spacedBy(MaterialTheme.dimensions.normal)
            ) {
                SecondaryButton(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(CoreStrings.cancel),
                    onClick = callbacks::onCancelClick,
                )
                PrimaryButton(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(R.string.create_playlist),
                    onClick = callbacks::onCreateClick,
                    isEnabled = state.isCreateButtonEnabled,
                )
            }
        }
    }
}