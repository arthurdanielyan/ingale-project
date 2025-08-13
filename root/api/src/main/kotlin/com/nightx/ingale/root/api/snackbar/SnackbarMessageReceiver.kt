package com.nightx.ingale.root.api.snackbar

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow

@Immutable
interface SnackbarMessageReceiver {

    val snackbarMessage: Flow<String>
}
