package com.night.ingale.feature_local.main.presentation.ui_components

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.night.ingale.core.presentation.view.LaunchedEffect
import com.night.ingale.mvi.wrappers.StableList
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> TabRow(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    itemContent: @Composable (item: T) -> Unit,
    items: StableList<T>
) {
    var selectedItemIndex by remember {
        mutableIntStateOf(pagerState.currentPage)
    }
    var currentWidth by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    val offsets = remember {
        Array(items.size) { 0 }
    }
    var offset by rememberSaveable {
        mutableIntStateOf(0)
    }
    val widthAnim by animateDpAsState(
        targetValue = currentWidth.dp,
        animationSpec = tween(
            durationMillis = 100,
            easing = LinearEasing
        ),
        label = "length of the line"
    )
    val offsetAnim by animateIntAsState(
        targetValue = offset,
        animationSpec = tween(
            durationMillis = 100,
            easing = LinearEasing
        ),
        label = "offset of the line"
    )

    var dragOffset by remember {
        mutableIntStateOf(0)
    }
    var isTargetingInProgress by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(
        pagerState.currentPage,
        pagerState.isScrollInProgress,
        pagerState.targetPage,
        isTargetingInProgress,
        pagerState.currentPageOffsetFraction
    ) { currentPage, isScrollInProgress, targetPage, isTargetingInProgress, currentPageOffsetFraction ->
        if (isTargetingInProgress) { // scrolling by click
            selectedItemIndex = targetPage
        } else if (isScrollInProgress) { // userDragging
            selectedItemIndex = currentPage
            val estimatedTargetPage =
                (currentPage + if (currentPageOffsetFraction > 0) 1 else -1).coerceIn(
                    0,
                    offsets.size - 1
                )
            Log.d("myLogs", "fraction: $currentPageOffsetFraction")
            dragOffset =
                offsets[currentPage] + (currentPageOffsetFraction * offsets[estimatedTargetPage]).roundToInt()
        }
    }

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier
                            .clickable {
                                scope.launch {
                                    isTargetingInProgress = true
                                    pagerState.animateScrollToPage(index)
                                    isTargetingInProgress = false
                                }
                                selectedItemIndex = index
                            }
                            .onGloballyPositioned {
                                val currentOffset = it.positionInParent().x.roundToInt()
                                offsets[index] = currentOffset
                                if (index == selectedItemIndex) {
                                    currentWidth = density.run {
                                        it.size.width.toDp().value
                                    }
                                }
                            }
                    ) {
                        itemContent(item)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            if (isTargetingInProgress) offsetAnim else dragOffset,
                            0
                        )
                    }
                    .align(Alignment.BottomStart)
                    .width(widthAnim)
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}