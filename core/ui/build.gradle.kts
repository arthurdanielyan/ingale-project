plugins {
    alias(libs.plugins.ingale.android.library)
    alias(libs.plugins.ingale.uses.compose)
}

android {
    namespace = "com.nightx.ingale.core.ui"
}

dependencies {

    implementation(libs.androidx.ktx)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.runtime)

    api(libs.androidx.activity.compose)
    api(libs.decompose.extensions.compose)
    api(libs.decompose.core)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material3)
    api(libs.androidx.lifecycle.runtime.compose)
    api(libs.androidx.compose.constraintlayout)
    api(libs.androidx.constraintlayout.solver)
    api(projects.core.viewState)
    api(libs.coil.compose)

    implementation(projects.resources.strings)
    implementation(projects.resources.icon)
}