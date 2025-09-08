plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.bottomBarApi"
}

dependencies {

    implementation(projects.core.ui)
    implementation(projects.core.decompose)
}