package com.nightx.ingale.feature_local.required_permissions_requester_dialog.presentation.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.nightx.ingale.R
import com.nightx.ingale.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionNotGrantedDialog(
    modifier: Modifier = Modifier,
    isPermanentlyDeclined: Boolean,
    descriptionProvider: PermissionDescriptionProvider,
    onOkClick: () -> Unit,
    onGoToAppSettings: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
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
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Text(
                text = stringResource(R.string.permission_required),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    top = MaterialTheme.spacing.large,
                    start = MaterialTheme.spacing.large,
                    end = MaterialTheme.spacing.large,
                )
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(24.dp),
                    text = descriptionProvider.getDescription(),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Divider()
                Text(
                    text = if (isPermanentlyDeclined) {
                        stringResource(R.string.go_to_settings)
                    } else {
                        stringResource(R.string.ok)
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = if (isPermanentlyDeclined) onGoToAppSettings
                            else onOkClick
                        )
                        .padding(MaterialTheme.spacing.large),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

interface PermissionDescriptionProvider {
    @Composable
    fun getDescription(): String
}

class AudioPermissionDescriptionProvider : PermissionDescriptionProvider {

    @Composable
    override fun getDescription(): String =
        stringResource(id = R.string.audio_permission_not_granted_description)
}

class StoragePermissionDescriptionProvider : PermissionDescriptionProvider {

    @Composable
    override fun getDescription(): String =
        stringResource(id = R.string.storage_permission_not_granted_permission)
}

class NotificationPermissionDescriptionProvider : PermissionDescriptionProvider {

    @Composable
    override fun getDescription(): String =
        stringResource(id = R.string.notification_permission_not_granted_permission)
}