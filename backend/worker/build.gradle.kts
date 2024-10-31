val kotlin_version: String by project
val logback_version: String by project

application {
    mainClass.set("dev.run.worker.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(project(":common"))

    implementation("ch.qos.logback:logback-classic:$logback_version")
}