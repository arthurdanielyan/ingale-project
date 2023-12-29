package com.example.ingale.feature_local.main

import com.example.ingale.feature_local.main.data.dataModule
import com.example.ingale.feature_local.main.domain.domainModule
import com.example.ingale.feature_local.main.presentation.presentationModule

val localMainModule = dataModule + domainModule + presentationModule