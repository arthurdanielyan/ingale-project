package com.nightx.ingale.bottomBar.impl.uiComponents

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import com.nightx.ingale.bottomBar.impl.BottomBarItem
import com.nightx.ingale.core.ui.slideInLeft
import com.nightx.ingale.core.ui.slideInRight
import com.nightx.ingale.core.ui.slideOutLeft
import com.nightx.ingale.core.ui.slideOutRight

@Composable
internal fun SlidingBottomTabSwitcher(
    selectedTab: BottomBarItem,
    content: @Composable (BottomBarItem) -> Unit,
) {
    val holder = rememberSaveableStateHolder()
    AnimatedContent(
        targetState = selectedTab,
        transitionSpec = { getContentTransformation(initialState, targetState) },
        label = "BottomTabSwitcher"
    ) {
        holder.SaveableStateProvider(key = it.ordinal) {
            content(it)
        }
    }
}

/**
 * Determines the content transformation (enter/exit animations)
 * based on the direction of the transition between tabs.
 *
 * @param from The currently displayed BottomBarItem.
 * @param to The BottomBarItem that the user is navigating to.
 * @return ContentTransform with appropriate enter/exit animations.
 */
private fun getContentTransformation(
    from: BottomBarItem,
    to: BottomBarItem,
): ContentTransform = (from.ordinal < to.ordinal).let { isSlidingRight ->
    ContentTransform(
        targetContentEnter = if (isSlidingRight) {
            slideInLeft
        } else {
            slideInRight
        },
        initialContentExit = if (isSlidingRight) {
            slideOutLeft
        } else {
            slideOutRight
        }
    )
}
