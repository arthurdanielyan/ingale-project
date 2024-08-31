package com.nightx.gradlePlugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class UsesComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.android {
            buildFeatures {
                compose = true
            }
            composeOptions {
                kotlinCompilerExtensionVersion = "1.5.14"
            }
        }
    }
}