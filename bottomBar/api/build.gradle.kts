plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.realm)
    alias(libs.plugins.ingale.uses.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.nightx.ingale.bottomBar.api"
}

dependencies {

    implementation(projects.core.ui)
    implementation(projects.core.decompose)
}