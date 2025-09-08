package com.nightx.gradlePlugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler

internal val Project.versionCatalog: VersionCatalog
    get() =
        extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("libs")

internal fun VersionCatalog.getPlugin(alias: String): String =
    findPlugin(alias).get().get().pluginId

internal fun VersionCatalog.getLib(lib: String): MinimalExternalModuleDependency =
    findLibrary(lib).get().get()

internal fun DependencyHandler.implementation(dependency: Any) =
    add("implementation", dependency)

internal fun DependencyHandler.ksp(dependency: Any) =
    add("ksp", dependency)

internal fun Project.android(config: LibraryExtension.() -> Unit) {
    extensions.getByType(LibraryExtension::class.java).apply(config)
}
