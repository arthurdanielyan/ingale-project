plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.realm)
}

android {
    namespace = "com.nightx.ingale.bottomBar.api"
}

dependencies {

    implementation(projects.core.ui)
}