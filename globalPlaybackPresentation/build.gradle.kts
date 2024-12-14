plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.realm)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.globalPlaybackPresentation"
}

dependencies {

    implementation(libs.koin.core)

    implementation(projects.core.ui)
    implementation(projects.core.audioPlayer.api)
    implementation(projects.bottomBar.api)
    implementation(projects.core.presentation)
    implementation(projects.resources.strings)
    implementation(projects.resources.icon)
    implementation(projects.resources.playbackActions)
}