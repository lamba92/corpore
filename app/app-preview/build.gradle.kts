plugins {
    id(libs.plugins.kotlin.jvm.get().pluginId)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.plugin.serialization)
    application
}

application {
    mainClass = "io.github.lamba92.corpore.app.ui.preview.MainKt"
}

dependencies {
    api(projects.app.appCore)
    api(libs.logback.classic)
    api(libs.bundles.compose.desktop)
}

compose {
    resources {
        // workaround: resources are not included when using a local project dependency
        customDirectory(
            sourceSetName = "main",
            directoryProvider = provider { layout.projectDirectory.dir("../app-core/src/commonMain/composeResources") }
        )
    }
}
