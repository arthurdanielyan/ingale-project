plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.core.navigation"
}

dependencies {

    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
}