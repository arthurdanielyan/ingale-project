plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.featureLocal.navigation.impl"
}

dependencies {

    api(projects.featureLocal.navigation.api)

    implementation(projects.core.navigation)
    implementation(libs.koin.core)
    implementation(projects.core.ui)
    implementation(libs.androidx.compose.navigation)

    // Screens
    implementation(projects.featureLocal.featureHome.presentation)
}
