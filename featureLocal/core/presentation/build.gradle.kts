plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.nightx.ingale.featureLocal.core.presentation"
}

dependencies {

    implementation(projects.core.decompose)
    implementation(projects.core.ui)
    implementation(projects.core.audioPlayer.api)
    implementation(projects.bottomBarApi)
    implementation(projects.resources.songOperations)
    implementation(projects.featureLocal.core.domain)
    implementation(projects.core.viewState)
    implementation(projects.core.presentation)
    implementation(projects.core.utils)
    implementation(projects.resources.strings)
}