package com.nightx.ingale.root.api.bottomBar

import androidx.compose.runtime.Immutable
import com.nightx.ingale.resources.bottomBar.R.drawable as BottomBarIcons
import com.nightx.ingale.resources.strings.R.string as Strings

@Immutable
data class BottomNavigationState(
    val selectedTab: BottomBarItemViewState = BottomBarItemViewState.Local,
    val isVisible: Boolean = true,
)

enum class BottomBarItemViewState(
    val titleKey: Int,
    val icon: Int,
) {

    Local(
        titleKey = Strings.local_music,
        icon = BottomBarIcons.bottom_local,
    ),

    Youtube(
        titleKey = Strings.youtube,
        icon = BottomBarIcons.bottom_youtube,
    )
}