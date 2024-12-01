package com.nightx.ingale.bottomBar.impl.bottomBar

import com.nightx.ingale.resources.bottomBar.R.drawable as BottomBarIcons
import com.nightx.ingale.resources.strings.R.string as Strings

internal enum class BottomBarItem(
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