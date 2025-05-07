package com.nightx.ingale.featureLocal.featureRequirePermissions.api

import com.nightx.ingale.core.viewState.ComposeList

sealed interface RequirePermissionsUiEffect {
    data class RequestPermission(
        val permissions: ComposeList<String>
    ) : RequirePermissionsUiEffect
}
