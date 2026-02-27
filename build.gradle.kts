plugins {
    kotlin("jvm") version "1.9.22"
    // Плагін Ktor
    id("io.ktor.plugin") version "2.3.8"
    // Плагін Shadow Jar
    id("com.github.johnrengelman.shadow") version "8.1.1"
    // Серіалізація JSON
    kotlin("plugin.serialization") version "1.9.22"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:2.3.8")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.8")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:2.3.8")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.8")
    implementation("com.stripe:stripe-java:24.22.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

tasks {
    "shadowJar"(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
        manifest {
            attributes("Main-Class" to "org.example.MainKt")
        }
    }

    kotlin {
        jvmToolchain(21)
    }
}