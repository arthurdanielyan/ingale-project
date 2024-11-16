package com.nightx.ingale.featureLocal.featureSongsSet.presentation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nightx.featureLocal.core.viewState.SongsSetViewState
import com.nightx.ingale.core.presentation.diExt.ingaleViewModels
import com.nightx.ingale.core.ui.SongIcon
import com.nightx.ingale.core.ui.extensions.alpha
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.core.ui.topBar.TopBarBackButton
import com.nightx.ingale.core.ui.topBar.TopBarHeight
import com.nightx.ingale.core.ui.topBar.TopBarWithBackButton
import com.nightx.ingale.featureLocal.core.ui.SongsLazyList
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.viewModel.SongsSetCallbacks
import com.nightx.ingale.featureLocal.featureSongsSet.presentation.view.viewModel.SongsSetViewModel
import com.nightx.ingale.musicbar.view.rememberNestedScrollForMusicBarNotification

@Composable
fun SongsSetScreen(savedStateHandle: SavedStateHandle) {
    val vm = ingaleViewModels<SongsSetViewModel>(
        savedStateHandle = savedStateHandle
    )
    val state by vm.state.collectAsStateWithLifecycle()

    SongsSetScreen(
        state = state,
        callbacks = vm
    )
}

@Composable
internal fun SongsSetScreen(
    state: SongsSetViewState,
    callbacks: SongsSetCallbacks,
) {
    val lazyListState = rememberLazyListState()
    val musicBarNotifier = rememberNestedScrollForMusicBarNotification()

    val density = LocalDensity.current
    val headerAlpha by remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemIndex == 0) {
                lazyListState.layoutInfo.visibleItemsInfo
                    .getOrNull(0)?.size?.toFloat()?.minus(
                        density.run {
                            TopBarHeight.toPx()
                        }
                    )?.let { headerHeight ->
                        1f - lazyListState.firstVisibleItemScrollOffset.toFloat() / headerHeight
                    } ?: return@derivedStateOf 1f
            } else 0f
        }
    }
    val topBarAlpha by remember {
        derivedStateOf {
            1f - headerAlpha
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        TopBarWithBackButton(
            modifier = Modifier
                .zIndex(1f),
            alpha = { topBarAlpha },
            onBackClick = callbacks::onBackClick,
            title = state.title
        )
        SongsLazyList(
            lazyListState = lazyListState,
            modifier = Modifier
                .layoutId("content")
                .nestedScroll(musicBarNotifier),
            songs = state.songs,
            header = {
                SongsSetHeader(
                    modifier = Modifier
                        .alpha { headerAlpha },
                    onBackClick = callbacks::onBackClick,
                    icon = state.iconPath,
                    title = state.title,
                )
            },
            onSongClick = callbacks::onSongClick,
        )
    }
}

@Composable
private fun SongsSetHeader(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    title: String,
    icon: String?,
) {
    Box(
        modifier = modifier
            .padding(
                start = MaterialTheme.dimensions.normal,
                end = MaterialTheme.dimensions.normal,
                bottom = MaterialTheme.dimensions.large,
            ),
        contentAlignment = Alignment.Center
    ) {
        TopBarBackButton(
            modifier = Modifier
                .padding(
                    top = MaterialTheme.dimensions.normal
                )
                .align(Alignment.TopStart),
            onClick = onBackClick,
        )
        Column(
            modifier = modifier
                .padding(
                    top = MaterialTheme.dimensions.extraLarge
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SongIcon(
                model = icon,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(MaterialTheme.dimensions.large),
                    ),
                shape = RoundedCornerShape(MaterialTheme.dimensions.large)
            )
            Text(
                text = title,
                fontSize = 24.sp,
                modifier = Modifier
            )
        }
    }
}

