plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.core.audioPlayer.api"
}

dependencies {

    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.featureLocal.core.domain)
}