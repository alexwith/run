plugins {
    kotlin("jvm") version("2.0.21") apply(false)
    id("io.ktor.plugin") version("2.3.12") apply(false)
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("io.ktor.plugin")
    }

    val kotlin_version: String by project
    val logback_version: String by project

    group = "dev.run"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }
}