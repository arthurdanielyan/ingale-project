package com.example.ingale.feature_local

import com.example.ingale.feature_local.local_navigation.localNavigationModule
import com.example.ingale.feature_local.main.localMainModule
import com.example.ingale.feature_local.permissionRequester.permissionRequesterModule
import com.example.ingale.feature_local.songs_set.songsSetModule

val featureLocalModule = localMainModule + songsSetModule + permissionRequesterModule + localNavigationModule