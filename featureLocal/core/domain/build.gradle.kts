plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.featureLocal.core.domain"
}

dependencies {
    implementation(libs.koin.core)
}