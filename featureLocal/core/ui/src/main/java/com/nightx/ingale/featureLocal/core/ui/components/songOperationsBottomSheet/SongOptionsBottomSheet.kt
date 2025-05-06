package com.nightx.ingale.featureLocal.core.ui.components.songOperationsBottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.ui.bottomSheet.ModalBottomSheet
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.featureLocal.core.ui.components.songOperationsBottomSheet.SongOperations.ADD_TO_QUEUE
import com.nightx.ingale.featureLocal.core.ui.components.songOperationsBottomSheet.SongOperations.DELETE
import com.nightx.ingale.featureLocal.core.ui.components.songOperationsBottomSheet.SongOperations.MODIFY_SONG_INFO
import com.nightx.ingale.featureLocal.core.ui.components.songOperationsBottomSheet.SongOperations.PLAY_NEXT
import com.nightx.ingale.featureLocal.core.ui.components.songOperationsBottomSheet.SongOperations.SAVE_TO_FAVOURITES
import com.nightx.ingale.featureLocal.core.ui.components.songOperationsBottomSheet.SongOperations.SAVE_TO_PLAYLIST
import com.nightx.ingale.resources.songOperations.R.drawable as SongOperationDrawables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongOperationsBottomSheet(
    onOperation: (SongOperations) -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier
            .padding(MaterialTheme.dimensions.large)
            .clip(RoundedCornerShape(MaterialTheme.dimensions.large)),
        onDismissRequest = onDismissRequest,
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
private fun BottomSheetButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    iconPainter: Painter = painterResource(SongOperationDrawables.operation_playlist),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(MaterialTheme.dimensions.normal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(36.dp),
            painter = iconPainter,
            contentDescription = text
        )
        Spacer(modifier = Modifier.width(MaterialTheme.dimensions.normal))
        Text(
            text = text
        )
    }
}