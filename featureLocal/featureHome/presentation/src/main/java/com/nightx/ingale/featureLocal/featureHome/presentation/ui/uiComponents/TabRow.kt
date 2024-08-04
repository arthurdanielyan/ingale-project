package com.nightx.ingale.featureLocal.featureHome.presentation.ui.uiComponents

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.viewState.StableList
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

private data class TabBounds(
    val width: Float = 0f,
    val offset: Int = 0
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun <T> TabRow(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    items: StableList<T>,
    userScrollEnabled: Boolean = true,
    itemContent: @Composable (item: T) -> Unit
) {
    var selectedItemIndex by remember {
        mutableIntStateOf(pagerState.currentPage)
    }
    var tabsBounds by remember {
        mutableStateOf(
            Array(items.size) {
                TabBounds()
            }
        )
    }
    var dragWidth by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    val dragOffset = rememberSaveable(
        pagerState.isScrollInProgress,
        pagerState.currentPageOffsetFraction
    ) {
        val fraction = pagerState.currentPageOffsetFraction
        if(fraction > 0) {
            pagerState.currentPage + 1
        } else {
            pagerState.currentPage
        }
        if(fraction > 0) {
            val currentBounds = tabsBounds[pagerState.currentPage]
            val targetBounds = tabsBounds[pagerState.currentPage+1]
            val diff = targetBounds.offset - currentBounds.offset
            val progress = fraction
            dragWidth = currentBounds.width + progress*(targetBounds.width-currentBounds.width)
            currentBounds.offset + (progress*diff).roundToInt()
        } else if(fraction < 0) {
            val currentBounds = tabsBounds[pagerState.currentPage]
            val targetBounds = tabsBounds[pagerState.currentPage-1]
            val diff = currentBounds.offset - targetBounds.offset
            val progress = fraction.absoluteValue.reverseProgress()
            dragWidth = currentBounds.width + progress.reverseProgress()*(targetBounds.width-currentBounds.width)
            targetBounds.offset + (progress*diff).roundToInt()
        } else { // fraction == 0
            dragWidth = tabsBounds[pagerState.currentPage].width
            tabsBounds[pagerState.currentPage].offset
        }
    }

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = remember(items.size) {
            (maxWidth.value / items.size).dp
        }
        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier
                            .requiredWidthIn(max = maxWidth)
                            .clickable {
                                if(userScrollEnabled) {
                                    scope.launch {
                                        pagerState.animateScrollToPage(
                                            page = index,
                                            animationSpec = tween(
                                                durationMillis = PageSettleDuration,
                                                easing = LinearEasing
                                            )
                                        )
                                    }
                                    selectedItemIndex = index
                                }
                            }
                            .onPlaced {
                                val currentOffset = it.positionInParent().x.roundToInt()
                                tabsBounds = tabsBounds.apply {
                                    this[index] = TabBounds(
                                        width = density.run { it.size.width.toDp().value },
                                        offset = currentOffset
                                    )
                                }
                                if (selectedItemIndex == index) {
                                    dragWidth = tabsBounds[index].width
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        itemContent(item)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            dragOffset,
                            0
                        )
                    }
                    .align(Alignment.BottomStart)
                    .width(dragWidth.dp)
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
        }
    }
}

private fun Float.reverseProgress(): Float {
    require(this in 0f..1f)
    return 1f - this
}

private const val PageSettleDuration = 200
