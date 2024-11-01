plugins {
    kotlin("jvm") version("2.0.21") apply(false)
    kotlin("plugin.serialization") version ("2.0.21") apply(false)
    id("io.ktor.plugin") version("2.3.12") apply(false)
}

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.serialization")
        plugin("io.ktor.plugin")
    }

    val kotlin_version: String by project
    val ktor_version: String by project
    val logback_version: String by project

    group = "dev.run"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    val implementation by configurations
    val testImplementation by configurations

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
        implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
        implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
        implementation("io.ktor:ktor-network:$ktor_version")
        implementation("com.rabbitmq:amqp-client:5.22.0")
        implementation("io.insert-koin:koin-core:4.0.0")
        implementation("io.insert-koin:koin-ktor:4.0.0")

        testImplementation("io.ktor:ktor-server-test-host-jvm")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    }
}