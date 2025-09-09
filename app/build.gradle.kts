plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
//    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.nightx.ingale"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nightx.ingale"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        compose = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//            signingConfig = signingConfigs.debug
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    // Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    implementation(projects.core.ui)
    implementation(projects.bottomBarApi)
    implementation(projects.globalPlaybackPresentation)
    implementation(projects.core.audioPlayer.impl)
    implementation(projects.core.dataModel)
    implementation(projects.core.domainModel)
    implementation(projects.core.utils)
    implementation(projects.core.presentation)
    implementation(projects.featureLocal.core.ui)
    implementation(projects.featureLocal.featureHome.data)
    implementation(projects.featureLocal.featureHome.domain)
    implementation(projects.featureLocal.featureHome.presentation)
    implementation(projects.featureLocal.featureRequirePermissions)
    implementation(projects.featureLocal.featureSongsSet.presentation)
    implementation(projects.featureLocal.navigation.impl)
    implementation(projects.resources.strings)
    implementation(projects.root.impl)
}