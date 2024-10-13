package com.nightx.ingale.bottomBar.impl

import androidx.compose.runtime.saveable.Saver
import com.nightx.ingale.resources.bottomBar.R.drawable as BottomBarIcons
import com.nightx.ingale.resources.strings.R.string as Strings

internal sealed class BottomBarItem(
    val id: Int,
    val titleKey: Int,
    val icon: Int,
) {

    data object Local : BottomBarItem(
        id = LocalId,
        titleKey = Strings.local_music,
        icon = BottomBarIcons.bottom_local,
    )

    data object Youtube : BottomBarItem(
        id = YoutubeId,
        titleKey = Strings.youtube,
        icon = BottomBarIcons.bottom_youtube
    )

    companion object {
        const val LocalId = 0
        const val YoutubeId = 1

        val ListSaver = Saver<List<BottomBarItem>, IntArray>(
            save = { original ->
                original.map { it.id }.toIntArray()
            },
            restore = { ids ->
                ids.map {
                    when (it) {
                        LocalId -> Local
                        YoutubeId -> Youtube
                        else -> throw IllegalArgumentException("Unknown bottom bar item id: $it")
                    }
                }
            }
        )
    }
}