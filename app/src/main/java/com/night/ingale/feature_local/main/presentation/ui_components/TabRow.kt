package com.night.ingale.feature_local.main.presentation.ui_components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.night.ingale.mvi.wrappers.StableList
import kotlin.math.roundToInt

@Composable
fun <T> TabRow(
    modifier: Modifier = Modifier,
    itemContent: @Composable (item: T) -> Unit,
    onSelect: (index: Int, item: T) -> Unit,
    selectedItemIndex: () -> Int,
    items: StableList<T>
) {
    var selectedItemIndexUpt by remember(selectedItemIndex()) { mutableIntStateOf(selectedItemIndex()) }
    var currentWidth by rememberSaveable {
        mutableFloatStateOf(0f)
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
    val density = LocalDensity.current

    Box(
        contentAlignment = Alignment.BottomStart
    ) {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            itemsIndexed(
                items = items,
                key = { index, _ ->
                    index
                }
            ) { index, item ->
                Box(
                    modifier = Modifier
                        .clickable {
                            onSelect(index, item)
                            selectedItemIndexUpt = index
                        }
                        .onGloballyPositioned {
                            if (index == selectedItemIndexUpt) {
                                currentWidth = density.run {
                                    it.size.width.toDp().value
                                }

                                offset = it.positionInParent().x.roundToInt()
                            }
                        }
                ) {
                    itemContent(item)
                }
            }
        }
    }
    key(offsetAnim) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetAnim, 0) }
                .width(widthAnim)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onBackground)
        )
    }
}