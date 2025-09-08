plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.room)
}

android {
    namespace = "com.nightx.ingale.core.dataModel"
}

dependencies {

    implementation(libs.koin.core)

    implementation(projects.core.utils)
    implementation(projects.core.domainModel)
}