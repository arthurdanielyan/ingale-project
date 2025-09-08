import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("ingaleAndroidPlugin") {
            id = "nightx.ingale.android.library"
            implementationClass = "com.nightx.gradlePlugins.IngaleAndroidLibraryPlugin"
            version = "1.0.0"
        }
        create("usesComposePlugin") {
            id = "nightx.ingale.uses.compose"
            implementationClass = "com.nightx.gradlePlugins.UsesComposePlugin"
            version = "1.0.0"
        }
        create("roomApplierPlugin") {
            id = "nightx.ingale.room"
            implementationClass = "com.nightx.gradlePlugins.RoomPlugin"
            version = "1.0.0"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())

    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.android.tools.build.gradle)
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
