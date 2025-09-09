plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.core.domainModel"
}

dependencies {
    implementation(libs.koin.core)
}