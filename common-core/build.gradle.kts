@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.androidLibrary

plugins {
    `convention-lib`
}

kotlin {
    androidLibrary {
        namespace += ".common.core"
    }
    sourceSets {
        commonMain {
            dependencies {
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
