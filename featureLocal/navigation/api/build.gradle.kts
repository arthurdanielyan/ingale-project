plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.kotlinx.parcelize)
}

android {
    namespace = "com.nightx.ingale.featureLocal.navigation.api"
}

dependencies {

    api(projects.core.navigation)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
}