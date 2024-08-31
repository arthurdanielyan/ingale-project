package com.nightx.ingale.featureLocal.core.ui.song_item

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
import com.nightx.featureLocal.core.viewState.song.SongViewState
import com.nightx.ingale.core.ui.SongIcon
import com.nightx.ingale.core.ui.highlight
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.featureLocal.core.ui.songOperationsBottomSheet.SongOperations
import com.nightx.ingale.featureLocal.core.ui.songOperationsBottomSheet.SongOperationsBottomSheet

@SuppressLint("SuspiciousIndentation")
@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    song: SongViewState,
    onSongClick: (SongViewState) -> Unit,
    highlightedPart: String = "",
    onSongOperation: (SongOperations) -> Unit,
) {
    var songOperationSheetExpanded by rememberSaveable { mutableStateOf(false) }
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(SongItemHeight)
            .background(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onSongClick(song)
            }
            .padding(
                vertical = MaterialTheme.dimensions.normal,
                horizontal = MaterialTheme.dimensions.large,
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
            model = song.picturePath
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
    if (songOperationSheetExpanded)
        SongOperationsBottomSheet(
            onOperation = onSongOperation,
            onDismissRequest = { songOperationSheetExpanded = false }
        )
}

private val SongItemHeight = 80.dp
