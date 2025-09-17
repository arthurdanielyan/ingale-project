plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.featureLocal.featureHome.presentation"
}

dependencies{

    api(projects.featureLocal.navigation.api) // TODO include navigation and core.decompose separately

    implementation(projects.featureLocal.featureHome.domain)
    implementation(projects.featureLocal.featureRequirePermissions)
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