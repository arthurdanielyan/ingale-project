plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.featureLocal.featureHome.domain"
}

dependencies {

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)

    implementation(projects.featureLocal.core.domain)
    implementation(projects.core.utils)
}
