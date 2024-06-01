package com.nightx.ingale.feature_local.main.presentation.ui.uiComponents

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.presentation.ui.modifierExt.shimmer
import com.nightx.ingale.feature_local.local_core.domain.functions.highlight
import com.nightx.ingale.feature_local.local_core.domain.model.SongsSet
import com.nightx.ingale.feature_local.local_core.presentation.song_item.SongIcon
import com.nightx.ingale.mvi.wrappers.StableList
import com.nightx.ingale.ui.theme.dimensions
import kotlin.math.roundToInt

/**
* For Albums, Artists sections and playlists
* */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonSongSetsGrid(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    items : StableList <SongsSet>,
    onClick: (songsSet: SongsSet) -> Unit,
    isLoading: Boolean,
    query: String,
) {
    if(!isLoading) {
        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(MaterialTheme.dimensions.normal)
        ) {
            items(
                items = items,
                key = {
                    it.id
                }
            ) {
                SongsSetCard(
                    modifier = Modifier.animateItemPlacement(
                        tween(durationMillis = ItemPlacementAnimationDuration)
                    )
                    ,
                    onClick = onClick,
                    highlightedText = query,
                    songsSet = it
                )
            }
        }
    } else {
        var freeHeight by remember { mutableIntStateOf(1) }
        val spacing = MaterialTheme.dimensions
        val density = LocalDensity.current
        val itemHeight = remember {
            density.run {
                (80.dp - spacing.small * 2).toPx().roundToInt()
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged {
                    freeHeight = it.height
                },
            contentPadding = PaddingValues(MaterialTheme.dimensions.normal)
        ) {
            items((2*freeHeight/itemHeight).coerceAtLeast(0)) {
                Box(
                    modifier = Modifier
                        .padding(MaterialTheme.dimensions.small)
                        .height(80.dp - MaterialTheme.dimensions.small * 2)
                        .clip(RoundedCornerShape(10))
                        .shimmer()
                )
            }
        }
    }
}

@Composable
private fun SongsSetCard(
    modifier: Modifier = Modifier,
    onClick: (SongsSet) -> Unit,
    highlightedText: String,
    songsSet: SongsSet
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .requiredHeight(SongsSetCardHeight)
            .clip(RoundedCornerShape(10))
            .clickable {
                onClick(songsSet)
            }
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(10)
            ),
    ) {
        SongIcon(
            modifier = Modifier.padding(MaterialTheme.dimensions.normal),
            model = songsSet.iconPath,
            shape = RoundedCornerShape(10)
        )
        Text(
            text = songsSet.title.highlight(highlightedText),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private val SongsSetCardHeight = 80.dp
private const val ItemPlacementAnimationDuration = 300
