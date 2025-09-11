plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.room)
}

android {
    namespace = "com.nightx.ingale.featureLocal.core.data"
}

dependencies {

    implementation(libs.androidx.ktx)
    implementation(libs.koin.core)

    implementation(projects.core.utils)
    implementation(projects.resources.strings)
    implementation(projects.featureLocal.core.domain)
    implementation(projects.featureLocal.featureHome.domain)
}