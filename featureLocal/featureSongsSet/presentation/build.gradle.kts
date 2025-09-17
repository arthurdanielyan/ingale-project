plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.featureLocal.featureSongsSet.presentation"
}

dependencies{

    implementation(projects.featureLocal.navigation.api)
    implementation(projects.featureLocal.featureHome.domain)
    implementation(projects.featureLocal.core.presentation)
    implementation(projects.globalPlaybackPresentation)
    implementation(projects.bottomBarApi)
    implementation(projects.featureLocal.core.domain)
    implementation(projects.core.audioPlayer.api)
    implementation(projects.core.ui)
    implementation(projects.core.presentation)
    implementation(projects.core.utils)
    implementation(projects.resources.strings)
}