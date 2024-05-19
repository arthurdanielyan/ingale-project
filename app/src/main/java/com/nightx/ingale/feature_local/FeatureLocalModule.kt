package com.nightx.ingale.feature_local

import com.nightx.ingale.feature_local.local_navigation.localNavigationModule
import com.nightx.ingale.feature_local.main.localMainModule
import com.nightx.ingale.feature_local.required_permissions_requester_dialog.permissionRequesterModule
import com.nightx.ingale.feature_local.songs_set.songsSetModule

val featureLocalModule = localMainModule + songsSetModule +
    permissionRequesterModule + localNavigationModule