package com.nightx.ingale.featureLocal.featureHome.presentation.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.ingale.core.ui.SongIcon
import com.nightx.ingale.core.ui.extensions.shimmer
import com.nightx.ingale.core.ui.highlight
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.core.viewState.StableList

/**
 * For Albums, Artists sections and for Playlists in the future
 * */
@Composable
internal fun CommonSongSetsGrid(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    items: StableList<SongsSetViewState>,
    onClick: (songsSet: SongsSetViewState) -> Unit,
    isLoading: Boolean,
    query: String,
) {
    if (!isLoading) {
        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(MaterialTheme.dimensions.normal),
            horizontalArrangement = Arrangement.spacedBy(SongsSetCardPadding),
            verticalArrangement = Arrangement.spacedBy(SongsSetCardPadding),
        ) {
            items(
                items = items,
                key = {
                    it.id
                }
            ) {
                SongsSetCard(
                    modifier = Modifier.animateItem(
                        tween(durationMillis = ItemPlacementAnimationDuration)
                    ),
                    onClick = onClick,
                    highlightedText = query,
                    songsSet = it
                )
            }
        }
    } else {
        SongsSetsGridSkeleton()
    }
}

@Composable
private fun SongsSetsGridSkeleton() {
    BoxWithConstraints {
        val skeletonCount = rememberSkeletonCount(maxHeight)
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(MaterialTheme.dimensions.normal),
            horizontalArrangement = Arrangement.spacedBy(SongsSetCardSkeletonPadding),
            verticalArrangement = Arrangement.spacedBy(SongsSetCardSkeletonPadding),
            userScrollEnabled = false,
        ) {
            items(skeletonCount) {
                SongsSetCardSkeleton()
            }
        }
    }
}

@Composable
private fun SongsSetCard(
    modifier: Modifier = Modifier,
    onClick: (SongsSetViewState) -> Unit,
    highlightedText: String,
    songsSet: SongsSetViewState,
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

@Composable
private fun SongsSetCardSkeleton() {
    Box(
        modifier = Modifier
            .requiredHeight(SongsSetCardHeight)
            .clip(RoundedCornerShape(10))
            .shimmer()
    )
}

@Composable
private fun rememberSkeletonCount(freeHeight: Dp) =
    remember {
        (2 * freeHeight / SongsSetCardHeight).toInt() + 1
    }

private val SongsSetCardHeight = 80.dp
private val SongsSetCardPadding: Dp
    @Composable get() = MaterialTheme.dimensions.small
private val SongsSetCardSkeletonPadding: Dp
    @Composable get() = MaterialTheme.dimensions.normal
private const val ItemPlacementAnimationDuration = 300
