package io.dmm.dsl.examples

import api.DSLManager
import api.create
import dsl.domains.gradle.GradleDSL
import dsl.domains.gradle.references.MavenPublication
import dsl.domains.gradle.references.components
import dsl.framework.DSLType


fun main() {
    val dslManager = DSLManager()

    // Создание Gradle DSL с использованием Fluent API
    val gradleDSL = dslManager.create {
        type(DSLType.GRADLE)
        language("Kotlin")
        features {
            enableTypeInference()
            enableSmartCasts()
            enableExtensionFunctions()
            enableNullableTypes()
        }
    } as GradleDSL

    // Построение Gradle скрипта
    val buildScript = gradleDSL {
        publishing {
            repositories {
                mavenLocal()
                maven("https://repo1.maven.org/maven2/")
            }
            publications {
                create<MavenPublication>("java") {
                    from(components["java"])
                }
                create<MavenPublication>("kotlin") {
                    from(components["kotlin"])
                }
            }
        }

        signing {
            useInMemoryPgpKeys("dummy-key-id", "dummy-password")
            sign(publishing.publications)
        }
    }

    // Генерация строкового представления
    val generatedCode = gradleDSL.generate(buildScript)
    println("Generated Gradle script:")
    println(generatedCode)
    println("\n" + "=".repeat(50))
    // Также можно создать DSL для Groovy
    val groovyGradleDSL = dslManager.create {
        type(DSLType.GRADLE)
        language("Groovy")
        features {
            enableGroovyClosures()
            enableGradlePlugins()
        }
    } as GradleDSL

    val buildScriptGroovy = groovyGradleDSL {
        publishing {
            repositories {
                mavenLocal()
                maven("https://repo1.maven.org/maven2/")
            }
            publications {
                create<MavenPublication>("java") {
                    from(components["java"])
                }
                create<MavenPublication>("kotlin") {
                    from(components["kotlin"])
                }
            }
        }

        signing {
            useInMemoryPgpKeys("dummy-key-id", "dummy-password")
            sign(publishing.publications)
        }
    }
    println("Generated Gradle script:")
    println(groovyGradleDSL.generate(buildScriptGroovy))
}

