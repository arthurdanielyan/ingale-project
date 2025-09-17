plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.kotlinx.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.nightx.ingale.featureLocal.navigation.api"
}

dependencies {

    api(projects.core.decompose)// TODO make implementation, include navigation and core.decompose separately

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.kotlinx.serialization.json)
}