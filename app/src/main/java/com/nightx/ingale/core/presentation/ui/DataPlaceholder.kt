package com.nightx.ingale.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.nightx.ingale.R
import com.nightx.ingale.ui.theme.spacing

@Composable
fun DataPlaceholder(
    title: String,
    description: String? = null,
    actionButtonText: String = stringResource(R.string.refresh),
    onAction: () -> Unit,
    additionalButtonText: String? = null,
    onAdditionalAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.large),
        verticalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.spacing.large,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        description?.let {
            Text(
                text = description,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Button(
            onClick = onAction,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = actionButtonText,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
        additionalButtonText?.let {
            Button(
                onClick = onAdditionalAction ?: {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = additionalButtonText,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}