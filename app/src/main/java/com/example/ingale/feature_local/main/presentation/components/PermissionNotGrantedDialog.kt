package com.example.ingale.feature_local.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ingale.ui.theme.colorScheme.colors
import com.example.ingale.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionNotGrantedDialog(
    modifier: Modifier = Modifier,
    isPermanentlyDeclined: Boolean,
    descriptionProvider: PermissionDescriptionProvider,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettings: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.background
                ),
        ) {
            Text(
                text = "Permission required",
                fontSize = 24.sp,
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
                    text = descriptionProvider.description
                )
                HorizontalDivider()
                Text(
                    text = if (isPermanentlyDeclined) "Go to settings" else "OK",
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
    val description: String
}

class AudioPermissionDescriptionProvider : PermissionDescriptionProvider {
    override val description =
        "Please grant audio permission access so that we could read your audio files"
}

class NotificationPermissionDescriptionProvider : PermissionDescriptionProvider {
    override val description =
        "Please grant notification permission so you can control audio playback on background"
}