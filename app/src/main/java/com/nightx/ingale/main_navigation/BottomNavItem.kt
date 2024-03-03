package com.nightx.ingale.main_navigation

import com.nightx.ingale.R

sealed class BottomNavItem(
    val title: String,
    val icon: Int
) {

    object Local : BottomNavItem("Local Music", R.drawable.bottom_local)
    object Youtube : BottomNavItem("Youtube", R.drawable.bottom_youtube)
}