package com.nightx.ingale.feature_local.main.presentation.ui_components

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.presentation.view.LaunchedEffect
import com.nightx.ingale.mvi.wrappers.StableList
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

data class TabBounds(
    val width: Float = 0f,
    val offset: Int = 0
)

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
    var tabsBounds by remember {
        mutableStateOf(
            Array(items.size) {
                TabBounds()
            }
        )
    }
    var dragOffset by remember {
        mutableIntStateOf(0)
    }
    var dragWidth by remember {
        mutableFloatStateOf(0f)
    }
    val isUserDragging by pagerState.interactionSource.collectIsDraggedAsState()
    val widthAnim by animateDpAsState(
        targetValue = dragWidth.dp,
        animationSpec = tween(
            durationMillis = pageSettleDuration,
            easing = LinearEasing
        ),
        label = "length of the line"
    )
    val offsetAnim by animateIntAsState(
        targetValue = dragOffset,
        animationSpec = tween(
            durationMillis = if(!isUserDragging) {
                pageSettleDuration
            } else 0,
            easing = LinearEasing
        ),
        label = "offset of the line"
    )
    LaunchedEffect(
        pagerState.isScrollInProgress,
        pagerState.currentPageOffsetFraction,
    ) { isScrollInProgress, fraction ->
        Log.d("myLogs", "$isUserDragging")
        if (!isUserDragging) { // scrolling by click
            selectedItemIndex = pagerState.targetPage
            dragWidth = tabsBounds[selectedItemIndex].width
            dragOffset = tabsBounds[selectedItemIndex].offset
        } else if (isScrollInProgress) { // userDragging
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
                dragOffset = currentBounds.offset + (progress*diff).roundToInt()
                dragWidth = currentBounds.width + progress*(targetBounds.width-currentBounds.width)
            } else if(fraction < 0) {
                val currentBounds = tabsBounds[pagerState.currentPage]
                val targetBounds = tabsBounds[pagerState.currentPage-1]
                val diff = currentBounds.offset - targetBounds.offset
                val progress = 1f-fraction.absoluteValue
                dragOffset = targetBounds.offset + (progress*diff).roundToInt()
                dragWidth = currentBounds.width + progress*(targetBounds.width-currentBounds.width)
            }
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
                                    pagerState.animateScrollToPage(
                                        page = index,
                                        animationSpec = tween(
                                            durationMillis = pageSettleDuration,
                                            easing = LinearEasing
                                        )
                                    )
                                }
                                selectedItemIndex = index
                            }
                            .onPlaced {
                                val currentOffset = it.positionInParent().x.roundToInt()
                                tabsBounds = tabsBounds.apply {
                                    this[index] = TabBounds(
                                        width = density.run { it.size.width.toDp().value },
                                        offset = currentOffset
                                    )
                                }
                                if(selectedItemIndex == index) {
                                    dragOffset = tabsBounds[index].offset
                                    dragWidth = tabsBounds[index].width
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
                            offsetAnim,
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

private const val pageSettleDuration = 200
