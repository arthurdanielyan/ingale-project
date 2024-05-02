package com.nightx.ingale.main_navigation

import com.nightx.ingale.R

sealed class BottomNavItem(
    val titleKey: Int,
    val icon: Int
) {

    object Local : BottomNavItem(R.string.local_music, R.drawable.bottom_local)
    object Youtube : BottomNavItem(R.string.youtube, R.drawable.bottom_youtube)
}