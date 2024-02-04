package com.night.ingale.main_navigation

import com.night.ingale.R

sealed class BottomNavItem(
    val title: String,
    val icon: Int
) {

    object Local : BottomNavItem("Local Music", R.drawable.bottom_local)
    object Youtube : BottomNavItem("Youtube", R.drawable.bottom_youtube)
}