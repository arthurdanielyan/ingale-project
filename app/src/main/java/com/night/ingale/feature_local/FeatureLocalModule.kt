package com.night.ingale.feature_local

import com.night.ingale.feature_local.local_navigation.localNavigationModule
import com.night.ingale.feature_local.main.localMainModule
import com.night.ingale.feature_local.required_permissions_requester_dialog.permissionRequesterModule
import com.night.ingale.feature_local.songs_set.songsSetModule

val featureLocalModule = localMainModule + songsSetModule + permissionRequesterModule + localNavigationModule