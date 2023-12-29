package com.example.ingale.feature_local.local_core.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ingale.R
import com.example.ingale.feature_local.local_core.domain.model.SongOperations
import com.example.ingale.feature_local.local_core.domain.model.SongOperations.ADD_TO_QUEUE
import com.example.ingale.feature_local.local_core.domain.model.SongOperations.DELETE
import com.example.ingale.feature_local.local_core.domain.model.SongOperations.MODIFY_SONG_INFO
import com.example.ingale.feature_local.local_core.domain.model.SongOperations.PLAY_NEXT
import com.example.ingale.feature_local.local_core.domain.model.SongOperations.SAVE_TO_FAVOURITES
import com.example.ingale.feature_local.local_core.domain.model.SongOperations.SAVE_TO_PLAYLIST
import com.example.ingale.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongOperationsBottomSheet(
    onOperation: (SongOperations) -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column {
            BottomSheetButton(
                text = "Modify song info",
                onClick = { onOperation(MODIFY_SONG_INFO) }
            )
            BottomSheetButton(
                text = "Save to playlist",
                onClick = { onOperation(SAVE_TO_PLAYLIST) }
            )
            BottomSheetButton(
                text = "Save to favourites",
                onClick = { onOperation(SAVE_TO_FAVOURITES) }
            )
            BottomSheetButton(
                text = "Play next",
                onClick = { onOperation(PLAY_NEXT) }
            )
            BottomSheetButton(
                text = "Add to queue",
                onClick = { onOperation(ADD_TO_QUEUE) }
            )
            BottomSheetButton(
                text = "Delete",
                onClick = { onOperation(DELETE) }
            )
        }
    }
}

@Composable
fun BottomSheetButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    iconPainter: Painter = painterResource(R.drawable.operation_playlist),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(MaterialTheme.spacing.normal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(36.dp),
            painter = iconPainter,
            contentDescription = text
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.normal))
        Text(
            text = text
        )
    }
}