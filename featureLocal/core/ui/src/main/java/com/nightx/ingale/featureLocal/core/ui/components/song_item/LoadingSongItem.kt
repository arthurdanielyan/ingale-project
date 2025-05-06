package com.nightx.ingale.featureLocal.core.ui.components.song_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.nightx.ingale.core.ui.extensions.shimmer
import com.nightx.ingale.core.ui.theme.dimensions


@Composable
fun LoadingSongItem(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = MaterialTheme.dimensions.normal,
                horizontal = MaterialTheme.dimensions.large,
            )
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(100)
            )
            .clip(RoundedCornerShape(100))
    ) {
        val (songIcon, songInfoCol) = createRefs()

        Box(
            modifier = Modifier
                .size(60.dp)
                .constrainAs(songIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .clip(RoundedCornerShape(100))
                .shimmer()
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.constrainAs(songInfoCol) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(songIcon.end, margin = 8.dp)
                end.linkTo(parent.end, margin = 40.dp)

                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            }
        ) {
            Spacer(modifier = Modifier.weight(0.1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .shimmer()
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .shimmer()
            )
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}