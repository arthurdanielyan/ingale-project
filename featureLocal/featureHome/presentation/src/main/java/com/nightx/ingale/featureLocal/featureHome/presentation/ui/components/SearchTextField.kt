package com.nightx.ingale.featureLocal.featureHome.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.nightx.ingale.core.ui.IngaleTextField
import com.nightx.ingale.resources.strings.R.string as Strings

@Composable
internal fun SearchTextField(
    text: String,
    onType: (String) -> Unit
) {
    IngaleTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        value = text,
        onValueChange = onType,
        placeholder = stringResource(Strings.search),
        trailingIcon = Icons.Default.Close,
        onTrailingIconClick = {
            onType("")
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        )
    )
}