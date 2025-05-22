plugins {
    id(libs.plugins.kotlin.jvm)
    id(libs.plugins.kotlin.plugin.serialization)
    id(libs.plugins.lamba.docker)
    application
    `convention-ktlint`
}

application {
    mainClass = "io.github.lamba92.corpore.server.MainKt"
}

dependencies {
    api(projects.app.appCore)
    api(libs.logback.classic)
    api(libs.model.contex.protocol)
    api(libs.kotlinx.io.core)
    api(libs.bundles.ktor.server)
}
