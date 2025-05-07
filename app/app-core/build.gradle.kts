import com.android.build.api.dsl.androidLibrary

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.ktlint)
}

kotlin {

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
                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.serialization.json)
                api(libs.kotlinx.datetime)
                api(libs.compose.foundation)
                api(libs.compose.lifecycle.viewmodel)
                api(libs.compose.resources)
                api(libs.compose.material3)
            }
        }
    }
}

ktlint {
    filter {
        exclude("**/generated/**")
    }
}
