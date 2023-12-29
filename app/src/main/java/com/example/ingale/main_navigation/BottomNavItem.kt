package com.example.ingale.main_navigation

import com.example.ingale.R

sealed class BottomNavItem(
    val title: String,
    val icon: Int
) {

    object Local : BottomNavItem("Local Music", R.drawable.bottom_local)
    object Youtube : BottomNavItem("Youtube", R.drawable.bottom_youtube)
}