plugins {
    kotlin("jvm") version "1.8.21"
    id("io.ktor.plugin") version "2.3.7"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("io.ktor:ktor-server-websockets:2.3.7")
}

application {
    mainClass.set("com.example.ApplicationKt")
}
