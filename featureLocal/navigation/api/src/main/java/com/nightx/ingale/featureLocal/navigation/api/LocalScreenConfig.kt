package com.nightx.ingale.featureLocal.navigation.api

import com.nightx.ingale.core.decompose.ScreenConfig
import kotlinx.serialization.Serializable

@Serializable
sealed interface LocalScreenConfig : ScreenConfig {

    @Serializable
    data object Home : LocalScreenConfig
}