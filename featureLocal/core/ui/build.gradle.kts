plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.featureLocal.core.ui"
}

dependencies {

    implementation(projects.core.ui)
    implementation(projects.featureLocal.core.viewState)
    implementation(projects.resources.songOperations)
}