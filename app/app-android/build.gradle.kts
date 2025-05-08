plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
}

kotlin {
    jvmToolchain(8)
}

android {
    namespace = "io.gituhb.lamba92.corpore.app"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
}

dependencies {
    api(projects.app.appCore)
//    api(libs.androidx.activity.compose)
}