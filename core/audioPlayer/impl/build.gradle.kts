plugins {
    alias(libs.plugins.ingale.android.library)
}

android {
    namespace = "com.nightx.ingale.core.audioPlayer.impl"
}

dependencies {

    api(projects.core.audioPlayer.api)

    implementation(libs.androidx.media)
    implementation(libs.androidx.ktx)
    implementation(libs.koin.android)

    implementation(projects.bottomBarApi)
    implementation(projects.core.utils)
    implementation(projects.core.domainModel)
    implementation(projects.resources.icon)
    implementation(projects.resources.playbackActions)
    implementation(projects.resources.strings)
}