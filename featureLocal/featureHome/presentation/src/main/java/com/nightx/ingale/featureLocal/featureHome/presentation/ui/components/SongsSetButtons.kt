package com.nightx.ingale.featureLocal.featureHome.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.ui.theme.colorScheme.ingaleColors
import com.nightx.ingale.core.ui.theme.dimensions
import com.nightx.ingale.resources.strings.R.string as Strings

@Composable
internal fun SongsSetButtons(
    onPlaylistsClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(80.dp)
            .padding(MaterialTheme.dimensions.normal),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.normal)
    ) {
        SongsSetButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, true),
            gradientWeak = MaterialTheme.ingaleColors.additional1,
            text = stringResource(Strings.playlists),
            onClick = onPlaylistsClick
        )
        SongsSetButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, true),
            gradientWeak = MaterialTheme.ingaleColors.additional2,
            text = stringResource(Strings.favourites),
            onClick = onFavouritesClick
        )
        SongsSetButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, true),
            gradientWeak = MaterialTheme.ingaleColors.additional3,
            text = stringResource(Strings.history),
            onClick = onHistoryClick
        )
    }
}