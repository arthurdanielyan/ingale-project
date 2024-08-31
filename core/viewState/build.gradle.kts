plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.core.viewState"
}

dependencies {

    implementation(libs.koin.core)

    implementation(projects.core.utils)
    implementation(projects.core.domainModel)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
}