package com.nightx.ingale.feature_local.main

import com.nightx.ingale.feature_local.main.data.dataModule
import com.nightx.ingale.feature_local.main.domain.domainModule
import com.nightx.ingale.feature_local.main.presentation.presentationModule

val localMainModule = dataModule + domainModule + presentationModule