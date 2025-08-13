package com.nightx.ingale.root.impl.root.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.nightx.ingale.root.api.root.BottomBarItemViewState

@Composable
internal fun BoxScope.BottomBar(
    modifier: Modifier = Modifier,
    offset: Density.() -> Int,
    selectedTab: BottomBarItemViewState,
    onTabSelected: (BottomBarItemViewState) -> Unit,
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(BottomBarHeight)
            .align(Alignment.BottomCenter)
            .offset {
                IntOffset(
                    x = 0,
                    y = offset()
                )
            },
        containerColor = MaterialTheme.colorScheme.inversePrimary
    ) {
        BottomBarItemViewState.entries.forEach { item ->
            BottomBarItem(
                item = item,
                isSelected = selectedTab == item,
                onClick = {
                    onTabSelected(item)
                }
            )
        }
    }
}

internal val BottomBarHeight = 80.dp
