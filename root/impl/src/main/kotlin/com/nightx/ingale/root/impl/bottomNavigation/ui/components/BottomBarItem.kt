package com.nightx.ingale.root.impl.bottomNavigation.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.root.api.bottomBar.BottomBarItemViewState
import com.nightx.ingale.root.impl.bottomNavigation.ui.BottomBarHeight

@Composable
internal fun RowScope.BottomBarItem(
    item: BottomBarItemViewState,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.inversePrimary,
            selectedTextColor = MaterialTheme.colorScheme.inverseSurface,
            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
            unselectedTextColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier.requiredSize(1.15 * BottomBarHeight),
        selected = isSelected,
        onClick = onClick,
        icon = {
            Icon(
                modifier = Modifier
                    .size(45.dp),
                painter = painterResource(item.icon),
                contentDescription = stringResource(item.titleKey)
            )
        },
        label = {
            MaterialTheme.dimensions.normal
            Text(
                text = stringResource(item.titleKey),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    )
}