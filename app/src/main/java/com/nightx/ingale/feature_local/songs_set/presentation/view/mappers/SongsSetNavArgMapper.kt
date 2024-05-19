package com.nightx.ingale.feature_local.songs_set.presentation.view.mappers

import com.nightx.ingale.core.domain.Mapper
import com.nightx.ingale.feature_local.local_core.domain.model.SongsSet
import com.nightx.ingale.feature_local.local_navigation.destinations.SongsSetScreenDestination
import com.nightx.ingale.mvi.wrappers.toStableList

class SongsSetNavArgMapper : Mapper<SongsSetScreenDestination.SongsSet, SongsSet> {

    override fun invoke(from: SongsSetScreenDestination.SongsSet) =
        SongsSet(
            id = from.id,
            title = from.title,
            songs = from.songs.toStableList(),
            iconPath = from.iconPath
        )
}