package com.example.ingale.feature_local.local_core.domain.functions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.ingale.ui.theme.colorScheme.ingaleColors

@Composable
fun String.highlight(highlight: String): AnnotatedString = buildAnnotatedString {
    if(highlight.isBlank()) {
        append(this@highlight)
        return@buildAnnotatedString
    }
    val parts = this@highlight.splitBy(highlight)
    parts.forEach { part ->
        if(part.lowercase() == highlight.lowercase()) {
            withStyle(SpanStyle(color = MaterialTheme.ingaleColors.backgroundInverse)) {
                append(part)
            }
        } else append(part)
    }
}

/**
 * Splits this String to a list of Strings around occurrences of the specified delimiters preserving
 * delimiters
 * */
private fun String.splitBy(delimiter: String): List<String> {
    var rest = this
    val result = mutableListOf<String>()
    while(rest.contains(delimiter, true)) {
        val firstDelIndex = rest.indexOf(delimiter, ignoreCase = true)
        result.add(rest.substring(0, firstDelIndex))
        result.add(rest.substring(firstDelIndex, firstDelIndex + delimiter.length))
        rest = rest.substring(firstDelIndex + delimiter.length)
    }
    if(!this.endsWith(result.lastOrNull() ?: "")) {
        result.add(rest)
    }

    return result.apply { removeIf { it.isBlank() } }
}