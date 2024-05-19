package com.nightx.ingale.feature_local.main.presentation.view.mappers

import com.nightx.ingale.core.domain.Mapper
import com.nightx.ingale.feature_local.local_core.domain.model.SongsSet
import com.nightx.ingale.feature_local.local_navigation.destinations.SongsSetScreenDestination

class SongsSetToNavArgMapper : Mapper<SongsSet, SongsSetScreenDestination.SongsSet> {

    override operator fun invoke(from: SongsSet): SongsSetScreenDestination.SongsSet {
        return SongsSetScreenDestination.SongsSet(
            id = from.id,
            title = from.title,
            songs = from.songs,
            iconPath = from.iconPath
        )
    }
}