package com.nightx.ingale.core.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun IngaleTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    singleLine: Boolean = true,
    shape: Shape = CircleShape,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current
    BackHandler(enabled = isFocused) {
        focusManager.clearFocus()
    }
    OutlinedTextField(
        modifier = modifier,
        interactionSource = interactionSource,
        placeholder = placeholder?.let {
            @Composable {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        value = value,
        onValueChange = onValueChange,
        trailingIcon = trailingIcon?.let {
            @Composable {
                Icon(
                    imageVector = it,
                    contentDescription = "Remove search text",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.clickable {
                        onTrailingIconClick?.invoke()
                    }
                )
            }
        },
        shape = shape,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
    )
}