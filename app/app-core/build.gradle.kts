import com.android.build.api.dsl.androidLibrary

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.ktlint)
    `maven-publish`
}

publishing {
    repositories {
        maven(layout.buildDirectory.dir("mavenTestRepository")) {
            name = "test"
        }
    }
}

kotlin {
    jvmToolchain(11)
    androidLibrary {
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        namespace = "io.gituhb.lamba92.corpore.app.core"
        compileSdk = 35
        minSdk = 24
    }
    jvm()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                api(libs.coil.compose)
                api(libs.coil.svg)
                api(libs.compose.foundation)
                api(libs.compose.lifecycle.viewmodel)
                api(libs.compose.material3)
                api(libs.compose.navigation)
                api(libs.compose.resources)
                api(libs.koin.compose.viewmodel)
                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.datetime)
                api(libs.kotlinx.serialization.json)
            }
        }
        jvmMain {
            dependencies {
                api(libs.kotlinx.coroutines.swing)
            }
        }
    }
}

ktlint {
    filter {
        exclude("**/generated/**")
    }
}
