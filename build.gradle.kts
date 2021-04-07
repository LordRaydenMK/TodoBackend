import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:6.1.0")
    }
}

plugins {
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.serialization") version "1.4.31"
}

group = "io.github.lordraydenmk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    val className = "io.ktor.server.netty.EngineMain"
    mainClass.set(className)
    // https://github.com/johnrengelman/shadow/issues/336
    mainClassName = className
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes")
}

dependencies {
    implementation(kotlin("stdlib"))
    val ktor_version = "1.5.3"
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClass.get()
            )
        )
    }
}