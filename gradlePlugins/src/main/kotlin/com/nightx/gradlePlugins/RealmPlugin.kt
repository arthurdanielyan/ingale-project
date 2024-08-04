package com.nightx.gradlePlugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import kotlin.apply as kotlinApply

class RealmPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.kotlinApply {
            applyRealmPlugin()
            applyRealmDependencies()
        }
    }

    private fun Project.applyRealmPlugin() {
        val libs = versionCatalog

        pluginManager.kotlinApply {
            apply(libs.getPlugin("android-library"))
            apply(libs.getPlugin("jetbrains-kotlin-android"))
        }
    }

    private fun Project.applyRealmDependencies() {
        dependencies {
            implementation(versionCatalog.getLib("realm-kotlin"))
        }
    }
}