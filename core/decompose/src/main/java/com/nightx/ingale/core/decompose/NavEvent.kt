package com.nightx.ingale.core.decompose

import android.os.Parcelable

interface NavEvent

data class NavigateEvent<A: Parcelable, R: Parcelable>(
    val destination: Destination,
    val argument: A?,
    val onResult: ((R) -> Unit)?,
): NavEvent

data object NavigateUpEvent : NavEvent