plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.featureLocal.navigation.ui"
}

dependencies {

    api(projects.featureLocal.navigation.api)

    implementation(projects.core.decompose)
    implementation(projects.core.ui)

    // Screens
    implementation(projects.featureLocal.featureHome.presentation)
    implementation(projects.featureLocal.featureSongsSet.presentation)
}