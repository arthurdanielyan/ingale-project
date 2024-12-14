package com.nightx.ingale.featureLocal.navigation.impl

import com.nightx.ingale.core.navigation.DefaultNavigatorImpl
import com.nightx.ingale.featureLocal.navigation.api.LocalNavigator
import com.nightx.ingale.feature_local.local_navigation.destinations.LocalScreenDestination

class LocalNavigatorImpl : DefaultNavigatorImpl<LocalScreenDestination>(), LocalNavigator