package com.nightx.ingale.bottomBarApi

import kotlinx.coroutines.flow.StateFlow

interface BottomBarController {

    val isBottomBarVisible: StateFlow<Boolean>

    fun setVisibility(isVisible: Boolean)
}
