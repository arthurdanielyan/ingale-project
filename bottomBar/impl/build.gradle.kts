plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.realm)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.bottomBar.impl"
}

dependencies {

    api(projects.bottomBar.api)

    implementation(libs.koin.core)

    implementation(projects.core.ui)
    implementation(projects.core.utils)
    implementation(projects.featureLocal.navigation.graph)
    implementation(projects.musicBar)
    implementation(projects.resources.strings)
    implementation(projects.resources.bottomBar)
}