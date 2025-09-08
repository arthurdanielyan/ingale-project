package com.nightx.gradlePlugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RoomPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.applyKspPlugin()
        target.applyRoomDependencies()
    }

    private fun Project.applyKspPlugin() {
        val libs = versionCatalog

        pluginManager.apply {
            apply(libs.getPlugin("ksp"))
        }
    }

    private fun Project.applyRoomDependencies() {
        dependencies {
            implementation(versionCatalog.getLib("room-runtime"))
            implementation(versionCatalog.getLib("room-ktx"))
            ksp(versionCatalog.getLib("room-compiler"))
        }
    }
}