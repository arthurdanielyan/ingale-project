plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.featureLocal.navigation.graph"
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.featureLocal.navigation.api)
    implementation(projects.core.navigation)
    implementation(libs.androidx.compose.navigation)
    implementation(projects.featureLocal.featureHome.presentation)
    implementation(projects.featureLocal.featureSongsSet.presentation)
}
