import java.util.Properties

allprojects {
    group = "io.github.lamba92"
    version = "1.0-SNAPSHOT"
}

tasks.register<Delete>("deleteSdkIconsData") {
    val sdkPath =
        System.getenv("ANDROID_SDK_ROOT")
            ?: System.getenv("ANDROID_HOME")
            ?: readLocalProperties()["sdk.dir"]
    when {
        sdkPath != null -> delete(file("$sdkPath/icons"))
        else -> doLast {
            error("ANDROID_SDK_ROOT, ANDROID_HOME or local.properties not found")
        }
    }
}

fun readLocalProperties(): Map<String, String> {
    val file = rootProject.file("local.properties")
    if (!file.exists()) {
        return emptyMap()
    }
    val properties = Properties()
    val inputStream = file.inputStream()
    properties.load(inputStream)
    return properties.entries.associate { it.key.toString() to it.value.toString() }
}
