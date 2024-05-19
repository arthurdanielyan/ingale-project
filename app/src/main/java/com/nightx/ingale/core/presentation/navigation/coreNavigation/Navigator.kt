package com.nightx.ingale.core.presentation.navigation.coreNavigation

import android.os.Parcelable
import kotlinx.coroutines.flow.Flow

interface Navigator<in T: Destination, out E: NavEvent> {

    /**
     * For UI to collect
     * */
    val navigationEvent: Flow<E>

    fun <A: Parcelable, R: Parcelable> navigate(
        destination: T,
        argument: A,
        onResult: (R) -> Unit
    )

    fun <A: Parcelable> navigate(
        destination: T,
        argument: A
    )

    fun <R: Parcelable> navigate(
        destination: T,
        onResult: (R) -> Unit
    )

    fun navigate(destination: T)

    fun navigateUp()
}
