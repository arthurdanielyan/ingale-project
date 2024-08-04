package com.nightx.ingale.featureLocal.featureRequirePermissions.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.resources.strings.R.string as Strings

@Composable
fun PermissionNotGrantedDialog(
    isPermanentlyDeclined: Boolean,
    descriptionProvider: PermissionDescriptionProvider,
    onOkClick: () -> Unit,
    onGoToAppSettings: () -> Unit
) {
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
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Text(
                text = stringResource(Strings.permission_required),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    top = MaterialTheme.dimensions.large,
                    start = MaterialTheme.dimensions.large,
                    end = MaterialTheme.dimensions.large,
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
                HorizontalDivider()
                Text(
                    text = if (isPermanentlyDeclined) {
                        stringResource(Strings.go_to_settings)
                    } else {
                        stringResource(Strings.ok)
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = if (isPermanentlyDeclined) onGoToAppSettings
                            else onOkClick
                        )
                        .padding(MaterialTheme.dimensions.large),
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
        stringResource(id = Strings.audio_permission_not_granted_description)
}

class StoragePermissionDescriptionProvider : PermissionDescriptionProvider {

    @Composable
    override fun getDescription(): String =
        stringResource(id = Strings.storage_permission_not_granted_permission)
}

class NotificationPermissionDescriptionProvider : PermissionDescriptionProvider {

    @Composable
    override fun getDescription(): String =
        stringResource(id = Strings.notification_permission_not_granted_permission)
}