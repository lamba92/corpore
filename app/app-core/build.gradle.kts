@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.androidLibrary

plugins {
    `convention-lib`
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

kotlin {
    androidLibrary {
        namespace += ".app.core"
    }
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
                api(projects.commonCore)
            }
        }
    }
}
