plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.core.decompose"
}

dependencies {

    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    api(libs.decompose.core)
    api(libs.decompose.extensions.compose)
}