plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.realm)
}

android {
    namespace = "com.nightx.ingale.featureLocal.featureHome.data"
}

dependencies {

    implementation(libs.androidx.ktx)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.utils)
    implementation(projects.resources.strings)
    implementation(projects.core.dataModel)
    implementation(projects.core.domainModel)
    implementation(projects.featureLocal.featureHome.domain)
}
