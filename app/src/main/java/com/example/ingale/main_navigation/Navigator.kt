package com.example.ingale.main_navigation

import androidx.navigation.NavController

abstract class Navigator(
    private val navController: NavController
) {

    fun back() {
        navController.popBackStack()
    }
}