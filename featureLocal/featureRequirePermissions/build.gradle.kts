plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.featureLocal.featureRequirePermissions"
}

dependencies {

    implementation(libs.koin.core)
    implementation(libs.androidx.activity.compose)

    implementation(projects.core.utils)
    implementation(projects.core.ui)
    implementation(projects.core.decompose)
    implementation(projects.core.presentation)
    implementation(projects.resources.strings)
}