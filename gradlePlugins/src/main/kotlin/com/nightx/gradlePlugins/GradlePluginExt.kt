package com.nightx.gradlePlugins

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler

val Project.versionCatalog: VersionCatalog
    get() =
        extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("libs")

fun VersionCatalog.getPlugin(alias: String): String =
    findPlugin(alias).get().get().pluginId

fun VersionCatalog.getLib(lib: String): MinimalExternalModuleDependency =
    findLibrary(lib).get().get()

fun DependencyHandler.implementation(dependency: Any) =
    add("implementation", dependency)