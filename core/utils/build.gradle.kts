plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.core.utils"
}

dependencies {

    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
}