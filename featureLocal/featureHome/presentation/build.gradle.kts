plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.featureLocal.featureHome.presentation"
}

dependencies{

    api(projects.featureLocal.navigation.api)

    implementation(projects.featureLocal.featureHome.domain)
    implementation(projects.featureLocal.featureRequirePermissions)
    implementation(projects.featureLocal.core.ui)
    implementation(projects.globalPlaybackPresentation)
    implementation(projects.bottomBarApi)
    implementation(projects.core.domainModel)
    implementation(projects.core.audioPlayer.api)
    implementation(projects.core.ui)
    implementation(projects.core.presentation)
    implementation(projects.core.utils)
    implementation(projects.resources.strings)
}