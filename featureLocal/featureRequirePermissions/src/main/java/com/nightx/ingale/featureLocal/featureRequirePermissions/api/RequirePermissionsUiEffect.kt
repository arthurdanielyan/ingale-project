package com.nightx.ingale.featureLocal.featureRequirePermissions.api

import com.nightx.ingale.core.viewState.StableList

sealed interface RequirePermissionsUiEffect {
    data class RequestPermission(
        val permissions: StableList<String>
    ) : RequirePermissionsUiEffect
}
