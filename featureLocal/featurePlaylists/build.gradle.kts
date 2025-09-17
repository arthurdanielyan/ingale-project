plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.featureLocal.featurePlaylists"
}

dependencies {

    implementation(libs.koin.core)

    implementation(projects.featureLocal.navigation.api)
    implementation(projects.core.utils)
    implementation(projects.core.ui)
    implementation(projects.featureLocal.core.domain)
}