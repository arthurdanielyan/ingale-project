package com.nightx.ingale.bottomBar.impl

import com.nightx.ingale.resources.bottomBar.R.drawable as BottomBarIcons
import com.nightx.ingale.resources.strings.R.string as Strings

internal sealed class BottomNavItem(
    val titleKey: Int,
    val icon: Int
) {

    data object Local : BottomNavItem(Strings.local_music, BottomBarIcons.bottom_local)
    data object Youtube : BottomNavItem(Strings.youtube, BottomBarIcons.bottom_youtube)
}