plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.realm)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.bottomBar.api"
}

dependencies {

    implementation(projects.core.ui)
}