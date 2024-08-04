package com.nightx.ingale.featureLocal.featureHome.presentation.ui.uiComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nightx.ingale.R
import com.nightx.ingale.ui.theme.colorScheme.ingaleColors
import com.nightx.ingale.ui.theme.dimensions

@Composable
internal fun SongsSetButtons(
    onPlaylistsClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(MaterialTheme.dimensions.normal),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.normal)
    ) {
        SongsSetButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, true),
            gradientWeak = MaterialTheme.ingaleColors.additional1,
            text = stringResource(R.string.playlists),
            onClick = onPlaylistsClick
        )
        SongsSetButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, true),
            gradientWeak = MaterialTheme.ingaleColors.additional2,
            text = stringResource(R.string.favourites),
            onClick = onFavouritesClick
        )
        SongsSetButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, true),
            gradientWeak = MaterialTheme.ingaleColors.additional3,
            text = stringResource(R.string.history),
            onClick = onHistoryClick
        )
    }
}