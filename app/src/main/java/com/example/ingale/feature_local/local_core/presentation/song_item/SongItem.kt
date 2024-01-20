package com.example.ingale.feature_local.local_core.presentation.song_item

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.ingale.feature_local.local_core.domain.functions.highlight
import com.example.ingale.feature_local.local_core.domain.model.SongOperations
import com.example.ingale.feature_local.local_core.presentation.SongOperationsBottomSheet
import com.example.ingale.core.domain.model.Song
import com.example.ingale.ui.theme.colorScheme.ingaleColors
import com.example.ingale.ui.theme.spacing

@SuppressLint("SuspiciousIndentation")
@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    song: Song,
    onSongClick: (Song) -> Unit,
    highlightedPart: String = "",
    onSongOperation: (SongOperations) -> Unit
) {
    var songOperationSheetExpanded by rememberSaveable { mutableStateOf(false) }
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(SongItemHeight)
            .background(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.ingaleColors.background,
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                onClick = {
                    onSongClick(song)
                }
            )
            .padding(
                vertical = MaterialTheme.spacing.normal,
                horizontal = MaterialTheme.spacing.large,
            )
            .clip(RoundedCornerShape(100))
    ) {
        val (songIcon, songInfoCol, songOptionsIcon) = createRefs()

        SongIcon(
            modifier = Modifier.constrainAs(songIcon) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            },
            picture = song.picture
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.constrainAs(songInfoCol) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(songIcon.end, margin = 8.dp)
                end.linkTo(songOptionsIcon.start, margin = 8.dp)

                width = Dimension.fillToConstraints
            }
        ) {
            Text(
                text = song.title.highlight(highlight = highlightedPart),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${song.artist} | ${song.album}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Song options",
            modifier = Modifier
                .size(25.dp)
                .constrainAs(songOptionsIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, margin = 8.dp)
                }
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(20)
                )
                .clip(RoundedCornerShape(20))
                .clickable { songOperationSheetExpanded = true }
        )
    }
    if(songOperationSheetExpanded)
    SongOperationsBottomSheet(
        onOperation = onSongOperation,
        onDismissRequest = { songOperationSheetExpanded = false }
    )
}

private val SongItemHeight = 80.dp
