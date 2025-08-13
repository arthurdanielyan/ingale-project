plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.root.impl"
}

dependencies {

    api(projects.root.api)

    implementation(projects.core.decompose)
    implementation(projects.featureLocal.navigation.api)
    implementation(libs.koin.core)
    implementation(projects.core.ui)
    implementation(projects.core.utils)
    implementation(projects.bottomBarApi)
    implementation(projects.resources.bottomBar)
    implementation(projects.featureLocal.navigation.ui)
    implementation(projects.globalPlaybackPresentation)
}