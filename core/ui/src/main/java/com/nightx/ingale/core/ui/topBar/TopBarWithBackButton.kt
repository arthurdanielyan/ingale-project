package com.nightx.ingale.core.ui.topBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.ui.autoSizeText.GoodAutoSizeText
import com.nightx.ingale.core.ui.extensions.alpha
import com.nightx.ingale.core.ui.theme.dimensions

@Composable
fun TopBarWithBackButton(
    modifier: Modifier = Modifier,
    alpha: () -> Float,
    onBackClick: () -> Unit,
    title: String,
) {
    val isVisible by remember {
        derivedStateOf {
            alpha() > 0f
        }
    }
    if (isVisible) {
        Box(
            modifier = modifier
                .alpha {
                    alpha()
                }
                .fillMaxWidth()
                .requiredHeight(TopBarHeight)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = MaterialTheme.dimensions.normal),
        ) {
            TopBarBackButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(vertical = MaterialTheme.dimensions.normal),
                onClick = onBackClick,
            )
            GoodAutoSizeText(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = MaterialTheme.dimensions.large),
                text = title,
                forceFit = true,
            )
        }
    }
}

private val TopBarHeight = 64.dp
