plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.featureLocal.navigation.impl"
}

dependencies {

    api(projects.featureLocal.navigation.api)

    implementation(projects.core.decompose)
    implementation(libs.koin.core)
    implementation(projects.core.ui)
    implementation(libs.androidx.compose.navigation)
    implementation(projects.bottomBar.api)

    // Screens
    implementation(projects.featureLocal.featureHome.presentation)
    implementation(projects.featureLocal.featureSongsSet.presentation)
}
