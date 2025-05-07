plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.root.api"
}

dependencies {

    implementation(projects.core.decompose)
    implementation(projects.core.utils)
    implementation(libs.androidx.compose.runtime)
    implementation(projects.resources.strings)
    implementation(projects.resources.bottomBar)
    implementation(projects.globalPlaybackPresentation)
}