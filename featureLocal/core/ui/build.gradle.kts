plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.featureLocal.core.ui"
}

dependencies {

    implementation(projects.core.ui)
    implementation(projects.resources.songOperations)
    implementation(projects.featureLocal.core.domain)
    implementation(projects.core.viewState)
    implementation(projects.core.presentation)
    implementation(projects.core.utils)
    implementation(projects.resources.strings)
}