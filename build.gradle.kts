plugins {
    kotlin("jvm") version "2.1.20"
}

allprojects {
    group = "io.dmm.dsl"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        val implementation by configurations
        val testImplementation by configurations

        implementation(kotlin("stdlib"))
        implementation("com.squareup:kotlinpoet:2.2.0")

        testImplementation(kotlin("test"))
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
        testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
        testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    }

    kotlin {
        jvmToolchain(21)
    }

    tasks.test {
        useJUnitPlatform()
    }
}