val kotlin_version: String by project
val logback_version: String by project

application {
    mainClass.set("dev.run.worker.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.insert-koin:koin-core:4.0.0")
    implementation("io.insert-koin:koin-ktor:4.0.0")
    implementation("com.rabbitmq:amqp-client:5.22.0")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}