plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.featureLocal.navigation.impl"
}

dependencies {

    api(projects.featureLocal.navigation.api)

    implementation(projects.core.decompose)
    implementation(libs.koin.core)
    implementation(projects.core.ui)
    implementation(projects.bottomBarApi)

    // Screens
    implementation(projects.featureLocal.featureHome.presentation)
    implementation(projects.featureLocal.featureSongsSet.presentation)
}
