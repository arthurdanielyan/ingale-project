plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.resources.strings"
}

dependencies {

    implementation(libs.koin.core)
}