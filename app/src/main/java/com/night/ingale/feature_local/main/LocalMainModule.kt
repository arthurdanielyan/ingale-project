package com.night.ingale.feature_local.main

import com.night.ingale.feature_local.main.data.dataModule
import com.night.ingale.feature_local.main.domain.domainModule
import com.night.ingale.feature_local.main.presentation.presentationModule

val localMainModule = dataModule + domainModule + presentationModule