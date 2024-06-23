package com.nightx.gradlePlugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import kotlin.apply as kotlinApply

class IngaleAndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        applyPlugins(target)
        setProjectConfig(target)
    }

    private fun applyPlugins(project: Project) {
        val libs = project
            .extensions
            .getByType(VersionCatalogsExtension::class.java)
            .named("libs")

        project.pluginManager.kotlinApply {
            apply(libs.getPluginId("android-library"))
            apply(libs.getPluginId("jetbrains-kotlin-android"))
        }
    }

    private fun setProjectConfig(project: Project) {
        project.android().kotlinApply {
            compileSdk = LibraryProjectConfig.compileSdk

            defaultConfig {
                minSdk = LibraryProjectConfig.minSdk

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
    }

    private fun Project.android(): LibraryExtension {
        return extensions.getByType(LibraryExtension::class.java)
    }

    private fun VersionCatalog.getPluginId(alias: String): String =
        findPlugin(alias).get().get().pluginId
}